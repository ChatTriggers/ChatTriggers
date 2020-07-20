package com.chattriggers.ctjs.launch.plugin

import me.falsehonesty.asmhelper.dsl.At
import me.falsehonesty.asmhelper.dsl.InjectionPoint
import me.falsehonesty.asmhelper.dsl.inject
import me.falsehonesty.asmhelper.dsl.instructions.*

fun injectNetworkManager() = inject {
    className = "net/minecraft/network/NetworkManager"
    methodName = "channelRead0"
    methodDesc = "(Lio/netty/channel/ChannelHandlerContext;L$PACKET;)V"

    methodMaps = mapOf("func_148833_a" to "processPacket")

    at = At(InjectionPoint.INVOKE(
            Descriptor(
                    "net/minecraft/network/Packet",
                    "processPacket",
                    "(Lnet/minecraft/network/INetHandler;)V"
            ),
            0
        )
    )

    insnList {
        // create new cancellable event
        createInstance(CANCELLABLE_EVENT, "()V")
        val event = astore()

        // get enum clientbound to filter out non-clientbound packets
        getStatic("net/minecraft/network/EnumPacketDirection", "CLIENTBOUND", "Lnet/minecraft/network/EnumPacketDirection;")

        // get "this" instance
        aload(0)
        // get the direction/sidedness of the packet
        getField("net/minecraft/network/NetworkManager", "field_179294_g", "Lnet/minecraft/network/EnumPacketDirection;")
        // compare clientbound enum to direction of packet
        ifClause(JumpCondition.REFS_NOT_EQUAL) {

            getStatic(TRIGGER_TYPE, "PACKET_RECEIVED", "L$TRIGGER_TYPE;")
            invokeVirtual(TRIGGER_TYPE, "triggerAll", "([Ljava/lang/Object;)V") {
                array(2, "java/lang/Object") {
                    aadd {
                        aload(2)
                    }
                    aadd {
                        load(event)
                    }
                }
            }


            load(event)
            invoke(InvokeType.VIRTUAL, CANCELLABLE_EVENT, "isCancelled", "()Z")

            ifClause(JumpCondition.FALSE) {
                methodReturn()
            }
        }
    }
}

