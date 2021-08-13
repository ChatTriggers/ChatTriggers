let global = this;

global.Java = {
    type: clazz => Packages[clazz]
};

global.Thread = Java.type("com.chattriggers.ctjs.minecraft.wrappers.objects.threading.WrappedThread");
global.Console = Java.type("com.chattriggers.ctjs.engine.langs.js.JSLoader").INSTANCE.getConsole();

global.sync = (func, lock) => new org.mozilla.javascript.Synchronizer(func, lock);

global.setTimeout = function (func, delay) {
    new Thread(function () {
        Thread.sleep(delay);
        func();
    }).start();
};

// simplified methods
global.print = function (toPrint) {
    if (toPrint === null) {
        toPrint = 'null';
    } else if (toPrint === undefined) {
        toPrint = 'undefined';
    }

    Console.println(toPrint);
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
        Console.println(s);
    };

    _.printLineToStdErr = function (s) {
        Console.println(s);
    };

    _.printLineToStdOut = function (s) {
        Console.println(s);
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
