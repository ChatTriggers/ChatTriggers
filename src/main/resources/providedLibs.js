/*global
   Java
*/

// Extra libs
var ArrayList = Java.type("java.util.ArrayList");
var HashMap = Java.type("java.util.HashMap");
var Thread = Java.type("java.lang.Thread");
var Keyboard = Java.type("org.lwjgl.input.Keyboard");
var ReflectionHelper = Java.type("net.minecraftforge.fml.relauncher.ReflectionHelper");

// Triggers
var TriggerRegister = Java.type("com.chattriggers.ctjs.engine.langs.js.JSRegister").INSTANCE;
var TriggerResult = Java.type("com.chattriggers.ctjs.triggers.OnTrigger.TriggerResult");
var Priority = Java.type("com.chattriggers.ctjs.triggers.OnTrigger.Priority");
var InteractAction = Java.type("net.minecraftforge.event.entity.player.PlayerInteractEvent.Action");

// Libraries
var ChatLib = Java.type("com.chattriggers.ctjs.minecraft.libs.ChatLib");
var EventLib = Java.type("com.chattriggers.ctjs.minecraft.libs.EventLib");

var Renderer = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Renderer");
var Shape = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Shape");
var Rectangle = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Rectangle");
var Text = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Text");
var Image = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Image");

var Tessellator = Java.type("com.chattriggers.ctjs.minecraft.libs.Tessellator");
var FileLib = Java.type("com.chattriggers.ctjs.minecraft.libs.FileLib");
var MathLib = Java.type("com.chattriggers.ctjs.minecraft.libs.MathLib");
var XMLHttpRequest = Java.type("com.chattriggers.ctjs.engine.langs.js.JSXMLHttpRequest");

// Objects
var Display = Java.type("com.chattriggers.ctjs.engine.langs.js.JSDisplay");
var DisplayLine = Java.type("com.chattriggers.ctjs.engine.langs.js.JSDisplayLine");
var DisplayHandler = Java.type("com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler");
var Gui = Java.type("com.chattriggers.ctjs.engine.langs.js.JSGui");
var Message = Java.type("com.chattriggers.ctjs.minecraft.objects.message.Message");
var TextComponent = Java.type("com.chattriggers.ctjs.minecraft.objects.message.TextComponent");
var Book = Java.type("com.chattriggers.ctjs.minecraft.objects.Book");
var KeyBind = Java.type("com.chattriggers.ctjs.minecraft.objects.KeyBind");
var Image = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Image");
var Sound = Java.type("com.chattriggers.ctjs.minecraft.objects.Sound");
var PlayerMP = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP");

// Wrappers
var Client = Java.type("com.chattriggers.ctjs.minecraft.wrappers.Client");
var Player = Java.type("com.chattriggers.ctjs.minecraft.wrappers.Player");
var World = Java.type("com.chattriggers.ctjs.minecraft.wrappers.World");
var Server = Java.type("com.chattriggers.ctjs.minecraft.wrappers.Server");
var Inventory = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Inventory");
var TabList = Java.type("com.chattriggers.ctjs.minecraft.wrappers.TabList");
var Scoreboard = Java.type("com.chattriggers.ctjs.minecraft.wrappers.Scoreboard");
var CPS = Java.type("com.chattriggers.ctjs.minecraft.wrappers.CPS");
var Item = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item");
var Block = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.block.Block");
var Sign = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.block.Sign");
var Entity = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.Entity");
var Action = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.Action");
var ClickAction = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.ClickAction");
var DragAction = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.DragAction");
var KeyAction = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.KeyAction");

// Triggers
var OnChatTrigger = Java.type("com.chattriggers.ctjs.triggers.OnChatTrigger");
var OnCommandTrigger = Java.type("com.chattriggers.ctjs.triggers.OnCommandTrigger");
var OnRegularTrigger = Java.type("com.chattriggers.ctjs.triggers.OnRegularTrigger");
var OnRenderTrigger = Java.type("com.chattriggers.ctjs.triggers.OnRenderTrigger");
var OnSoundPlayTrigger = Java.type("com.chattriggers.ctjs.triggers.OnSoundPlayTrigger");
var OnStepTrigger = Java.type("com.chattriggers.ctjs.triggers.OnStepTrigger");
var OnTrigger = Java.type("com.chattriggers.ctjs.triggers.OnTrigger");

// Misc
var Console = Java.type("com.chattriggers.ctjs.engine.langs.js.JSLoader").INSTANCE.getConsole();
var Config = Java.type("com.chattriggers.ctjs.utils.config.Config").INSTANCE;
var ChatTriggers = Java.type("com.chattriggers.ctjs.Reference").INSTANCE;

/*End Built in Vars */



// simplified methods
function print(toPrint) {
    Console.out.println(toPrint);
}

function cancel(event) {
    try {
        EventLib.cancel(event);
    } catch(err) {
        if (!event.isCancelable()) return;
        event.setCanceled(true);
    }
}

function register(triggerType, methodName) {
    return TriggerRegister.register(triggerType, methodName);
}

// animation
function easeOut(start, finish, speed, jump) {
    if (!jump) jump = 1;

    if (Math.floor(Math.abs(finish - start) / jump) > 0) {
        return start + (finish - start) / speed;
    } else {
        return finish;
    }
}

Number.prototype.easeOut = function(to, speed, jump) {
    if (!jump) jump = 1;

    if (Math.floor(Math.abs(to - this) / jump) > 0) {
        this = this + (to - this) / speed;
    } else {
        this = to
    }
};

function easeColor(start, finish, speed, jump) {
    return Renderer.color(
        easeOut((start >> 16) & 0xFF, (finish >> 16) & 0xFF, speed, jump),
        easeOut((start >> 8) & 0xFF, (finish >> 8) & 0xFF, speed, jump),
        easeOut(start & 0xFF, finish & 0xFF, speed, jump),
        easeOut((start >> 24) & 0xFF, (finish >> 24) & 0xFF, speed, jump)
    );
}

Number.prototype.easeColor = function(start, finish, speed, jump) {
    this = Renderer.color(
        easeOut((start >> 16) & 0xFF, (finish >> 16) & 0xFF, speed, jump),
        easeOut((start >> 8) & 0xFF, (finish >> 8) & 0xFF, speed, jump),
        easeOut(start & 0xFF, finish & 0xFF, speed, jump),
        easeOut((start >> 24) & 0xFF, (finish >> 24) & 0xFF, speed, jump)
    );
};

function setTimeout(func, delay) {
    new Thread(function() {
        Thread.sleep(delay);
        func();
    }).start();
}
