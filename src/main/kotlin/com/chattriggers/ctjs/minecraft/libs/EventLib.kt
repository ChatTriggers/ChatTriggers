package com.chattriggers.ctjs.minecraft.libs

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.sound.PlaySoundEvent
import net.minecraftforge.fml.common.eventhandler.Event
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

//TODO: figure out what is not needed anymore after the kotlin conversion and remove
object EventLib {
    @JvmStatic
    fun getType(event: ClientChatReceivedEvent): Int {
        //#if MC<=10809
        return event.type.toInt()
        //#else
        //$$ return event.type.ordinal
        //#endif
    }

    @JvmStatic
    fun getMessage(event: ClientChatReceivedEvent): UTextComponent {
        return UTextComponent(event.message)
    }

    @JvmStatic
    fun getName(event: PlaySoundEvent): String {
        return event.name
    }

    /**
     * Cancel an event. Automatically used with `cancel(event)`.
     *
     * @param event the event to cancel
     * @throws IllegalArgumentException if event can be cancelled "normally"
     */
    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun cancel(event: Any) {
        when (event) {
            is PlaySoundEvent ->
                //#if MC<=10809
                event.result = null
            //#else
            //$$ event.resultSound =null
            //#endif
            is CancellableEvent -> event.setCanceled(true)
            is Event -> if (event.isCancelable) {
                event.isCanceled = true
            } else throw IllegalArgumentException("Attempt to cancel non-cancelable event ${event.javaClass.name}")
            is CallbackInfo -> event.cancel()
            else -> throw IllegalArgumentException("cancel() expects an Event but received ${event.javaClass.name}")
        }
    }
}
