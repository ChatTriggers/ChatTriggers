package com.chattriggers.ctjs.engine.langs.js.objects

import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.langs.js.JSDisplay
import com.chattriggers.ctjs.engine.langs.js.JSDisplayLine
import com.chattriggers.ctjs.engine.langs.js.JSGui
import com.chattriggers.ctjs.engine.langs.js.JSRegister
import com.chattriggers.ctjs.engine.langs.js.impl.CTBuiltin
import com.chattriggers.ctjs.minecraft.libs.*
import com.chattriggers.ctjs.minecraft.libs.renderer.*
import com.chattriggers.ctjs.minecraft.objects.Book
import com.chattriggers.ctjs.minecraft.objects.KeyBind
import com.chattriggers.ctjs.minecraft.objects.Sound
import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.*
import com.chattriggers.ctjs.minecraft.wrappers.objects.Entity
import com.chattriggers.ctjs.minecraft.wrappers.objects.Particle
import com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.*
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Inventory
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.*
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt.NBTBase
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt.NBTTagCompound
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt.NBTTagList
import com.chattriggers.ctjs.triggers.*
import com.chattriggers.ctjs.utils.Config
import com.reevajs.reeva.jvmcompat.JSClassObject
import com.reevajs.reeva.runtime.objects.JSObject
import com.reevajs.reeva.utils.attrs
import net.java.games.input.Keyboard
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.relauncher.ReflectionHelper
import org.lwjgl.opengl.*

fun moduleGlobalObjectInitHelper(obj: ModuleGlobalObject) {
    // Extra libs
    defineJavaObject<ArrayList<*>>(obj)
    defineJavaObject<HashMap<*, *>>(obj)
    defineJavaObject<Keyboard>(obj)
    defineJavaObject<ReflectionHelper>(obj)

    // Libraries
    defineJavaObject<ChatLib>(obj)
    defineJavaObject<EventLib>(obj)
    defineJavaObject<Renderer>(obj)
    defineJavaObject<Shape>(obj)
    defineJavaObject<Rectangle>(obj)
    defineJavaObject<Text>(obj)
    defineJavaObject<Image>(obj)
    defineJavaObject<Tessellator>(obj)
    defineJavaObject<FileLib>(obj)
    defineJavaObject<MathLib>(obj)

    // Objects
    defineJavaObject<JSDisplay>(obj, "Display")
    defineJavaObject<JSDisplayLine>(obj, "DisplayLine")
    defineJavaObject<DisplayHandler>(obj)
    defineJavaObject<JSGui>(obj, "Gui")
    defineJavaObject<Message>(obj)
    defineJavaObject<TextComponent>(obj)
    defineJavaObject<Book>(obj)
    defineJavaObject<KeyBind>(obj)
    defineJavaObject<Image>(obj)
    defineJavaObject<Sound>(obj)
    defineJavaObject<PlayerMP>(obj)

    // Wrappers
    defineJavaObject<Client>(obj)
    defineJavaObject<Player>(obj)
    defineJavaObject<World>(obj)
    defineJavaObject<Server>(obj)
    defineJavaObject<TabList>(obj)
    defineJavaObject<Scoreboard>(obj)
    defineJavaObject<CPS>(obj)
    defineJavaObject<NBTBase>(obj)
    defineJavaObject<Block>(obj)
    defineJavaObject<BlockFace>(obj)
    defineJavaObject<BlockPos>(obj)
    defineJavaObject<BlockType>(obj)
    defineJavaObject<Sign>(obj)
    defineJavaObject<Vec3i>(obj)
    defineJavaObject<Entity>(obj)
    defineJavaObject<Particle>(obj)

    // Inventory
    defineJavaObject<Inventory>(obj)
    defineJavaObject<Item>(obj)
    defineJavaObject<NBTTagCompound>(obj)
    defineJavaObject<NBTTagList>(obj)
    defineJavaObject<Action>(obj)
    defineJavaObject<ClickAction>(obj)
    defineJavaObject<DragAction>(obj)
    defineJavaObject<DropAction>(obj)
    defineJavaObject<KeyAction>(obj)

    // Triggers
    defineJavaInstanceObject<JSRegister>(obj, "TriggerRegister")
    defineJavaObject<OnTrigger.Priority>(obj)
    defineJavaObject<PlayerInteractEvent.Action>(obj, "InteractAction")
    defineJavaObject<OnChatTrigger>(obj)
    defineJavaObject<OnCommandTrigger>(obj)
    defineJavaObject<OnRegularTrigger>(obj)
    defineJavaObject<OnRenderTrigger>(obj)
    defineJavaObject<OnSoundPlayTrigger>(obj)
    defineJavaObject<OnStepTrigger>(obj)
    defineJavaObject<OnTrigger>(obj)

    // Misc
    defineJavaInstanceObject<Config>(obj)
    defineJavaObject<Reference>(obj, "ChatTriggers")

    // GL
    defineJavaObject<GlStateManager>(obj)
    defineJavaObject<GL11>(obj)
    defineJavaObject<GL12>(obj)
    defineJavaObject<GL13>(obj)
    defineJavaObject<GL14>(obj)
    defineJavaObject<GL15>(obj)
    defineJavaObject<GL20>(obj)
    defineJavaObject<GL21>(obj)
    defineJavaObject<GL30>(obj)
    defineJavaObject<GL31>(obj)
    defineJavaObject<GL32>(obj)
    defineJavaObject<GL33>(obj)
    defineJavaObject<GL40>(obj)
    defineJavaObject<GL41>(obj)
    defineJavaObject<GL42>(obj)
    defineJavaObject<GL43>(obj)
    defineJavaObject<GL44>(obj)
    defineJavaObject<GL45>(obj)

    // Functions
    obj.defineBuiltin("setTimeout", 2, CTBuiltin.GlobalSetTimeout)
    obj.defineBuiltin("setImmediate", 2, CTBuiltin.GlobalSetImmediate)
    obj.defineBuiltin("cancel", 1, CTBuiltin.GlobalCancel)
    obj.defineBuiltin("register", 2, CTBuiltin.GlobalRegister)
    obj.defineBuiltin("easeOut", 4, CTBuiltin.GlobalEaseOut)
    obj.defineBuiltin("easeColor", 4, CTBuiltin.GlobalEaseColor)
}

private inline fun <reified T> defineJavaObject(obj: JSObject, name: String = T::class.java.simpleName) {
    obj.defineOwnProperty(
        name,
        JSClassObject.create(obj.realm, T::class.java),
        attributes = attrs { -conf; +enum; -writ }
    )
}

private inline fun <reified T> defineJavaInstanceObject(obj: JSObject, name: String = T::class.java.simpleName) {
    obj.defineOwnProperty(
        name,
        JSClassObject.create(obj.realm, T::class.java).get("INSTANCE"),
        attributes = attrs { -conf; +enum; -writ }
    )
}
