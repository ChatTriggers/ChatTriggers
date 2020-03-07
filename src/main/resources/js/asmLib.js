const ASMAt = Java.type('me.falsehonesty.asmhelper.dsl.At');
const ASMInjectionPoint = Java.type('me.falsehonesty.asmhelper.dsl.InjectionPoint');
const ASMDescriptor = Java.type('me.falsehonesty.asmhelper.dsl.instructions.Descriptor');
const asmInjection = Java.type('com.chattriggers.ctjs.engine.langs.js.JSLoader').INSTANCE.asmHelperInject;
const HashMap = Java.type("java.util.HashMap");

class InjectBuilder {
    constructor(className, methodName, descriptor, at) {
        this.className = className;
        this.methodName = methodName;
        this.descriptor = descriptor;
        this.at = at;
        this.methodMap = new HashMap();
        this.fieldMap = new HashMap();
    }

    methodMaps(obj) {
        this.methodMap = obj;

        return this;
    }

    fieldMaps(obj) {
        this.fieldMap = obj;

        return this;
    }

    proxyInsnList($) {
        const self = this;

        const proxy = new Proxy({ builder: $ }, {
            get(target, key) {
                // Here is where new methods are "added". Currently, we only
                // add one method: invokeJS.
                if (key === 'invokeJS') {
                    // We have to return a function to make it callable. Inside
                    // the function, we do our logic (the invoke dynamic calls).
                    // It is important that we are sure to bind all function calls
                    // on the InsnListBuilder to that builder. Otherwise, thisObj
                    // will be set to the global scope.
                    return functionId => {
                        let handle = target.builder.indyHandle.bind(target.builder)(
                            target.builder.H_INVOKESTATIC,
                            "com/chattriggers/ctjs/launch/IndySupport",
                            "bootstrapInvokeJS",
                            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/invoke/CallSite;"
                        );

                        target.builder.invokeDynamic.bind(target.builder)(
                            "invokeJSFunction",
                            "([Ljava/lang/Object;)Ljava/lang/Object;",
                            handle,
                            ASM.currentModule,
                            functionId
                        );

                        // We return this proxy, which is like returning the
                        // original InsnListBuilder for method chaining
                        return proxy;
                    };
                } else {
                    // We delegate the call to the InsnListBuilder. However,
                    // most of the methods on InsnListBuilder will return an
                    // instance of itself, not our special proxy object. So
                    // here, we return another proxy which will execute the
                    // function call, and then return our InsnListBuilder
                    // proxy
                    return new Proxy(target.builder[key], {
                        apply(innerTarget, thisArg, argArray) {
                            // If any arguments are functions, they are single arguments
                            // functions that take the plain InsnListBuidler. Make sure
                            // they actually receive our modified builder
                            for (let i = 0; i < argArray.length; i++) {
                                const arg = argArray[i];
                                if (typeof arg === 'function') {
                                    argArray[i] = $ => arg(self.proxyInsnList($));
                                }
                            }

                            // Make sure that the target function has the original
                            // InsnListBuilder object as it's thisObj
                            const result = innerTarget.bind($)(...argArray);

                            // The following methods always return something other
                            // than the builder
                            if (key === 'makeLabel' || key === 'indyHandle') {
                                return result;
                            }

                            // The following methods only return something other
                            // than the builder on a particular overload (an overload
                            // that takes no arguments)
                            if (argArray.length === 0 && ['astore', 'fstore', 'istore', 'dstore', 'lstore'].includes(key)) {
                                return result;
                            }

                            // The method we've called just returns the builder,
                            // so instead return our modified proxy
                            return proxy;
                        }
                    })
                }
            }
        });

        return proxy;
    }

    instructions(insnList) {
        // Wrap the insnList in a proxy so we can "add" methods to the
        // InsnListBuilder provided by ASMHelper. This proxy delegates
        // all gets and calls to the target handler except those specified
        // in the 'get' trap
        this.insnList = $ => insnList(this.proxyInsnList($));

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
    static INTEGER = 'java/lang/Integer';
    static DOUBLE = 'java/lang/Double';
    static LONG = 'java/lang/Long';
    static BOOLEAN = 'java/lang/Boolean';
    static SHORT = 'java/lang/Short';
    static CHARACTER = 'java/lang/Character';
    static BYTE = 'java/lang/Byte';
    static OBJECT = 'java/lang/Object';
    static STRING = 'java/lang/String';

    static MINECRAFT = 'net/minecraft/client/Minecraft'
    static ENTITY = 'net/minecraft/entity/Entity';
    static ENTITY_FX = 'net/minecraft/client/particle/EntityFX';
    static ENTITY_ITEM = 'net/minecraft/entity/item/EntityItem';
    static ENTITY_PLAYER = 'net/minecraft/entity/player/EntityPlayer';
    static FILE = 'java/io/File';
    static FRAME_BUFFER = 'net/minecraft/client/shader/Framebuffer';
    static ICHATCOMPONENT = 'net/minecraft/util/IChatComponent';
    static INVENTORY_PLAYER = 'net/minecraft/entity/player/InventoryPlayer';
    static ITEM_STACK = 'net/minecraft/item/ItemStack';
    static PACKET = 'net/minecraft/network/Packet';
    static BLOCK_POS = 'net/minecraft/util/BlockPos';
    static ENUM_FACING = 'net/minecraft/util/EnumFacing';
    static CANCELLABLE_EVENT = 'com/chattriggers/ctjs/minecraft/listeners/CancellableEvent'
    static TRIGGER_TYPE = 'com/chattriggers/ctjs/triggers/TriggerType';

    static currentModule = "";

    static JumpCondition = Java.type('me.falsehonesty.asmhelper.dsl.instructions.JumpCondition');

    static ARRAY(o) {
        return `[${o}`;
    }

    static L(o) {
        return `L${o};`;
    }

    static At(injectionPoint, before = true, shift = 0) {
        return new ASMAt(injectionPoint, before, shift);
    }

    static desc(returnType, ...paramTypes) {
        return `(${paramTypes.join('')})${returnType}`;
    }

    static injectBuilder(className, methodName, descriptor, at) {
        return new InjectBuilder(className, methodName, descriptor, at);
    }
}

ASM.At.HEAD = ASMInjectionPoint.HEAD.INSTANCE;

ASM.At.RETURN = function (ordinal = null) {
    return new ASMInjectionPoint.RETURN(ordinal);
};

ASM.At.INVOKE = function (
    owner = throw new Error('ASM.At.INVOKE requires an owner parameter'),
    name = throw new Error('ASM.At.INVOKE requires a name parameter'),
    descriptor = throw new Error('ASM.At.INVOKE requires a descriptor parameter'),
    ordinal = null
) {
    return new ASMInjectionPoint.INVOKE(new ASMDescriptor(owner, name, descriptor), ordinal)
};

ASM.At.TAIL = ASMInjectionPoint.TAIL.INSTANCE;

ASM.At.CUSTOM = function (finder = () => ([])) {
    return new ASMInjectionPoint.CUSTOM(finder);
};