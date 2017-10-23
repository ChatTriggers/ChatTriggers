// Java libs
var ArrayList = Java.type("java.util.ArrayList");
var HashMap = Java.type("java.util.HashMap");
var Thread = Java.type("java.lang.Thread");

// Triggers
var TriggerRegister = Java.type("com.chattriggers.ctjs.triggers.TriggerRegister");
var TriggerResult = Java.type("com.chattriggers.ctjs.triggers.OnTrigger.TriggerResult");
var Priority = Java.type("com.chattriggers.ctjs.triggers.Priority");

// Events
var ChatLib = Java.type("com.chattriggers.ctjs.libs.ChatLib");
var WorldLib = Java.type("com.chattriggers.ctjs.libs.WorldLib");
var RenderLib = Java.type("com.chattriggers.ctjs.libs.RenderLib");
var FileLib = Java.type("com.chattriggers.ctjs.libs.FileLib");

// Objects
var Display = Java.type("com.chattriggers.ctjs.objects.Display");
var DisplayHandler = Java.type("com.chattriggers.ctjs.objects.DisplayHandler");
var Gui = Java.type("com.chattriggers.ctjs.objects.Gui");
var Message = Java.type("com.chattriggers.ctjs.utils.Message");
var Book = Java.type("com.chattriggers.ctjs.objects.Book");
var KeyBind = Java.type("com.chattriggers.ctjs.objects.KeyBind");
var Keyboard = Java.type("org.lwjgl.input.Keyboard");
var XMLHttpRequest = Java.type("com.chattriggers.ctjs.objects.XMLHttpRequest");
var Console = Java.type("com.chattriggers.ctjs.utils.console.Console");

/*Built in Vars */
var MinecraftVars = Java.type("com.chattriggers.ctjs.libs.MinecraftVars");

// Constant
var playerName = MinecraftVars.getPlayerName();
var uuid = MinecraftVars.getPlayerUUID();

// Update every tick
var hp = MinecraftVars.getPlayerHP();
var hunger = MinecraftVars.getPlayerHunger();
var saturation = MinecraftVars.getPlayerSaturation();
var armorPoints = MinecraftVars.getPlayerArmorPoints();
var airLevel = MinecraftVars.getPlayerAirLevel()
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
var motionX = MinecraftVars.getPlayerMotionX();
var motionY = MinecraftVars.getPlayerMotionY();
var motionZ = MinecraftVars.getPlayerMotionZ();
var biome = MinecraftVars.getPlayerBiome();
var lightLevel = MinecraftVars.getPlayerLightLevel();
var cameraPitch = MinecraftVars.getPlayerPitch();
var cameraYaw = MinecraftVars.getPlayerYaw();
var fps = MinecraftVars.getPlayerFPS();
var facing = MinecraftVars.getPlayerFacing();
var leftArrow = MinecraftVars.isLeftArrowDown();
var rightArrow = MinecraftVars.isRightArrowDown();
var upArrow = MinecraftVars.isUpArrowDown();
var downArrow = MinecraftVars.isDownArrowDown();
var tabbedIn = MinecraftVars.isUserTabbedIn();
var potEffects = MinecraftVars.getActivePotionEffects();

// Update every world load
var serverIP = MinecraftVars.getServerIP();
var serverMOTD = MinecraftVars.getServerMOTD();
var server = MinecraftVars.getServerName();

/*End Built in Vars */

function updateProvidedLibsTick() {
    hp = MinecraftVars.getPlayerHP();
    hunger = MinecraftVars.getPlayerHunger();
    saturation = MinecraftVars.getPlayerSaturation();
    armorPoints = MinecraftVars.getPlayerArmorPoints();
    airLevel = MinecraftVars.getPlayerAirLevel();
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
    motionX = MinecraftVars.getPlayerMotionX();
    motionY = MinecraftVars.getPlayerMotionY();
    motionZ = MinecraftVars.getPlayerMotionZ();
    biome = MinecraftVars.getPlayerBiome();
    lightLevel = MinecraftVars.getPlayerLightLevel();
    cameraPitch = MinecraftVars.getPlayerPitch();
    cameraYaw = MinecraftVars.getPlayerYaw();
    fps = MinecraftVars.getPlayerFPS();
    facing = MinecraftVars.getPlayerFacing();
    leftArrow = MinecraftVars.isLeftArrowDown();
    rightArrow = MinecraftVars.isRightArrowDown();
    upArrow = MinecraftVars.isUpArrowDown();
    downArrow = MinecraftVars.isDownArrowDown();
    tabbedIn = MinecraftVars.isUserTabbedIn();
    potEffects = MinecraftVars.getActivePotionEffects();
}

function updateProvidedLibsWorld() {
    serverIP = MinecraftVars.getServerIP();
    serverMOTD = MinecraftVars.getServerMOTD();
    server = MinecraftVars.getServerName();
}

function print(toPrint) {
    Console.getConsole().out.println(toPrint);
}