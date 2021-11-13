import { At, InjectionPoint, Method } from 'jvm:dev.falsehonesty.asmhelper.dsl';
import Descriptor from 'jvm:dev.falsehonesty.asmhelper.dsl.instructions.Descriptor';
import JSLoader from 'jvm:com.chattriggers.ctjs.engine.langs.js.JSLoader'
import JumpCondition from 'jvm:dev.falsehonesty.asmhelper.dsl.instructions.JumpCondition';
import AccessType from 'jvm:dev.falsehonesty.asmhelper.dsl.writers.AccessType';

const { asmInjectHelper, asmRemoveHelper, asmFieldHelper } = JSLoader.INSTANCE;

const proxyInsnList = $ => {
    const proxy = new Proxy({builder: $}, {
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
                    let handle = target.builder.handle.bind(target.builder)(
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
                                argArray[i] = $ => arg(proxyInsnList($));
                            }
                        }

                        // Make sure that the target function has the original
                        // InsnListBuilder object as it's thisObj
                        const result = innerTarget.bind($)(...argArray);

                        // The following methods always return something other
                        // than the builder
                        if (key === 'makeLabel' || key === 'handle') {
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

class ASMBuilder {
    constructor(className, methodName, descriptor, at) {
        this.className = className;
        this.methodName = methodName;
        this.descriptor = descriptor;
        this.at = at;
    }
}

class RemoveBuilder extends ASMBuilder {
    constructor(className, methodName, descriptor, at) {
        super(className, methodName, descriptor, at);
        this._methodMaps = {};
    }

    methodMaps(obj) {
        this._methodMaps = obj;
        return this;
    }

    numberToRemove(numberToRemove) {
        this.numberToRemove = numberToRemove;
        return this;
    }

    execute() {
        asmRemoveHelper(this.className, this.at, this.methodName, this.descriptor, this._methodMaps, this.numberToRemove);
    }
}

class FieldBuilder {
    constructor(className, fieldName, descriptor, accessTypes) {
        this.className = className;
        this.fieldName = fieldName;
        this.descriptor = descriptor;
        this.accessTypes = accessTypes ?? [];
    }

    initialValue(obj) {
        this._initialValue = obj;
        return this;
    }

    execute() {
        asmFieldHelper(this.className, this.fieldName, this.descriptor, this._initialValue, this.accessTypes);
    }
}

class InjectBuilder extends ASMBuilder {
    constructor(className, methodName, descriptor, at) {
        super(className, methodName, descriptor, at);
        this._methodMaps = {};
        this._fieldMaps = {};
    }

    methodMaps(obj) {
        this._methodMaps = obj;
        return this;
    }

    fieldMaps(obj) {
        this._fieldMaps = obj;
        return this;
    }

    instructions(insnList) {
        // Wrap the insnList in a proxy so we can "add" methods to the
        // InsnListBuilder provided by ASMHelper. This proxy delegates
        // all gets and calls to the target handler except those specified
        // in the 'get' trap
        this.insnList = $ => insnList(proxyInsnList($));

        return this;
    }

    execute() {
        if (this.insnList === undefined) {
            throw new Error('InjectBuilder requires a call to instructions()');
        }

        asmInjectHelper(this.className, this.at, this.methodName, this.descriptor, this._fieldMaps, this._methodMaps, this.insnList);
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

    static MINECRAFT = 'net/minecraft/client/Minecraft';
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
    static CANCELLABLE_EVENT = 'com/chattriggers/ctjs/minecraft/listeners/CancellableEvent';
    static TRIGGER_TYPE = 'com/chattriggers/ctjs/triggers/TriggerType';

    static currentModule = "";

    static JumpCondition = JumpCondition

    static AccessType = AccessType

    static ARRAY(o) {
        return `[${o}`;
    }

    static L(o) {
        return `L${o};`;
    }

    static At(injectionPoint, before = true, shift = 0) {
        return new At(injectionPoint, before, shift);
    }

    static desc(returnType, ...paramTypes) {
        return `(${paramTypes.join('')})${returnType}`;
    }

    static injectBuilder(className, methodName, descriptor, at) {
        return new InjectBuilder(className, methodName, descriptor, at);
    }

    static removeBuilder(className, methodName, descriptor, at) {
        return new RemoveBuilder(className, methodName, descriptor, at);
    }

    static fieldBuilder(className, fieldName, descriptor, ...accessTypes) {
        return new FieldBuilder(className, fieldName, descriptor, accessTypes);
    }

    static modify(className, block) {
        Method.modify(className, block);
    }
}

ASM.At.HEAD = InjectionPoint.HEAD.INSTANCE;

ASM.At.RETURN = (ordinal = null) => new InjectionPoint.RETURN(ordinal);

ASM.At.INVOKE = (owner, name, descriptor, ordinal = null) => {
    if (owner === undefined)
        throw new Error('ASM.At.INVOKE requires an owner parameter');
    if (name === undefined)
        throw new Error('ASM.At.INVOKE requires a name parameter');
    if (descriptor === undefined)
        throw new Error('ASM.At.INVOKE requires a descriptor parameter');

    return new InjectionPoint.INVOKE(new Descriptor(owner, name, descriptor), ordinal);
};

ASM.At.TAIL = InjectionPoint.TAIL.INSTANCE;

ASM.At.CUSTOM = (finder = () => ([])) => new InjectionPoint.CUSTOM(finder);
