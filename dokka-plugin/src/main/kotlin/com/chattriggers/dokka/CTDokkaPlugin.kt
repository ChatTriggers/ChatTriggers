package com.chattriggers.dokka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.base.signatures.KotlinSignatureUtils.dri
import org.jetbrains.dokka.links.DRI
import org.jetbrains.dokka.model.*
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

data class DocumentableData(val dri: DRI, val sourceSets: Set<DisplaySourceSet>, val string: String)

object Extractor : DocumentableTransformer {
    private val _list = mutableListOf<DocumentableData>()
    val list: List<DocumentableData>
        get() = _list

    override fun invoke(original: DModule, context: DokkaContext): DModule {
        return original.also {
            it.withDescendants().forEach { documentable ->
                val rendered = when (documentable) {
                    is DClasslike -> documentable.name!!
                    is DFunction -> renderFunction(documentable)
                    is DProperty -> buildString {
                        val dri = documentable.dri
                        if (dri.classNames != null) {
                            append(dri.classNames)
                            append(".")
                        }
                        append(documentable.name)
                        append(": ")
                        append(renderProjection(documentable.type))
                    }
                    else -> null
                }
                if (rendered != null) {
                    _list.add(DocumentableData(documentable.dri, documentable.sourceSets.toDisplaySourceSets(), rendered))
                }
            }
        }
    }
}

fun renderFunction(function: DFunction): String {
    return buildString {
        val dri = function.dri
        if (dri.classNames != null) {
            append(dri.classNames)
            append(".")
        }
        append(function.name)
        append("(")
        function.parameters.forEachIndexed { i, param ->
            append(param.name)
            append(": ")
            append(renderProjection(param.type))
            if (i < function.parameters.size - 1) append(", ")
        }
        append("): ")
        append(renderProjection(function.type))
    }
}

fun renderProjection(p: Projection): String {
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
    val mapper = jacksonObjectMapper()

    fun createDataPage(): RendererSpecificResourcePage {
        fun getData(locationResolver: DriResolver) =
            Extractor.list.associate {
                it.string to locationResolver(it.dri, it.sourceSets)
            }


        return RendererSpecificResourcePage(
            name = "docs-data.json",
            children = emptyList(),
            strategy = RenderingStrategy.DriLocationResolvableWrite {
                mapper.writeValueAsString(getData(it))
            }
        )
    }

    override fun invoke(input: RootPageNode): RootPageNode {
        return input.modified(
            children = input.children + createDataPage()
        )
    }
}