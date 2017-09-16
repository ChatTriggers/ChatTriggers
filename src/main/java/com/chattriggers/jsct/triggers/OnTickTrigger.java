package com.chattriggers.jsct.triggers;

import com.chattriggers.jsct.JSCT;

import javax.script.ScriptException;

public class OnTickTrigger extends Trigger {

    public OnTickTrigger(String methodName) {
        super(methodName);
    }

    @Override
    public void trigger(Object... args) {
        try {
            JSCT.getInstance().getScriptEngine().invokeFunction(methodName);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
