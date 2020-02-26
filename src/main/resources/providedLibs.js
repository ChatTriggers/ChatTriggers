let global = this;

global.Java = {
    type: function (clazz) {
        const split = clazz.split(".");

        let returned = Packages;

        for (let i = 0; i < split.length; i++) {
            returned = returned[split[i]];
        }

        return returned;
    }
};

global.sync = (func, lock) => new org.mozilla.javascript.Synchronizer(func, lock);

// Extra libs
global.ArrayList = Java.type("java.util.ArrayList");
global.HashMap = Java.type("java.util.HashMap");
global.Keyboard = Java.type("org.lwjgl.input.Keyboard");
global.ReflectionHelper = Java.type("net.minecraftforge.fml.relauncher.ReflectionHelper");

// Triggers
global.TriggerRegister = Java.type("com.chattriggers.ctjs.engine.langs.js.JSRegister").INSTANCE;
global.TriggerResult = Java.type("com.chattriggers.ctjs.triggers.OnTrigger.TriggerResult");
global.Priority = Java.type("com.chattriggers.ctjs.triggers.OnTrigger.Priority");
//#if MC<=10809
global.InteractAction = Java.type("net.minecraftforge.event.entity.player.PlayerInteractEvent.Action");
//#else
//$$ global.InteractAction = Java.type("com.chattriggers.ctjs.minecraft.listeners.ClientListener").INSTANCE.PlayerInteractAction;
//#endif

// Libraries
global.ChatLib = Java.type("com.chattriggers.ctjs.minecraft.libs.ChatLib");
global.EventLib = Java.type("com.chattriggers.ctjs.minecraft.libs.EventLib");

global.Renderer = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Renderer");
global.Shape = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Shape");
global.Rectangle = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Rectangle");
global.Text = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Text");
global.Image = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Image");

global.Tessellator = Java.type("com.chattriggers.ctjs.minecraft.libs.Tessellator");
global.FileLib = Java.type("com.chattriggers.ctjs.minecraft.libs.FileLib");
global.MathLib = Java.type("com.chattriggers.ctjs.minecraft.libs.MathLib");

// Objects
global.Display = Java.type("com.chattriggers.ctjs.engine.langs.js.JSDisplay");
global.DisplayLine = Java.type("com.chattriggers.ctjs.engine.langs.js.JSDisplayLine");
global.DisplayHandler = Java.type("com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler");
global.Gui = Java.type("com.chattriggers.ctjs.engine.langs.js.JSGui");
global.Message = Java.type("com.chattriggers.ctjs.minecraft.objects.message.Message");
global.TextComponent = Java.type("com.chattriggers.ctjs.minecraft.objects.message.TextComponent");
global.Book = Java.type("com.chattriggers.ctjs.minecraft.objects.Book");
global.KeyBind = Java.type("com.chattriggers.ctjs.minecraft.objects.KeyBind");
global.Image = Java.type("com.chattriggers.ctjs.minecraft.libs.renderer.Image");
global.Sound = Java.type("com.chattriggers.ctjs.minecraft.objects.Sound");
global.PlayerMP = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP");

// Wrappers
global.Client = Java.type("com.chattriggers.ctjs.minecraft.wrappers.Client");
global.Player = Java.type("com.chattriggers.ctjs.minecraft.wrappers.Player");
global.World = Java.type("com.chattriggers.ctjs.minecraft.wrappers.World");
global.Server = Java.type("com.chattriggers.ctjs.minecraft.wrappers.Server");
global.Inventory = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Inventory");
global.TabList = Java.type("com.chattriggers.ctjs.minecraft.wrappers.TabList");
global.Scoreboard = Java.type("com.chattriggers.ctjs.minecraft.wrappers.Scoreboard");
global.CPS = Java.type("com.chattriggers.ctjs.minecraft.wrappers.CPS");
global.Item = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item");
global.Block = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.block.Block");
global.Sign = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.block.Sign");
global.Entity = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.Entity");
global.Action = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.Action");
global.ClickAction = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.ClickAction");
global.DragAction = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.DragAction");
global.KeyAction = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.KeyAction");
global.Particle = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.Particle");

// Triggers
global.OnChatTrigger = Java.type("com.chattriggers.ctjs.triggers.OnChatTrigger");
global.OnCommandTrigger = Java.type("com.chattriggers.ctjs.triggers.OnCommandTrigger");
global.OnRegularTrigger = Java.type("com.chattriggers.ctjs.triggers.OnRegularTrigger");
global.OnRenderTrigger = Java.type("com.chattriggers.ctjs.triggers.OnRenderTrigger");
global.OnSoundPlayTrigger = Java.type("com.chattriggers.ctjs.triggers.OnSoundPlayTrigger");
global.OnStepTrigger = Java.type("com.chattriggers.ctjs.triggers.OnStepTrigger");
global.OnTrigger = Java.type("com.chattriggers.ctjs.triggers.OnTrigger");

// Misc
global.Console = Java.type("com.chattriggers.ctjs.engine.langs.js.JSLoader").INSTANCE.getConsole();
global.Config = Java.type("com.chattriggers.ctjs.utils.config.Config").INSTANCE;
global.ChatTriggers = Java.type("com.chattriggers.ctjs.Reference").INSTANCE;
/*End Built in Vars */

// Thread
global.Thread = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.threading.WrappedThread");

global.setTimeout = function (func, delay) {
    new Thread(function () {
        Thread.sleep(delay);
        func();
    }).start();
};

// simplified methods
global.print = function (toPrint) {
    if (toPrint === null) {
        Console.out.println("null");
        return;
    }
    Console.out.println(toPrint);
};

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

// String prototypes
String.prototype.addFormatting = function () {
    return ChatLib.addFormatting(this);
};

String.prototype.removeFormatting = function () {
    return ChatLib.removeFormatting(this);
};

String.prototype.replaceFormatting = function () {
    return ChatLib.replaceFormatting(this);
};

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

(function (globalObject, factory) {
    Object.defineProperty(globalObject, "console", {
        "writable": true,
        "configurable": true,
        "enumerable": false,
        "value": factory()
    });
}(global, function factory() {
    const console = Object.create(Object.create(Object.prototype));

    const System = java.lang.System;

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

    _.counters = Object.create(null);

    _.defaultStringifier = function (anyValue) {
        switch (typeof anyValue) {
            case "boolean":
                return anyValue + "";
            case "function":
                return "[object Function(" + anyValue.length + ")]";
            case "number":
                return anyValue + "";
            case "object": {
                if (anyValue === null) return "null";
                else if (Array.isArray(anyValue)) return "[object Array(" + anyValue.length + ")]";
                else return _.toString(anyValue);
            }
            case "string":
                return JSON.stringify(anyValue);
            case "symbol":
                return anyValue + "";
            case "undefined":
                return "undefined";
            case "unknown":
                return "[object #Unknown]";
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
    _.timers = Object.create(null);


    // Formatter
    _.formatArguments = function (argumentObject) {
        const rv = [];
        for (var i = 0; i < argumentObject.length; i++) {
            const arg = argumentObject[i];
            const args = argumentObject;
            if (typeof arg === "string") {
                rv[rv.length] = arg.replace(/%[cdfiosO%]/g, (match, index) => {
                    if (match === "%%") return "%";
                    return ++i in args ? _.formats[match](args[i]) : match;
                });
            } else if (Object(arg) === arg) {
                rv[rv.length] = _.toString(arg);
            } else {
                rv[rv.length] = String(arg);
            }
        }
        return rv.join(" ");
    };


    _.formatAttributes = function (attributes) {
        const a = [];
        for (var i = 0; i < attributes.length; i++) {
            const attribute = attributes.item(i);
            a[a.length] = attribute.name + "=" + JSON.stringify(attribute.value);
        }
        return a.join(" ");
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
    _.println = function (s) {
        Console.out.println(s);
    };

    _.printLineToStdErr = function (s) {
        Console.out.println(s);
    };

    _.printLineToStdOut = function (s) {
        Console.out.println(s);
    };


    _.repeat = (s, n) => new Array(n + 1).join(s);
    _.slice = Function.prototype.call.bind(Array.prototype.slice);
    _.toString = Function.prototype.call.bind(Object.prototype.toString);


    // Logger
    _.writeln = function (msgType, msg) {
        if (arguments.length < 2) return;
        const showMessageType = console.config.showMessageType;
        const showTimeStamp = console.config.showTimeStamp;
        const msgTypePart = showMessageType ? msgType : "";
        const timeStampPart = showTimeStamp ? this.makeTimeStamp() : "";
        const prefix = (showMessageType || showTimeStamp
            ? "[" + msgTypePart + (msgTypePart && timeStampPart ? " - " : "") + timeStampPart + "] "
            : "");
        const indent = (this.indentLevel > 0
            ? _.repeat(console.config.indent, this.indentLevel)
            : "");
        switch (msgType) {
            case "assert":
            case "error":
            case "warn": {
                _.printLineToStdErr(prefix + indent + msg);
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
    console.count = function count(label) {
        if (typeof label === "undefined") label = "default";
        label = String(label);
        if (!(label in _.counters)) _.counters[label] = 0;
        _.counters[label]++;
        _.writeln("count", label + ": " + _.counters[label]);
    };


    /**
     * Logs a message, with a visual "debug" representation.
     * @todo Optionally include some information for debugging, like the file path or line number where the call occurred from.
     */
    console.debug = function debug(args) {
        const s = _.formatArguments(arguments);
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
    console.dirxml = function dirxml(args) {
        const list = [];
        for (const arg of _.slice(arguments)) {
            if (Object(arg) === arg) list[list.length] = _.formatObject(arg);
            else list[list.length] = _.defaultStringifier(arg);
        }
        _.writeln("dirxml", list.join(" "));
    };


    /**
     * Logs a message with the visual "error" representation.
     * @todo Optionally include some information for debugging, like the file path or line number where the call occurred from.
     */
    console.error = function error(args) {
        const s = _.formatArguments(arguments);
        _.writeln("error", s);
    };


    /**
     * Logs a message as a label for and opens a nested block to indent future messages sent.
     * Call console.groupEnd() to close the block.
     * Representation of block is up to the platform,
     * it can be an interactive block or just a set of indented sub messages.
     */
    console.group = function group(args) {
        if (arguments.length) {
            const s = _.formatArguments(arguments);
            _.writeln("group", s);
        }
        _.indentLevel++;
    };


    console.groupCollapsed = function groupCollapsed(args) {
        console.group.apply(null, _.slice(arguments, 0));
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
    console.info = function info(args) {
        const s = _.formatArguments(arguments);
        _.writeln("info", s);
    };


    /**
     * Logs a message with the visual "log" representation.
     */
    console.log = function log(args) {
        const s = _.formatArguments(arguments);
        _.writeln("log", s);
    };


    console.time = function time(label) {
        if (typeof label === "undefined") label = "default";
        label = String(label);
        if (label in _.timers) return;
        _.timers[label] = Date.now();
    };


    /**
     * Stops a timer created by a call to `console.time(label)` and logs the elapsed time.
     */
    console.timeEnd = function timeEnd(label) {
        if (typeof label === "undefined") label = "default";
        label = String(label);
        const milliseconds = Date.now() - _.timers[label];
        _.writeln("timeEnd", label + ": " + milliseconds + " ms");
    };


    /**
     * Logs a stack trace for where the call occurred from, using the given arguments as a label.
     */
    console.trace = function trace(args) {
        const label = "Trace" + ("0" in arguments ? ": " + _.formatArguments(arguments) : "");
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
    console.warn = function warn(args) {
        const s = _.formatArguments(arguments);
        _.writeln("warn", s);
    };

    return console;
}));