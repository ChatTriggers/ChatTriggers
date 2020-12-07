package com.chattriggers.ctjs.launch.plugin

import me.falsehonesty.asmhelper.dsl.At
import me.falsehonesty.asmhelper.dsl.InjectionPoint
import me.falsehonesty.asmhelper.dsl.inject
import me.falsehonesty.asmhelper.dsl.instructions.*

fun injectNetworkManager() = inject {
    className = "net/minecraft/network/NetworkManager"
    methodName = "dispatchPacket"
    methodDesc = "(Lnet/minecraft/network/Packet;[Lio/netty/util/concurrent/GenericFutureListener;)V"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                "io/netty/channel/Channel",
                "writeAndFlush",
                "(Ljava/lang/Object;)Lio/netty/channel/ChannelFuture;"
            ),
            ordinal = 0
        )
    )

    methodMaps = mapOf("func_150732_b" to "dispatchPacket")

    insnList {
        createInstance(CANCELLABLE_EVENT, "()V")
        val event = astore()

        invokeVirtual("net/minecraft/network/NetworkManager", "getDirection", "()Lnet/minecraft/network/EnumPacketDirection;") {
            aload(0)
        }
        getStatic("net/minecraft/network/EnumPacketDirection", "CLIENTBOUND", "Lnet/minecraft/network/EnumPacketDirection;")
        ifElseClause(JumpCondition.REFS_EQUAL) {
            ifCode {
                getStatic(TRIGGER_TYPE, "PACKET_SENT", "L$TRIGGER_TYPE;")
            }
            elseCode {
                getStatic(TRIGGER_TYPE, "PACKET_RECEIVED", "L$TRIGGER_TYPE;")
            }
        }

        invokeVirtual(TRIGGER_TYPE, "triggerAll", "([Ljava/lang/Object;)V") {
            array(2, "java/lang/Object") {
                aadd {
                    aload(1)
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

fun injectNetworkManager4() = inject {
    className = "net/minecraft/network/NetworkManager$4"
    methodName = "run"
    methodDesc = "()V"

    at = At(
        InjectionPoint.INVOKE(
            Descriptor(
                "io/netty/channel/Channel",
                "writeAndFlush",
                "(Ljava/lang/Object;)Lio/netty/channel/ChannelFuture;"
            ),
            ordinal = 0
        )
    )

    insnList {
        createInstance(CANCELLABLE_EVENT, "()V")
        val event = astore()

        invokeVirtual("net/minecraft/network/NetworkManager", "getDirection", "()Lnet/minecraft/network/EnumPacketDirection;") {
            aload(0)
            getField("net/minecraft/network/NetworkManager$4", "this$0", "Lnet/minecraft/network/NetworkManager;")
        }
        getStatic("net/minecraft/network/EnumPacketDirection", "CLIENTBOUND", "Lnet/minecraft/network/EnumPacketDirection;")
        ifElseClause(JumpCondition.REFS_EQUAL) {
            ifCode {
                getStatic(TRIGGER_TYPE, "PACKET_SENT", "L$TRIGGER_TYPE;")
            }
            elseCode {
                getStatic(TRIGGER_TYPE, "PACKET_RECEIVED", "L$TRIGGER_TYPE;")
            }
        }

        invokeVirtual(TRIGGER_TYPE, "triggerAll", "([Ljava/lang/Object;)V") {
            array(2, "java/lang/Object") {
                aadd {
                    aload(0)
                    getField("net/minecraft/network/NetworkManager$4", "val\$inPacket", "Lnet/minecraft/network/Packet;")
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