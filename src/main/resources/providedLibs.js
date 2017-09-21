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

module.exports = ArrayList;
module.exports = TriggerRegister;
module.exports = TriggerResult;
module.exports = ChatLib;
module.exports = Display;