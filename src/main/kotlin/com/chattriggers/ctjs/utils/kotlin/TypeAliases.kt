package com.chattriggers.ctjs.utils.kotlin

internal typealias MCChunk = net.minecraft.world.chunk.Chunk
internal typealias MCEntity = net.minecraft.entity.Entity
internal typealias MCPotionEffect = net.minecraft.potion.PotionEffect
internal typealias MCTessellator = net.minecraft.client.renderer.Tessellator

//#if MC<=10809
internal typealias MCParticle = net.minecraft.client.particle.EntityFX
internal typealias BlockPos = net.minecraft.util.BlockPos
internal typealias RayTraceType = net.minecraft.util.MovingObjectPosition.MovingObjectType
internal typealias MathHelper = net.minecraft.util.MathHelper
internal typealias ChatPacket = net.minecraft.network.play.server.S02PacketChat
internal typealias ITextComponent = net.minecraft.util.IChatComponent
internal typealias BaseTextComponent = net.minecraft.util.ChatComponentText
internal typealias TextClickEvent = net.minecraft.event.ClickEvent
internal typealias TextHoverEvent = net.minecraft.event.HoverEvent
internal typealias ClickEventAction = net.minecraft.event.ClickEvent.Action
internal typealias HoverEventAction = net.minecraft.event.HoverEvent.Action
internal typealias TextStyle = net.minecraft.util.ChatStyle
internal typealias GameType = net.minecraft.world.WorldSettings.GameType
internal typealias WorldRenderer = net.minecraft.client.renderer.WorldRenderer
internal typealias TextComponentSerializer = net.minecraft.util.IChatComponent.Serializer
internal typealias SoundCategory = net.minecraft.client.audio.SoundCategory
//#else
//$$ internal typealias MCParticle = net.minecraft.client.particle.Particle
//$$ internal typealias BlockPos = net.minecraft.util.math.BlockPos
//$$ internal typealias RayTraceType = net.minecraft.util.math.RayTraceResult.Type
//$$ internal typealias MathHelper = net.minecraft.util.math.MathHelper
//$$ internal typealias ChatPacket = net.minecraft.network.play.server.SPacketChat
//$$ internal typealias ITextComponent = net.minecraft.util.text.ITextComponent
//$$ internal typealias BaseTextComponent = net.minecraft.util.text.TextComponentString
//$$ internal typealias TextClickEvent = net.minecraft.util.text.event.ClickEvent
//$$ internal typealias TextHoverEvent = net.minecraft.util.text.event.HoverEvent
//$$ internal typealias ClickEventAction = net.minecraft.util.text.event.ClickEvent.Action
//$$ internal typealias HoverEventAction = net.minecraft.util.text.event.HoverEvent.Action
//$$ internal typealias TextStyle = net.minecraft.util.text.Style
//$$ internal typealias GameType = net.minecraft.world.GameType
//$$ internal typealias MCClickType = net.minecraft.inventory.ClickType
//$$ internal typealias WorldRenderer = net.minecraft.client.renderer.BufferBuilder
//$$ internal typealias TextComponentSerializer = net.minecraft.util.text.ITextComponent.Serializer
//$$ internal typealias SoundCategory = net.minecraft.util.SoundCategory
//#endif