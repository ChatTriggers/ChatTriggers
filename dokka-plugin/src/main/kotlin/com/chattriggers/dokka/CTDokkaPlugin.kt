package com.chattriggers.dokka

import com.chattriggers.dokka.DataWriter.createDataPage
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.base.signatures.KotlinSignatureUtils.dri
import org.jetbrains.dokka.links.DRI
import org.jetbrains.dokka.model.*
import org.jetbrains.dokka.model.properties.WithExtraProperties
import org.jetbrains.dokka.pages.DriResolver
import org.jetbrains.dokka.pages.RendererSpecificResourcePage
import org.jetbrains.dokka.pages.RenderingStrategy
import org.jetbrains.dokka.pages.RootPageNode
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.transformers.documentation.DocumentableTransformer
import org.jetbrains.dokka.transformers.pages.PageTransformer

class CTDokkaPlugin : DokkaPlugin() {
    val transformer by extending {
        CoreExtensions.documentableTransformer with Extractor
    }

    val preprocessor by extending {
        CoreExtensions.pageTransformer with DataWriter
    }
}

data class RenderedItem(
    val name: String,
    val descriptor: String,
    val url: String,
)

data class DocumentableData(
    val dri: DRI,
    val sourceSets: Set<DisplaySourceSet>,
    val name: String,
    val descriptor: String,
) {
    fun render(url: String) = RenderedItem(name, descriptor, url)
}

object Extractor : DocumentableTransformer {
    val list = mutableListOf<DocumentableData>()
    val objectNames = mutableSetOf<String>()

    override fun invoke(original: DModule, context: DokkaContext): DModule {
        return original.also {
            it.withDescendants().forEach { documentable ->
                val name = documentable.name ?: return@forEach

                val descriptor = when (documentable) {
                    is DClass -> "class $name"
                    is DEnum -> "enum $name"
                    is DInterface -> "interface $name"
                    is DObject -> {
                        objectNames.add(name)
                        "object $name"
                    }
                    is DFunction -> {
                        if (documentable.receiver != null)
                            return@forEach
                        makeFunctionDescriptor(documentable)
                    }
                    is DProperty -> {
                        if (documentable.receiver != null)
                            return@forEach
                        makePropertyDescriptor(documentable)
                    }
                    else -> return@forEach
                }

                list.add(
                    DocumentableData(
                        documentable.dri,
                        documentable.sourceSets.toDisplaySourceSets(),
                        name,
                        descriptor,
                    )
                )
            }
        }
    }
}

private fun hasStaticAnnotation(annotations: Annotations?) = annotations?.directAnnotations?.any { (_, list) ->
    list.any { it.dri.packageName == "kotlin.jvm" && it.dri.classNames == "JvmStatic" }
} ?: false

private fun isInObject(name: String) = name in Extractor.objectNames || name.endsWith(".Companion")

private fun makePropertyDescriptor(property: DProperty) = buildString {
    val dri = property.dri
    val outerClassName = dri.classNames
    if (outerClassName != null) {
        append(outerClassName.replace(".Companion", ""))

        val separator = when {
            !isInObject(outerClassName) -> "."
            hasStaticAnnotation(property.extra[Annotations]) -> ".INSTANCE."
            else -> "#"
        }

        append(separator)
    }

    append(property.name)
    append(": ")
    append(renderProjection(property.type))
}

private fun makeFunctionDescriptor(function: DFunction) = buildString {
    val dri = function.dri
    val outerClassName = dri.classNames
    if (outerClassName != null) {
        append(outerClassName.replace(".Companion", ""))

        if (!function.isConstructor) {
            val separator = when {
                !isInObject(outerClassName) -> "#"
                !hasStaticAnnotation(function.extra[Annotations]) -> ".INSTANCE."
                else -> "."
            }

            append(separator)
        }
    }

    if (!function.isConstructor)
        append(function.name)

    append('(')

    function.parameters.forEachIndexed { i, param ->
        append(param.name)
        append(": ")
        append(renderProjection(param.type))

        if (i != function.parameters.lastIndex)
            append(", ")
    }

    append(')')

    if (!function.isConstructor) {
        append(": ")
        append(renderProjection(function.type))
    }
}

private fun renderProjection(p: Projection): String {
    return when(p) {
        is TypeParameter -> p.name
        is FunctionalTypeConstructor -> {
            buildString {
                if (p.isExtensionFunction) {
                    append(renderProjection(p.projections.first()))
                    append(".")
                }

                val args = if (p.isExtensionFunction)
                    p.projections.drop(1)
                else
                    p.projections

                append("(")
                args.subList(0, args.size - 1).forEachIndexed { i, arg ->
                    append(renderProjection(arg))
                    if (i < args.size - 2) append(", ")
                }
                append(") -> ")
                append(renderProjection(args.last()))
            }
        }
        is GenericTypeConstructor -> p.dri.classNames.orEmpty()
        is Variance<*> -> ("$p ".takeIf { it.isNotBlank() } ?: "") + renderProjection(p.inner)
        is Star -> "*"
        is Nullable -> "${renderProjection(p.inner)}?"
        is TypeAliased -> renderProjection(p.typeAlias)
        is JavaObject -> "Any"
        is Void -> "Unit"
        is PrimitiveJavaType -> {
            renderProjection(GenericTypeConstructor(
                dri = p.dri,
                projections = emptyList(),
                presentableName = null
            ))
        }
        is Dynamic -> "dynamic"
        is UnresolvedBound -> p.name
    }
}

object DataWriter : PageTransformer {
    private val mapper = jacksonObjectMapper()

    private fun createDataPage() = RendererSpecificResourcePage(
        name = "docs-data.json",
        children = emptyList(),
        strategy = RenderingStrategy.DriLocationResolvableWrite { resolver ->
            val data = Extractor.list.map { it.render(resolver(it.dri, it.sourceSets)!!) }
            mapper.writeValueAsString(data)
        }
    )

    override fun invoke(input: RootPageNode) = input.modified(children = input.children + createDataPage())
}
