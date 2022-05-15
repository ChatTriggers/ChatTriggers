(function(global) {
    // Helpers to load classes which throws if the class isn't found. This
    // helps spot refactoring bugs early!

    global.Java = {
        type: clazz => Packages[clazz]
    };

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

    // TODO(BREAKING): Does replacing these classes break any APIs?
    loadClass("gg.essential.universal.wrappers.message.UMessage", "Message");
    loadClass("gg.essential.universal.wrappers.message.UTextComponent", "TextComponent");

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
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.utils.ResourceLocation");

    loadClass("com.chattriggers.ctjs.minecraft.wrappers.entity.Entity");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.entity.EntityLivingBase");
    global.EquipmentSlot = global.EntityLivingBase.EquipmentSlot;
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.entity.PlayerMP");

    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.Inventory");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.Item");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.Slot");

    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.action.Action");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.action.ClickAction");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.action.DragAction");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.action.DropAction");
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.inventory.action.KeyAction");

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
    loadClass("com.chattriggers.ctjs.minecraft.wrappers.utils.WrappedThread", "Thread");
    global.Console = getClass("com.chattriggers.ctjs.engine.langs.js.JSLoader").INSTANCE.getConsole();
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

    global.sync = (func, lock) => new org.mozilla.javascript.Synchronizer(func, lock);

    global.setTimeout = (func, delay) => {
        new Thread(() => {
            Thread.sleep(delay);
            func();
        }).start();
    }

    const LogType = com.chattriggers.ctjs.utils.console.LogType;

    // TODO: Make println and roll this back before 3.0.0
    // simplified methods
    global.print = function (toPrint, end = "\n", color = null) {
        if (toPrint === null) {
            toPrint = 'null';
        } else if (toPrint === undefined) {
            toPrint = 'undefined';
        }

        Console.println(toPrint, LogType.INFO, end, color);
    };

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


    /** CONSOLE POLYFILL */

    /**
     * @fileoverview console.js
     * Implementation of the whatwg/console namespace for the Nashorn script engine.
     * Ported to CT/Rhino
     * See https://console.spec.whatwg.org
     *
     * @author https://github.com/fmartin5
     *
     * @license AGPL-3.0
     *
     * @environment Nashorn on JDK 9
     *
     * @globals es5
     *
     * @syntax es5 +arrow-functions +const +for-of +let
     *
     * @members
     * config (non-standard)
     * assert
     * clear
     * count
     * debug
     * dir
     * dirxml
     * error
     * group
     * groupCollapsed
     * groupEnd
     * info
     * log
     * table
     * time
     * timeEnd
     * trace
     * warn
     */

    function factory() {
        const console = {};

        console.config = {
            "indent": " | ",
            "showMilliseconds": false,
            "showMessageType": false,
            "showTimeStamp": false,
            "useColors": false,
            "colorsByLogLevel": {
                "error": "",
                "log": "",
                "info": "",
                "warn": ""
            }
        };

        // An object holding local data and functions.
        const _ = {};

        // The following line can be commented out in development for debugging purposes.
        // console._ = _;

        _.counters = {};

        _.defaultStringifier = function (anyValue) {
            // noinspection FallThroughInSwitchStatementJS
            switch (typeof anyValue) {
                case "function":
                    return "[object Function(" + anyValue.length + ")]";
                case "object": {
                    if (Array.isArray(anyValue)) return "[object Array(" + anyValue.length + ")]";
                }
                default:
                    return String(anyValue);
            }
        };

        _.formats = {
            "%d": (x) => typeof x === "symbol" ? NaN : parseInt(x),
            "%f": (x) => typeof x === "symbol" ? NaN : parseFloat(x),
            "%i": (x) => typeof x === "symbol" ? NaN : parseInt(x),
            "%s": (x) => String(x),
            "%%": (x) => "%",
            // @todo Implement less trivial behaviors for the following format specifiers.
            "%c": (x) => String(x),
            "%o": (x) => String(x),
            "%O": (x) => String(x)
        };

        _.indentLevel = 0;
        _.lineSep = java.lang.System.getProperty("line.separator");
        _.timers = {};


        // Formatter
        _.formatArguments = function (args) {
            const rv = [];
            for (let i = 0; i < args.length; i++) {
                const arg = args[i];
                if (typeof arg === "string") {
                    rv.push(arg.replace(/%[cdfiosO%]/g, (match) => {
                        if (match === "%%") return "%";
                        return ++i in args ? _.formats[match](args[i]) : match;
                    }));
                } else {
                    rv.push(String(arg));
                }
            }
            return rv.join(" ");
        };

        _.formatObject = function (object) {
            const sep = _.lineSep;
            return _.defaultStringifier(object) + (" {" + sep + " ") + Object.keys(object).map(key =>
                _.defaultStringifier(key) + ": " + _.defaultStringifier(object[key])).join("," + sep + " ") + (sep + "}");
        };

        _.makeTimeStamp = function () {
            if (console.config.showMilliseconds) {
                const date = new Date();
                return date.toLocaleTimeString() + "," + date.getMilliseconds();
            }
            return new Date().toLocaleTimeString();
        };


        // Printer
        _.printlnWarn = function (s) {
            Console.println(s, LogType.WARN);
        };

        _.printLineToStdErr = function (s) {
            Console.println(s, LogType.ERROR);
        };

        _.printLineToStdOut = function (s) {
            Console.println(s);
        };


        _.repeat = (s, n) => new Array(n + 1).join(s);


        // Logger
        _.writeln = function (msgType, msg) {
            if (arguments.length < 2) return;
            const showMessageType = console.config.showMessageType;
            const showTimeStamp = console.config.showTimeStamp;
            const msgTypePart = showMessageType ? msgType : "";
            const timeStampPart = showTimeStamp ? _.makeTimeStamp() : "";
            const prefix = (showMessageType || showTimeStamp
                ? "[" + msgTypePart + (msgTypePart && timeStampPart ? " - " : "") + timeStampPart + "] "
                : "");
            const indent = (_.indentLevel > 0
                ? _.repeat(console.config.indent, _.indentLevel)
                : "");
            switch (msgType) {
                case "assert":
                case "error": {
                    _.printLineToStdErr(prefix + indent + msg);
                    break;
                }
                case "warn": {
                    _.printlnWarn(prefix + indent + msg);
                    break;
                }
                default: {
                    _.printLineToStdOut(prefix + indent + msg);
                }
            }
        };


        /**
         * Tests whether the given expression is true. If not, logs a message with the visual "error" representation.
         */
        console.assert = function assert(booleanValue, arg) {
            if (!!booleanValue) return;
            const defaultMessage = "assertion failed";
            if (arguments.length < 2) return _.writeln("assert", defaultMessage);
            if (typeof arg !== "string") return _.writeln("assert", _.formatArguments([defaultMessage, arg]));
            _.writeln("assert", (defaultMessage + ": " + arg));
        };


        /**
         * Clears the console.
         * Works by invoking `cmd /c cls` on Windows and `clear` on other OSes.
         */
        console.clear = function clear() {
            try {
                Console.clearConsole();
            } catch (_) {
                // Pass
            }
        };


        /**
         * Prints the number of times that 'console.count()' was called with the same label.
         */
        console.count = function count(label = "default") {
            label = String(label);
            if (!(label in _.counters)) _.counters[label] = 0;
            _.counters[label]++;
            _.writeln("count", label + ": " + _.counters[label]);
        };


        /**
         * Logs a message, with a visual "debug" representation.
         * @todo Optionally include some information for debugging, like the file path or line number where the call occurred from.
         */
        console.debug = function debug(...args) {
            const s = _.formatArguments(args);
            _.writeln("debug", s);
        };

        /**
         * Logs a listing of the properties of the given object.
         * @todo Use the `options` argument.
         */
        console.dir = function dir(arg, options) {
            if (Object(arg) === arg) {
                _.writeln("dir", _.formatObject(arg));
                return;
            }
            _.writeln("dir", _.defaultStringifier(arg));
        };


        /**
         * Logs a space-separated list of formatted representations of the given arguments,
         * using DOM tree representation whenever possible.
         */
        console.dirxml = function dirxml(...args) {
            const list = [];
            args.forEach((arg) => {
                if (Object(arg) === arg) list.push(_.formatObject(arg));
                else list.push(_.defaultStringifier(arg));
            });
            _.writeln("dirxml", list.join(" "));
        };


        /**
         * Logs a message with the visual "error" representation.
         * @todo Optionally include some information for debugging, like the file path or line number where the call occurred from.
         */
        console.error = function error(...args) {
            const s = _.formatArguments(args);
            _.writeln("error", s);
        };


        /**
         * Logs a message as a label for and opens a nested block to indent future messages sent.
         * Call console.groupEnd() to close the block.
         * Representation of block is up to the platform,
         * it can be an interactive block or just a set of indented sub messages.
         */
        console.group = function group(...args) {
            if (args.length) {
                const s = _.formatArguments(args);
                _.writeln("group", s);
            }
            _.indentLevel++;
        };


        console.groupCollapsed = function groupCollapsed(...args) {
            console.group([]);
        };


        /**
         * Closes the most recently opened block created by a call
         * to 'console.group()' or 'console.groupCollapsed()'.
         */
        console.groupEnd = function groupEnd() {
            if (_.indentLevel < 1) return;
            _.indentLevel--;
        };


        /**
         * Logs a message with the visual "info" representation.
         */
        console.info = function info(...args) {
            const s = _.formatArguments(args);
            _.writeln("info", s);
        };


        /**
         * Logs a message with the visual "log" representation.
         */
        console.log = function log(...args) {
            const s = _.formatArguments(args);
            _.writeln("log", s);
        };


        console.time = function time(label = "default") {
            label = String(label);
            if (label in _.timers) return;
            _.timers[label] = Date.now();
        };


        /**
         * Stops a timer created by a call to `console.time(label)` and logs the elapsed time.
         */
        console.timeEnd = function timeEnd(label = "default") {
            label = String(label);
            const milliseconds = Date.now() - _.timers[label];
            delete _.timers[label];

            _.writeln("timeEnd", label + ": " + milliseconds + " ms");
        };


        /**
         * Logs a stack trace for where the call occurred from, using the given arguments as a label.
         */
        console.trace = function trace(...args) {
            const label = "Trace" + (args.length > 0 ? ": " + _.formatArguments(args) : "");
            const e = new Error();
            Error.captureStackTrace(e, trace);
            // Replaces the first line by our label.
            const s = label + "\n" + e.stack;
            _.writeln("trace", s);
        };


        /**
         * @todo Logs a tabular representation of the given data.
         * Fall back to just logging the argument if it can’t be parsed as tabular.
         */
        console.table = function table(tabularData, properties) {
            console.log(tabularData);
        };


        /**
         * Logs a message with the visual "warning" representation.
         */
        console.warn = function warn(...args) {
            const s = _.formatArguments(args);
            _.writeln("warn", s);
        };

        return console;
    }

    Object.defineProperty(global, "console", {
        "writable": true,
        "configurable": true,
        "enumerable": false,
        "value": factory()
    });
})(this);
