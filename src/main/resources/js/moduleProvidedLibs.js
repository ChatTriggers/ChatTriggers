(function(global) {
    // Helpers to load classes which throws if the class isn't found. This
    // helps spot refactoring bugs early!

    const Class = Java.type('java.lang.Class');

    const getClassName = path => path.substring(path.lastIndexOf('.') + 1)

    function getClass(path) {
        const clazz = Java.type(path);
        if (clazz.class instanceof Class)
            return clazz;
        throw new Error(`moduleProvidedLibs: Could not load class "${path}"`);
    }

    function loadClass(path, className = getClassName(path)) {
        global[className] = getClass(path);
    }

    function loadInstance(path, className = getClassName(path)) {
        global[className] = getClass(path).INSTANCE;
    }

    // Extra libs
    loadClass("java.util.ArrayList");
    loadClass("java.util.HashMap");
    loadClass("net.minecraftforge.fml.relauncher.ReflectionHelper");
    loadClass("org.lwjgl.input.Keyboard");

    // Libraries
    loadClass("com.chattriggers.ctjs.minecraft.libs.ChatLib");
    loadClass("com.chattriggers.ctjs.minecraft.libs.EventLib");
    loadClass("com.chattriggers.ctjs.minecraft.libs.FileLib");
    loadClass("com.chattriggers.ctjs.minecraft.libs.MathLib");
    loadClass("com.chattriggers.ctjs.minecraft.libs.Tessellator");

    loadClass("com.chattriggers.ctjs.minecraft.libs.renderer.Image");
    loadClass("com.chattriggers.ctjs.minecraft.libs.renderer.Rectangle");
    loadClass("com.chattriggers.ctjs.minecraft.libs.renderer.Renderer");
    loadClass("com.chattriggers.ctjs.minecraft.libs.renderer.Shape");
    loadClass("com.chattriggers.ctjs.minecraft.libs.renderer.Text");

    // Objects
    loadClass("com.chattriggers.ctjs.minecraft.objects.Book");
    loadClass("com.chattriggers.ctjs.engine.langs.js.JSKeyBind", "KeyBind");
    loadClass("com.chattriggers.ctjs.minecraft.objects.Sound");

    loadClass("com.chattriggers.ctjs.engine.langs.js.JSDisplay", "Display");
    loadClass("com.chattriggers.ctjs.engine.langs.js.JSDisplayLine", "DisplayLine");
    loadClass("com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler");

    loadClass("com.chattriggers.ctjs.engine.langs.js.JSGui", "Gui");
    loadInstance("com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler");

    loadClass("com.chattriggers.ctjs.minecraft.objects.message.Message");
    loadClass("com.chattriggers.ctjs.minecraft.objects.message.TextComponent");

    // Wrappers
    loadInstance("com.chattriggers.ctjs.engine.langs.js.JSClient", "Client");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.CPS");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.Player");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.Scoreboard");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.Server");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.TabList");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.World");

    loadClass("com.chattriggers.ctjs.minecraft.wrappers.world.Chunk");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.entity.Particle");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.world.PotionEffect");

    loadClass("com.chattriggers.ctjs.minecraft.wrappers.world.block.Block");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockFace");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockPos");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockType");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.world.block.Sign");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.utils.Vec3i");

    loadClass("com.chattriggers.ctjs.minecraft.wrappers.entity.Entity");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.entity.EntityLivingBase");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.entity.PlayerMP");

    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.Inventory");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.Item");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.Slot");

    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.action.Action");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.action.ClickAction");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.action.DragAction");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.action.DropAction");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.action.KeyAction");

    loadInstance("com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt.NBT");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt.NBTBase");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt.NBTTagCompound");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt.NBTTagList");

    // Triggers
    loadInstance("com.chattriggers.ctjs.engine.langs.js.JSRegister", "TriggerRegister");

    loadClass("com.chattriggers.ctjs.triggers.ChatTrigger", "OnChatTrigger");
    loadClass("com.chattriggers.ctjs.triggers.CommandTrigger", "OnCommandTrigger");
    loadClass("com.chattriggers.ctjs.triggers.RegularTrigger", "OnRegularTrigger");
    loadClass("com.chattriggers.ctjs.triggers.EventTrigger", "OnRenderTrigger");
    loadClass("com.chattriggers.ctjs.triggers.SoundPlayTrigger", "OnSoundPlayTrigger");
    loadClass("com.chattriggers.ctjs.triggers.StepTrigger", "OnStepTrigger");
    loadClass("com.chattriggers.ctjs.triggers.Trigger", "OnTrigger");
    global.Priority = OnTrigger.Priority;
    //#if MC<=10809
    global.InteractAction = getClass("net.minecraftforge.event.entity.player.PlayerInteractEvent").Action;
    //#else
    //$$ global.InteractAction = Java.type("com.chattriggers.ctjs.minecraft.listeners.ClientListener").INSTANCE.PlayerInteractAction;
    //#endif

    // Misc
    loadInstance("com.chattriggers.ctjs.utils.Config");
    loadClass("com.chattriggers.ctjs.Reference", "ChatTriggers");
    /*End Built in Vars */

    // GL
    loadClass("net.minecraft.client.renderer.GlStateManager");
    loadClass("org.lwjgl.opengl.GL11");
    loadClass("org.lwjgl.opengl.GL12");
    loadClass("org.lwjgl.opengl.GL13");
    loadClass("org.lwjgl.opengl.GL14");
    loadClass("org.lwjgl.opengl.GL15");
    loadClass("org.lwjgl.opengl.GL20");
    loadClass("org.lwjgl.opengl.GL21");
    loadClass("org.lwjgl.opengl.GL30");
    loadClass("org.lwjgl.opengl.GL31");
    loadClass("org.lwjgl.opengl.GL32");
    loadClass("org.lwjgl.opengl.GL33");
    loadClass("org.lwjgl.opengl.GL40");
    loadClass("org.lwjgl.opengl.GL41");
    loadClass("org.lwjgl.opengl.GL42");
    loadClass("org.lwjgl.opengl.GL43");
    loadClass("org.lwjgl.opengl.GL44");
    loadClass("org.lwjgl.opengl.GL45");

    global.cancel = event => {
        try {
            EventLib.cancel(event);
        } catch (err) {
            if (event.isCancelable())
                event.setCanceled(true);
        }
    };

    global.register = (type, method) => TriggerRegister.register(type, method);

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
    global.easeOut = (start, finish, speed, jump = 1) => {
        if (Math.floor(Math.abs(finish - start) / jump) > 0)
            return start + (finish - start) / speed;
        return finish;
    };

    Number.prototype.easeOut = function (to, speed, jump) {
        return easeOut(this, to, speed, jump);
    };

    global.easeColor = (start, finish, speed, jump) => Renderer.color(
        easeOut((start >> 16) & 0xFF, (finish >> 16) & 0xFF, speed, jump),
        easeOut((start >> 8) & 0xFF, (finish >> 8) & 0xFF, speed, jump),
        easeOut(start & 0xFF, finish & 0xFF, speed, jump),
        easeOut((start >> 24) & 0xFF, (finish >> 24) & 0xFF, speed, jump)
    );

    Number.prototype.easeColor = function (to, speed, jump) {
        return easeColor(this, to, speed, jump);
    };

})(this);
