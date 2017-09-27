// Java libs
var ArrayList = Java.type("java.util.ArrayList");

// Triggers
var TriggerRegister = Java.type("com.chattriggers.ctjs.triggers.TriggerRegister");
var TriggerResult = Java.type("com.chattriggers.ctjs.triggers.OnTrigger.TriggerResult");

// Events
var ChatLib = Java.type("com.chattriggers.ctjs.libs.ChatLib");
var WorldLib = Java.type("com.chattriggers.ctjs.libs.WorldLib");
var RenderLib = Java.type("com.chattriggers.ctjs.libs.RenderLib");

// Objects
var Display = Java.type("com.chattriggers.ctjs.objects.Display");
var DisplayHandler = Java.type("com.chattriggers.ctjs.objects.DisplayHandler");
var Message = Java.type("com.chattriggers.ctjs.utils.Message");
var Book = Java.type("com.chattriggers.ctjs.objects.Book");

/*Built in Vars */
var MinecraftVars = Java.type("com.chattriggers.ctjs.libs.MinecraftVars");

// Constant
var playerName = MinecraftVars.getPlayerName();
var uuid = MinecraftVars.getPlayerUUID();

// Update every tick
var hp = MinecraftVars.getPlayerHP();
var hunger = MinecraftVars.getPlayerHunger();
var saturation = MinecraftVars.getPlayerSaturation();
var xpLevel = MinecraftVars.getXPLevel();
var xpProgress = MinecraftVars.getXPProgress();
var inChat = MinecraftVars.isInChat();
var inTab = MinecraftVars.isInTab();
var isSprinting = MinecraftVars.isSprinting();
var isSneaking = MinecraftVars.isSneaking();
var ping = MinecraftVars.getPing();
var posX = MinecraftVars.getPlayerPosX();
var posY = MinecraftVars.getPlayerPosY();
var posZ = MinecraftVars.getPlayerPosZ();
var cameraPitch = MinecraftVars.getPlayerPitch();
var cameraYaw = MinecraftVars.getPlayerYaw();
var fps = MinecraftVars.getPlayerFPS();
var facing = MinecraftVars.getPlayerFacing();
var leftArrow = MinecraftVars.keyLeftArrow.isKeyDown();
var rightArrow = MinecraftVars.keyRightArrow.isKeyDown();
var upArrow = MinecraftVars.keyUpArrow.isKeyDown();
var downArrow = MinecraftVars.keyDownArrow.isKeyDown();

// Update every world load
var serverIP = MinecraftVars.getServerIP();
var serverMOTD = MinecraftVars.getServerMOTD();
var server = MinecraftVars.getServerName();

/*End Built in Vars */

function updateProvidedLibsTick() {
    hp = MinecraftVars.getPlayerHP();
    hunger = MinecraftVars.getPlayerHunger();
    saturation = MinecraftVars.getPlayerSaturation();
    xpLevel = MinecraftVars.getXPLevel();
    xpProgress = MinecraftVars.getXPProgress();
    inChat = MinecraftVars.isInChat();
    inTab = MinecraftVars.isInTab();
    isSprinting = MinecraftVars.isSprinting();
    isSneaking = MinecraftVars.isSneaking();
    ping = MinecraftVars.getPing();
    posX = MinecraftVars.getPlayerPosX();
    posY = MinecraftVars.getPlayerPosY();
    posZ = MinecraftVars.getPlayerPosZ();
    cameraPitch = MinecraftVars.getPlayerPitch();
    cameraYaw = MinecraftVars.getPlayerYaw();
    fps = MinecraftVars.getPlayerFPS();
    facing = MinecraftVars.getPlayerFacing();
    leftArrow = MinecraftVars.keyLeftArrow.isKeyDown();
    rightArrow = MinecraftVars.keyRightArrow.isKeyDown();
    upArrow = MinecraftVars.keyUpArrow.isKeyDown();
    downArrow = MinecraftVars.keyDownArrow.isKeyDown();
}

function updateProvidedLibsWorld() {
    serverIP = MinecraftVars.getServerIP();
    serverMOTD = MinecraftVars.getServerMOTD();
    server = MinecraftVars.getServerName();
}