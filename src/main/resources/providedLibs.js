//Java libs
var ArrayList = Java.type("java.util.ArrayList");

//Triggers
var TriggerRegister = Java.type("com.chattriggers.jsct.triggers.TriggerRegister");
var TriggerResult = Java.type("com.chattriggers.jsct.triggers.Trigger.TriggerResult");

//Events
var ChatLib = Java.type("com.chattriggers.jsct.libs.ChatLib");
var WorldLib = Java.type("com.chattriggers.jsct.libs.WorldLib");
var RenderLib = Java.type("com.chattriggers.jsct.libs.RenderLib");

//Objects
var Display = Java.type("com.chattriggers.jsct.objects.Display");
var DisplayHandler = Java.type("com.chattriggers.jsct.objects.DisplayHandler");
var Message = Java.type("com.chattriggers.jsct.utils.Message");
var Book = Java.type("com.chattriggers.jsct.objects.Book");

/*Built in Vars */
var MinecraftVars = Java.type("com.chattriggers.jsct.libs.MinecraftVars");

//Constant
var playerName = MinecraftVars.getPlayerName();
var uuid = MinecraftVars.getPlayerUUID();

//Change
var hp = MinecraftVars.getPlayerHP();
var hunger = MinecraftVars.getPlayerHunger();
var saturation = MinecraftVars.getPlayerSaturation();
var xpLevel = MinecraftVars.getXPLevel();
var xpProgress = MinecraftVars.getXPProgress();
var inChat = MinecraftVars.isInChat();
var inTab = MinecraftVars.isInTab();
var server = MinecraftVars.getServerName();
var ping = MinecraftVars.getPing();
var playerPosX = MinecraftVars.getPlayerPosX();
var playerPosY = MinecraftVars.getPlayerPosY();
var playerPosZ = MinecraftVars.getPlayerPosZ();
var playerPitch = MinecraftVars.getPlayerPitch();
var playerYaw = MinecraftVars.getPlayerYaw();
var playerFPS = MinecraftVars.getPlayerFPS();
var playerFacing = MinecraftVars.getPlayerFacing();

/*End Built in Vars */

function updateProvidedLibs() {
    hp = MinecraftVars.getPlayerHP();
    hunger = MinecraftVars.getPlayerHunger();
    saturation = MinecraftVars.getPlayerSaturation();
    xpLevel = MinecraftVars.getXPLevel();
    xpProgress = MinecraftVars.getXPProgress();
    inChat = MinecraftVars.isInChat();
    inTab = MinecraftVars.isInTab();
    server = MinecraftVars.getServerName();
    ping = MinecraftVars.getPing();
    posX = MinecraftVars.getPlayerPosX();
    posY = MinecraftVars.getPlayerPosY();
    posZ = MinecraftVars.getPlayerPosZ();
    cameraPitch = MinecraftVars.getPlayerPitch();
    cameraYaw = MinecraftVars.getPlayerYaw();
    fps = MinecraftVars.getPlayerFPS();
    facing = MinecraftVars.getPlayerFacing();
}