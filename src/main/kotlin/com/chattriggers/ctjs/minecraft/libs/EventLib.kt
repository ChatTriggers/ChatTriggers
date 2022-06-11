package com.chattriggers.ctjs.minecraft.libs

import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent
import com.chattriggers.ctjs.minecraft.listeners.events.ChatEvent
import gg.essential.universal.wrappers.message.UTextComponent
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

//TODO: figure out what is not needed anymore after the kotlin conversion and remove
object EventLib {
    @JvmStatic
    fun getMessage(event: ChatEvent): UTextComponent {
        return UTextComponent(event.message)
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
            is CancellableEvent -> event.setCanceled(true)
            is CallbackInfo -> event.cancel()
            else -> throw IllegalArgumentException("cancel() expects an Event but received ${event.javaClass.name}")
        }
    }
}
