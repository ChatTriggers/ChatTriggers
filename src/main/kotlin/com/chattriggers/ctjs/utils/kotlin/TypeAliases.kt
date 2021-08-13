package com.chattriggers.ctjs.utils.kotlin

internal typealias MCChunk = net.minecraft.world.chunk.Chunk
internal typealias MCEntity = net.minecraft.entity.Entity
internal typealias MCPotionEffect = net.minecraft.potion.PotionEffect
internal typealias MCTessellator = net.minecraft.client.renderer.Tessellator
internal typealias MCEnumFacing = net.minecraft.util.EnumFacing
internal typealias MCBlock = net.minecraft.block.Block

//#if MC<=10809
internal typealias MCParticle = net.minecraft.client.particle.EntityFX
internal typealias MCNBTBase = net.minecraft.nbt.NBTBase
internal typealias MCNBTTagCompound = net.minecraft.nbt.NBTTagCompound
internal typealias MCNBTTagList = net.minecraft.nbt.NBTTagList
internal typealias MCNBTTagString = net.minecraft.nbt.NBTTagString

internal typealias MCBlockPos = net.minecraft.util.BlockPos
internal typealias MCRayTraceType = net.minecraft.util.MovingObjectPosition.MovingObjectType
internal typealias MCMathHelper = net.minecraft.util.MathHelper
internal typealias MCChatPacket = net.minecraft.network.play.server.S02PacketChat
internal typealias MCITextComponent = net.minecraft.util.IChatComponent
internal typealias MCBaseTextComponent = net.minecraft.util.ChatComponentText
internal typealias MCTextClickEvent = net.minecraft.event.ClickEvent
internal typealias MCTextHoverEvent = net.minecraft.event.HoverEvent
internal typealias MCClickEventAction = net.minecraft.event.ClickEvent.Action
internal typealias MCHoverEventAction = net.minecraft.event.HoverEvent.Action
internal typealias MCTextStyle = net.minecraft.util.ChatStyle
internal typealias MCGameType = net.minecraft.world.WorldSettings.GameType
internal typealias MCWorldRenderer = net.minecraft.client.renderer.WorldRenderer
internal typealias MCTextComponentSerializer = net.minecraft.util.IChatComponent.Serializer
internal typealias MCSoundCategory = net.minecraft.client.audio.SoundCategory
//#else
//$$ internal typealias MCParticle = net.minecraft.client.particle.Particle
//$$ internal typealias MCBlockPos = net.minecraft.util.math.BlockPos
//$$ internal typealias MCRayTraceType = net.minecraft.util.math.RayTraceResult.Type
//$$ internal typealias MCMathHelper = net.minecraft.util.math.MathHelper
//$$ internal typealias MCChatPacket = net.minecraft.network.play.server.SPacketChat
//$$ internal typealias MCITextComponent = net.minecraft.util.text.ITextComponent
//$$ internal typealias MCBaseTextComponent = net.minecraft.util.text.TextComponentString
//$$ internal typealias MCTextClickEvent = net.minecraft.util.text.event.ClickEvent
//$$ internal typealias MCTextHoverEvent = net.minecraft.util.text.event.HoverEvent
//$$ internal typealias MCClickEventAction = net.minecraft.util.text.event.ClickEvent.Action
//$$ internal typealias MCHoverEventAction = net.minecraft.util.text.event.HoverEvent.Action
//$$ internal typealias MCTextStyle = net.minecraft.util.text.Style
//$$ internal typealias MCGameType = net.minecraft.world.GameType
//$$ internal typealias MCClickType = net.minecraft.inventory.ClickType
//$$ internal typealias MCWorldRenderer = net.minecraft.client.renderer.BufferBuilder
//$$ internal typealias MCTextComponentSerializer = net.minecraft.util.text.ITextComponent.Serializer
//$$ internal typealias MCSoundCategory = net.minecraft.util.SoundCategory
//#endif
