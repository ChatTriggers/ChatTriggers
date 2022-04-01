let global = this;

// Extra libs
global.ArrayList = Java.type("java.util.ArrayList");
global.HashMap = Java.type("java.util.HashMap");
global.ReflectionHelper = Java.type("net.minecraftforge.fml.relauncher.ReflectionHelper");
global.Keyboard = Java.type("org.lwjgl.input.Keyboard");

// Libraries
global.ChatLib = Java.type("com.chattriggers.ctjs.minecraft.libs.ChatLib");
global.EventLib = Java.type("com.chattriggers.ctjs.minecraft.libs.EventLib");
global.FileLib = Java.type("com.chattriggers.ctjs.minecraft.libs.FileLib");
global.MathLib = Java.type("com.chattriggers.ctjs.minecraft.libs.MathLib");
global.Tessellator = Java.type("com.chattriggers.ctjs.minecraft.libs.Tessellator");

global.Image = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Image");
global.Rectangle = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Rectangle");
global.Renderer = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Renderer");
global.Shape = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Shape");
global.Text = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Text");

// Objects
global.Book = Java.type("com.chattriggers.ctjs.minecraft.objects.Book");
global.KeyBind = Java.type("com.chattriggers.ctjs.engine.langs.js.JSKeyBind");
global.Sound = Java.type("com.chattriggers.ctjs.minecraft.objects.Sound");

global.Display = Java.type("com.chattriggers.ctjs.engine.langs.js.JSDisplay");
global.DisplayLine = Java.type("com.chattriggers.ctjs.engine.langs.js.JSDisplayLine");
global.DisplayHandler = Java.type("com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler");

global.Gui = Java.type("com.chattriggers.ctjs.engine.langs.js.JSGui");
global.GuiHandler = Java.type("com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler").INSTANCE;

global.Message = Java.type("com.chattriggers.ctjs.minecraft.objects.message.Message");
global.TextComponent = Java.type("com.chattriggers.ctjs.minecraft.objects.message.TextComponent");

// Wrappers
global.Client = Java.type("com.chattriggers.ctjs.engine.langs.js.JSClient").INSTANCE;
global.CPS = Java.type("com.chattriggers.ctjs.minecraft.wrappers.CPS");
global.Player = Java.type("com.chattriggers.ctjs.minecraft.wrappers.Player");
global.Scoreboard = Java.type("com.chattriggers.ctjs.minecraft.wrappers.Scoreboard");
global.Server = Java.type("com.chattriggers.ctjs.minecraft.wrappers.Server");
global.TabList = Java.type("com.chattriggers.ctjs.minecraft.wrappers.TabList");
global.World = Java.type("com.chattriggers.ctjs.minecraft.wrappers.World");

global.Chunk = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.Chunk");
global.Particle = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.Particle");
global.PotionEffect = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.PotionEffect");

global.Block = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.block.Block");
global.BlockFace = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockFace");
global.BlockPos = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockPos");
global.BlockType = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockType");
global.Sign = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.block.Sign");
global.Vec3i = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.block.Vec3i");

global.Entity = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.entity.Entity");
global.EntityLivingBase = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.entity.EntityLivingBase");
global.PlayerMP = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.entity.PlayerMP");

global.Inventory = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Inventory");
global.Item = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item");
global.Slot = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Slot");

global.Action = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.Action");
global.ClickAction = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.ClickAction");
global.DragAction = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.DragAction");
global.DropAction = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.DropAction");
global.KeyAction = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.KeyAction");

global.NBTBase = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt.NBTBase");
global.NBTTagCompound = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt.NBTTagCompound");
global.NBTTagList = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt.NBTTagList");

// Triggers
global.TriggerRegister = Java.type("com.chattriggers.ctjs.engine.langs.js.JSRegister").INSTANCE;

global.OnChatTrigger = Java.type("com.chattriggers.ctjs.triggers.OnChatTrigger");
global.OnCommandTrigger = Java.type("com.chattriggers.ctjs.triggers.OnCommandTrigger");
global.OnRegularTrigger = Java.type("com.chattriggers.ctjs.triggers.OnRegularTrigger");
global.OnRenderTrigger = Java.type("com.chattriggers.ctjs.triggers.OnRenderTrigger");
global.OnSoundPlayTrigger = Java.type("com.chattriggers.ctjs.triggers.OnSoundPlayTrigger");
global.OnStepTrigger = Java.type("com.chattriggers.ctjs.triggers.OnStepTrigger");
global.OnTrigger = Java.type("com.chattriggers.ctjs.triggers.OnTrigger");
global.Priority = OnTrigger.Priority;
//#if MC<=10809
global.InteractAction = Java.type("net.minecraftforge.event.entity.player.PlayerInteractEvent").Action;
//#else
//$$ global.InteractAction = Java.type("com.chattriggers.ctjs.minecraft.listeners.ClientListener").INSTANCE.PlayerInteractAction;
//#endif

// Misc
global.Config = Java.type("com.chattriggers.ctjs.utils.Config").INSTANCE;
global.ChatTriggers = Java.type("com.chattriggers.ctjs.Reference");
/*End Built in Vars */

// GL
global.GlStateManager = Java.type("net.minecraft.client.renderer.GlStateManager");
global.GL11 = Java.type("org.lwjgl.opengl.GL11");
global.GL12 = Java.type("org.lwjgl.opengl.GL12");
global.GL13 = Java.type("org.lwjgl.opengl.GL13");
global.GL14 = Java.type("org.lwjgl.opengl.GL14");
global.GL15 = Java.type("org.lwjgl.opengl.GL15");
global.GL20 = Java.type("org.lwjgl.opengl.GL20");
global.GL21 = Java.type("org.lwjgl.opengl.GL21");
global.GL30 = Java.type("org.lwjgl.opengl.GL30");
global.GL31 = Java.type("org.lwjgl.opengl.GL31");
global.GL32 = Java.type("org.lwjgl.opengl.GL32");
global.GL33 = Java.type("org.lwjgl.opengl.GL33");
global.GL40 = Java.type("org.lwjgl.opengl.GL40");
global.GL41 = Java.type("org.lwjgl.opengl.GL41");
global.GL42 = Java.type("org.lwjgl.opengl.GL42");
global.GL43 = Java.type("org.lwjgl.opengl.GL43");
global.GL44 = Java.type("org.lwjgl.opengl.GL44");
global.GL45 = Java.type("org.lwjgl.opengl.GL45");

global.cancel = function (event) {
    try {
        EventLib.cancel(event);
    } catch (err) {
        if (!event.isCancelable()) return;
        event.setCanceled(true);
    }
};

global.register = function (triggerType, methodName) {
    return TriggerRegister.register(triggerType, methodName);
};

// String prototypes
String.prototype.addFormatting = function () {
    return ChatLib.addColor(this);
};

String.prototype.addColor = String.prototype.addFormatting;

String.prototype.removeFormatting = function () {
    return ChatLib.removeFormatting(this);
};

String.prototype.replaceFormatting = function () {
    return ChatLib.replaceFormatting(this);
};

// animation
global.easeOut = function (start, finish, speed, jump) {
    if (!jump) jump = 1;

    if (Math.floor(Math.abs(finish - start) / jump) > 0) {
        return start + (finish - start) / speed;
    } else {
        return finish;
    }
};

Number.prototype.easeOut = function (to, speed, jump) {
    return easeOut(this, to, speed, jump);
};

global.easeColor = function (start, finish, speed, jump) {
    return Renderer.color(
        easeOut((start >> 16) & 0xFF, (finish >> 16) & 0xFF, speed, jump),
        easeOut((start >> 8) & 0xFF, (finish >> 8) & 0xFF, speed, jump),
        easeOut(start & 0xFF, finish & 0xFF, speed, jump),
        easeOut((start >> 24) & 0xFF, (finish >> 24) & 0xFF, speed, jump)
    );
};

Number.prototype.easeColor = function (to, speed, jump) {
    return easeColor(this, to, speed, jump);
};
