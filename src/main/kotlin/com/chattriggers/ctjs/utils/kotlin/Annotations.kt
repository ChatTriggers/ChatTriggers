package com.chattriggers.ctjs.utils.kotlin

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.ModuleManager
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.LoaderException
import net.minecraftforge.fml.common.ModContainer
import net.minecraftforge.fml.common.discovery.ASMDataTable
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Modifier
import kotlin.reflect.full.companionObjectInstance


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class KotlinListener

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ModuleLoader

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class External

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class NotAbstract

object AnnotationHandler {
    private val LOGGER = LogManager.getLogger(AnnotationHandler::class.java)

    private val registered = mutableSetOf<Any>()

    fun subscribeAutomatic(mod: ModContainer, asm: ASMDataTable) {
        val modAnnotations = asm.getAnnotationsFor(mod) ?: return

        val listeners = modAnnotations.get(KotlinListener::class.java.name)
        val loaders = modAnnotations.get(ModuleLoader::class.java.name)

        val classLoader = Loader.instance().modClassLoader

        for (listener in listeners) {
            try {
                val subscriberClass = Class.forName(listener.className, false, classLoader) ?: continue
                val kotlinClass = subscriberClass.kotlin
                val objectInstance = kotlinClass.objectInstance ?: kotlinClass.companionObjectInstance ?: continue

                if (hasObjectEventHandlers(objectInstance) && objectInstance !in registered) {
                    MinecraftForge.EVENT_BUS.register(objectInstance)
                    registered += objectInstance
                    LOGGER.debug("Registered @KotlinListener object instance {}", listener.className)
                }

            } catch (e: Throwable) {
                LOGGER.error("An error occurred trying to load an @KotlinListener object {} for modid {}", mod.modId, e)
                throw LoaderException(e)
            }
        }

        for (loader in loaders) {
            try {
                val loaderClass = Class.forName(loader.className, false, classLoader) ?: continue
                val kotlinClass = loaderClass.kotlin
                val objectInstance = kotlinClass.objectInstance ?: kotlinClass.companionObjectInstance ?: continue
                val loaderInstance: ILoader = objectInstance as? ILoader
                        ?: continue

                ModuleManager.loaders.add(loaderInstance)
            } catch (e: Throwable) {
                LOGGER.error("An error occurred trying to load an @KotlinListener object {} for modid {}", mod.modId, e)
                throw LoaderException(e)
            }
        }
    }

    private fun hasObjectEventHandlers(objectInstance: Any): Boolean {
        return objectInstance.javaClass.methods.any {
            !Modifier.isStatic(it.modifiers) && it.isAnnotationPresent(SubscribeEvent::class.java)
        }
    }
}