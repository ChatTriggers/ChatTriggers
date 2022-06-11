package com.chattriggers.ctjs.minecraft.listeners.events

import net.minecraft.util.IChatComponent

//#if MC>=11701
//$$ import java.util.UUID
//#endif

class ChatEvent(
    var message: IChatComponent,
    val type: Byte,
    //#if MC>=11701
    //$$ val uuid: UUID,
    //#endif
) : CancellableEvent()
