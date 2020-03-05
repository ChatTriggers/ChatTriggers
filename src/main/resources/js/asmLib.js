const ASMAt = Java.type('me.falsehonesty.asmhelper.dsl.At');
const ASMInjectionPoint = Java.type('me.falsehonesty.asmhelper.dsl.InjectionPoint');
const ASMDescriptor = Java.type('me.falsehonesty.asmhelper.dsl.instructions.Descriptor');
const asmInjection = Java.type('com.chattriggers.ctjs.launch.AsmUtilsKt').inject;
const HashMap = Java.type("java.util.HashMap");
const ModuleManager = Java.type("com.chattriggers.ctjs.engine.module.ModuleManager").INSTANCE;

class InjectBuilder {
    constructor(className, at, methodName, descriptor) {
        this.className = className;
        this.at = at;
        this.methodName = methodName;
        this.descriptor = descriptor;
        this.methodMap = new HashMap();
        this.fieldMap = new HashMap();
    }

    methodMaps(maps) {
        maps.forEach(([key, val]) => {
            this.methodMap.put(key, val);
        });

        return this;
    }

    fieldMaps(maps) {
        maps.forEach(([key, val]) => {
            this.fieldMap.put(key, val);
        });

        return this;
    }

    instructions(insnList) {
        this.insnList = insnList;

        return this;
    }

    execute() {
        if (this.insnList === undefined) {
            throw new Error('InjectBuilder requires a call to instructions()');
        }

        asmInjection(this.className, this.at, this.methodName, this.descriptor, this.fieldMap, this.methodMap, this.insnList);
    }
}

export default class ASM {
    static STRING = 'Ljava/lang/String;';
    static INTEGER = "Ljava/lang/Integer;";
    static DOUBLE = "Ljava/lang/Double;";
    static LONG = "Ljava/lang/Long;";
    static BOOLEAN = "Ljava/lang/Boolean;";
    static SHORT = "Ljava/lang/Short;";
    static CHARACTER = "Ljava/lang/Character";
    static BYTE = "Ljava/lang/Byte;";
    static OBJECT = "Ljava/lang/Object;";

    static currentModule = "";

    static ARRAY(o) {
        return "[" + o;
    }

    static At(injectionPoint, before = true, shift = 0) {
        return new ASMAt(injectionPoint, before, shift);
    }

    static desc(returnType, ...paramTypes) {
        return `(${paramTypes.join()})${returnType}`;
    }

    static injectBuilder(className, at, methodName, descriptor) {
        return new InjectBuilder(className, at, methodName, descriptor);
    }

    static invokeJS($, functionId) {
        let handle = $.indyHandle(
            $.H_INVOKESTATIC,
            "com/chattriggers/ctjs/launch/IndySupport",
            "bootstrapInvokeJS",
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/invoke/CallSite;"
        );

        $.invokeDynamic(
            "invokeJSFunction",
            "([Ljava/lang/Object;)Ljava/lang/Object;",
            handle,
            ASM.currentModule,
            functionId
        );
    }
}

ASM.At.HEAD = ASMInjectionPoint.HEAD.INSTANCE;

ASM.At.RETURN = function(ordinal = null) {
    return new ASMInjectionPoint.RETURN(ordinal);
};

ASM.At.INVOKE = function(
    owner = throw new Error('ASM.At.INVOKE requires an owner parameter'),
    name = throw new Error('ASM.At.INVOKE requires a name parameter'),
    descriptor = throw new Error('ASM.At.INVOKE requires a descriptor parameter'),
    ordinal = null
) {
    return new ASMInjectionPoint.INVOKE(new ASMDescriptor(owner, name, descriptor), ordinal)
};

ASM.At.TAIL = ASMInjectionPoint.TAIL.INSTANCE;

ASM.At.CUSTOM = function(finder = () => ([])) {
    return new ASMInjectionPoint.CUSTOM(finder);
};