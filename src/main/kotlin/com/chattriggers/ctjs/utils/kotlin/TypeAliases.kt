package com.chattriggers.ctjs.utils.kotlin

//#if MC<=11202
internal typealias MCChunk = net.minecraft.world.chunk.Chunk
internal typealias MCEntity = net.minecraft.entity.Entity
internal typealias MCEntityLivingBase = net.minecraft.entity.EntityLivingBase
internal typealias MCPotionEffect = net.minecraft.potion.PotionEffect
internal typealias MCEnumFacing = net.minecraft.util.EnumFacing
internal typealias MCBlock = net.minecraft.block.Block
internal typealias MCScore = net.minecraft.scoreboard.Score
internal typealias MCTileEntity = net.minecraft.tileentity.TileEntity
internal typealias MCScoreboard = net.minecraft.scoreboard.Scoreboard
internal typealias MCItem = net.minecraft.item.Item
internal typealias MCSlot = net.minecraft.inventory.Slot

internal typealias MCParticle = net.minecraft.client.particle.EntityFX
internal typealias MCNBTBase = net.minecraft.nbt.NBTBase
internal typealias MCNBTTagCompound = net.minecraft.nbt.NBTTagCompound
internal typealias MCNBTTagList = net.minecraft.nbt.NBTTagList
internal typealias MCNBTTagString = net.minecraft.nbt.NBTTagString

internal typealias MCBlockPos = net.minecraft.util.BlockPos
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
//$$ // internal typealias MCChunk = net.minecraft.world.chunk.Chunk
//$$ internal typealias MCEntity = net.minecraft.world.entity.Entity
//$$ internal typealias MCEntityLivingBase = net.minecraft.world.entity.LivingEntity
//$$ internal typealias MCPotionEffect = net.minecraft.world.effect.MobEffectInstance
//$$ // internal typealias MCEnumFacing = net.minecraft.util.EnumFacing
//$$ internal typealias MCBlock = net.minecraft.world.level.block.Block
//$$ internal typealias MCScore = net.minecraft.world.scores.Score
//$$ internal typealias MCTileEntity = net.minecraft.world.level.block.entity.BlockEntity
//$$ internal typealias MCScoreboard = net.minecraft.world.scores.Scoreboard
//$$ internal typealias MCItem = net.minecraft.world.item.Item
//$$ internal typealias MCSlot = net.minecraft.world.inventory.Slot
//$$ internal typealias MCParticle = net.minecraft.client.particle.Particle
//$$ internal typealias MCNBTBase = net.minecraft.nbt.Tag
//$$ internal typealias MCNBTTagCompound = net.minecraft.nbt.CompoundTag
//$$ internal typealias MCNBTTagList = net.minecraft.nbt.ListTag
//$$ internal typealias MCNBTTagString = net.minecraft.nbt.StringTag
//$$ internal typealias MCBlockPos = net.minecraft.core.BlockPos
//$$ // internal typealias MCRayTraceType = net.minecraft.util.MovingObjectPosition.MovingObjectType
//$$ // internal typealias MCMathHelper = net.minecraft.util.MathHelper
//$$ internal typealias MCChatPacket = net.minecraft.network.protocol.game.ServerboundChatPacket
//$$ internal typealias MCITextComponent = net.minecraft.network.chat.Component
//$$ internal typealias MCBaseTextComponent = net.minecraft.network.chat.TextComponent
//$$ internal typealias MCTextClickEvent = net.minecraft.network.chat.ClickEvent
//$$ internal typealias MCTextHoverEvent = net.minecraft.network.chat.HoverEvent
//$$ internal typealias MCClickEventAction = net.minecraft.network.chat.ClickEvent.Action
//$$ internal typealias MCHoverEventAction<T> = net.minecraft.network.chat.HoverEvent.Action<T>
//$$ internal typealias MCTextStyle = net.minecraft.network.chat.Style
//$$ internal typealias MCGameType = net.minecraft.world.level.GameType
//$$ // internal typealias MCWorldRenderer = net.minecraft.client.renderer.WorldRenderer
//$$ internal typealias MCTextComponentSerializer = net.minecraft.network.chat.Component.Serializer
//$$ internal typealias MCSoundCategory = net.minecraft.sounds.SoundSource
//#endif
