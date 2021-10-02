package com.chattriggers.ctjs.utils.kotlin

// World
internal typealias MCChunk = net.minecraft.world.chunk.Chunk
internal typealias MCTessellator = net.minecraft.client.renderer.Tessellator
internal typealias MCDirection = net.minecraft.util.EnumFacing
internal typealias MCBlock = net.minecraft.block.Block
internal typealias MCBlockPos = net.minecraft.util.BlockPos
internal typealias MCWorldRenderer = net.minecraft.client.renderer.WorldRenderer
internal typealias MCSoundCategory = net.minecraft.client.audio.SoundCategory

// Entity
internal typealias MCEntity = net.minecraft.entity.Entity
internal typealias MCParticle = net.minecraft.client.particle.EntityFX

// Chat
internal typealias MCChatPacket = net.minecraft.network.play.server.S02PacketChat
internal typealias MCITextComponent = net.minecraft.util.IChatComponent
internal typealias MCStringTextComponent = net.minecraft.util.ChatComponentText
internal typealias MCTextStyle = net.minecraft.util.ChatStyle
internal typealias MCTextComponentSerializer = net.minecraft.util.IChatComponent.Serializer

// Misc
internal typealias MCMathHelper = net.minecraft.util.MathHelper

// NBT
//#if MC==10809
internal typealias MCNBTBase = net.minecraft.nbt.NBTBase
internal typealias MCNBTTagCompound = net.minecraft.nbt.NBTTagCompound
internal typealias MCNBTTagList = net.minecraft.nbt.NBTTagList
internal typealias MCNBTTagString = net.minecraft.nbt.NBTTagString
//#endif
