//Java libs
var ArrayList = Java.type("java.util.ArrayList");

//Triggers
var TriggerRegister = Java.type("com.chattriggers.jsct.triggers.TriggerRegister");
var TriggerResult = Java.type("com.chattriggers.jsct.triggers.Trigger.TriggerResult");

//Events
var ChatLib = Java.type("com.chattriggers.jsct.libs.ChatLib");

//Objects
var Display = Java.type("com.chattriggers.jsct.objects.Display");
var DisplayHandler = Java.type("com.chattriggers.jsct.objects.DisplayHandler");
var Message = Java.type("com.chattriggers.jsct.utils.Message");
var Book = Java.type("com.chattriggers.jsct.objects.Book");

function updateProvidedLibs() {

}

function clickable(text, action, value, hover) {
    var ChatStyle = Java.type("net.minecraft.util.ChatStyle");
    var ChatComponentText = Java.type("net.minecraft.util.ChatComponentText");
    var ClickEvent = Java.type("net.minecraft.event.ClickEvent");
    var HoverEvent = Java.type("net.minecraft.event.HoverEvent");

    var chatComponent = new ChatComponentText(ChatLib.addColor(text));

    chatComponent.setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(
        ClickEvent.Action.getValueByCanonicalName(action), value
    )));

    if (hover != null && hover != "") {
        chatComponent.getChatStyle().setChatHoverEvent(new HoverEvent(
            HoverEvent.Action.SHOW_TEXT, new ChatComponentText(ChatLib.addColor(hover))
        ));
    }

    return chatComponent;
}

module.exports = ArrayList;
module.exports = TriggerRegister;
module.exports = TriggerResult;
module.exports = ChatLib;
module.exports = Display;