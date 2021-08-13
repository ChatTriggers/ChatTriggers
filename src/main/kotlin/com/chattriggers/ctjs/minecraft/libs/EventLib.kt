package com.chattriggers.ctjs.minecraft.libs

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCITextComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.client.event.sound.PlaySoundEvent
import net.minecraftforge.fml.client.event.ConfigChangedEvent

//TODO: figure out what is not needed anymore after the kotlin conversion and remove
@External
object EventLib {
    @JvmStatic
    fun getButtonState(event: MouseEvent): Boolean {
        //#if MC<=10809
        return event.buttonstate
        //#else
        //$$ return event.isButtonstate
        //#endif
    }

    @JvmStatic
    fun getType(event: ClientChatReceivedEvent): Int {
        //#if MC<=10809
        return event.type.toInt()
        //#else
        //$$ return event.type.id.toInt()
        //#endif
    }

    @JvmStatic
    fun getMessage(event: ClientChatReceivedEvent): MCITextComponent {
        return event.message
    }

    @JvmStatic
    fun getName(event: PlaySoundEvent): String {
        return event.name
    }

    @JvmStatic
    fun getModId(event: ConfigChangedEvent.OnConfigChangedEvent): String {
        return event.modID
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
            else -> throw IllegalArgumentException()
        }
    }
}
