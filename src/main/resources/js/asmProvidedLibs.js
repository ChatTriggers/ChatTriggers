globalThis.Thread = Java.type("com.chattriggers.ctjs.engine.langs.js.JSWrappedThread");
globalThis.Console = Java.type("com.chattriggers.ctjs.engine.langs.js.JSLoader").INSTANCE.getConsole();

globalThis.sync = (func, lock) => new org.mozilla.javascript.Synchronizer(func, lock);

global.Thread = Java.type("com.chattriggers.ctjs.minecraft.wrappers.utils.WrappedThread");
global.Console = Java.type("com.chattriggers.ctjs.engine.langs.js.JSLoader").INSTANCE.getConsole();

global.setTimeout = function (func, delay) {
    new Thread(function () {
        Thread.sleep(delay);
        func();
    }).start();
};

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
