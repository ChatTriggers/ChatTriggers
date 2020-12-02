# Migrating to 1.0.0

1.0.0 is the newest version of ct.js, and with it comes a new JavaScript engine with a whole host of
new features. Modules that were built in 0.18.4 will no longer work, so this file will answer
any questions you might have about updating your modules from 0.18.4 to 1.0.0.

### What changed?

0.18.4, as well as all previous versions of ct.js, used a JavaScript engine called 
[Nashorn](https://en.wikipedia.org/wiki/Nashorn_(JavaScript_engine)). Nashorn was built and maintained 
by Oracle. This was fine initially, however with the release of Java 11, Nashorn has been officially
deprecated and will be removed from the JDK at some point. Nashorn implements ES5, and considering 
the deprecation, will not support any of the newer ES6/ESNEXT features.

In 1.0.0, we replaced Nashorn with an engine called 
[Rhino](https://developer.mozilla.org/en-US/docs/Mozilla/Projects/Rhino). Created by Mozilla, Rhino
has been fully open-source since its creation, and is still being maintained, albeit very slowly.
In fact, ct.js actually contains our own custom build of Rhino, which supports most of the new
ES6 features, as well as many ESNEXT features. These features will all be discussed in a later section.
Our custom build can be found [here](https://github.com/ChatTriggers/rhino/).

 ### Differences between Nashorn and Rhino
 
 As previously mentioned, Rhino (specifically, our custom build) supports many of the latest JavaScript
 features -- features that were never going to be supported in Nashorn. Additionally, the module engine
 has been completely reworked, and is the primary reason why old modules will no longer work in the
 new version.
 
### Updating modules to 1.0.0

##### Sandboxing

The most relevant change for you, as a module creator, in 1.0.0 is the modules are now **sandboxed**.
In 0.18.4, if you declared a global variable in one module or file, you could immediately access it in
a different module or file. This is because all modules shared the same *top-level scope*. In 1.0.0, this 
is not the case. Each module has its own top-level scope, and thus global variables declared in one module
or file cannot immediately be accessed in another module or file. 

At first, this may seen like an annoyance, however this change is quite useful. Though it was nice not 
having to worry about getting access to data in different files, module creators had to worry about giving
all of their top-level declarations unique (and often quite long) names. With the module sandboxing, this is
no longer a concern.

So, how does one get access to data located in a different file? Rhino supports ES6 module importing and 
exporting for precisely this purpose. To learn more about the import and export syntax, take a look at
[this guide](https://alligator.io/js/modules-es6/). A brief introduction will be provided here, to get the
point across.

Lets say your 0.18.4 module looked like this:
```js
/* lib.js */
function add(num1, num2) {
    return num1 + num2;
}

/* main.js */
print(add(4, 9)); // prints '13'
```

In order to run the module in 1.0.0, the module would have to be refactored to the following:
```js
/* lib.js */
function add(num1, num2) {
    return num1 + num2;
}

// Export the function so that any other module can use it
export { add };

/* main.js */
// import the function
import { add } from './lib'; // note that you could also import from '<MyModuleName>/lib'
 
print(add(4, 9)); // prints '13'
```

Take a look at the guide posted above for a complete list of all possible syntax.

##### CT Changes

There have also been a few changes with built in libraries that ct.js provides:

- Removed `XMLHttpRequest`; see the `request` module
- Commands can trigger other client-side commands using a new optional parameter in `ChatLib.command`
- Added `Message#getFormattedText` & `Message#getUnformattedText`
- Added `Tessellator.getRenderPos(x, y, z)`
- Changed parameter order of `Tessellator.drawString`:
    - Old parameter order: `(text, x, y, z, renderBlackBox, partialTicks, scale, color, increase)`
    - New parameter order: `(text, x, y, z, color, renderBlackBox, scale, increase)`
- Added `Renderer.getRenderManager`
- Changed console font to Fira Coda
- Moved `World.showTitle` to `Client.showTitle`
- Changed parameter order of `Inventory#click`
    - Old parameter order: `(slot, button, shift)`
    - New parameter order: `(slot, shift, button)`

New triggers:

- `registerRenderArmor`

### New JavaScript features

Rhino comes with many new ES6 features, as well as features from later specs, such as ES7, ES8, and ESNEXT. 
ES6 has been the JS standard for quite a number of years now, so this document will not cover the new
ES6 features. Instead, take a look at [this guide](https://codetower.github.io/es6-features/) for a general
introduction to the new features (note that Promises are not implemented).

Instead, this section will focus on features implemented from versions of JavaScript after ES6. This includes
features that may not even be part of the official ECMA standard, but that we thought were useful and decided
to implement anyways.

##### ES7

ES7 comes with a few minor changes, the most useful of which is the exponentiation operator (`**`). Gone
are the days of `Math.pow`!. Note that if the first argument is a negative number, you must used parenthesis,
or a syntax error will be thrown. This is to avoid ambiguities.

```js
print(5 ** 2);    // '25'
print(-5 ** 2);   // Syntax error: Did you mean "(-5) ** 2", or "-(5 ** 2)"?
print((-5) ** 2); // '25'
print(-(5 ** 2)); // '-25'
```

Additional features include `Array.prototype.includes(object)`, as well as deeply-nested destructuring in both object
literals and function parameters:
```js
const [x, ...[y, ...z]] = [1, 2, 3, 4];
// x === 1
// y === 2
// z === [3, 4]
```

##### ES8

ES8 brings support for the following functions:

- `Object.values(object)`
- `Object.entries(object)`
- `Object.getOwnPropertyDescriptors(object)`
- `String.prototype.padStart(padToNum <number>, padWith <string>)`
- `String.prototype.padEnd(padToNum <number>, padWith <string>)`

Trailing commas are also supported in function parameter lists, as well as in object literals

##### ES9

ES9 brings only one feature, albeit a very important one: object rest and spread operators:

```js
/* rest operator */
const { a, ...rest } = { a: 1, b: 2, c: 3 };
// a === 1
// rest === { b: 2, c: 3 };

/* spread operator */
const spread = { b: 2, c: 3 };
const o = { a: 1, ...spread };
// o === { a: 1, b: 2, c: 3 }
```

##### ESNEXT

This is where the majority of the new features come in. The term *ESNEXT* implies that these features
are still in the proposal stage, so most of them are not widely supported, not even in browsers. Let's
take a look at the most important features

###### Class fields (public, private, and static)

```js
class Foo {
    x = 5;
    #y = 10; // # symbol makes this field private
    static z = 15;
    
    getY() {
        return this.#y;
    }
}

print(Foo.z); // '15'

const f = new Foo();

print(f.z);      // 'undefined'
print(f.x);      // '5'
print(f.y);      // SyntaxError
print(f.#y);     // SyntaxError
print(f.getY()); // '10'
```

###### Optional chaining operator

```js
// The following two statements are equivalent
let result = foo?.bar?.['baz']?.();

// Note that here, foo is evaluated at most four times,
// whereas above, with optional chaining, it is only
// evaluated once
let result = foo && foo.bar && foo.bar['baz'] && foo.bar['baz']();
``` 

###### Nullish coalescing operator

```js
// The following two statements are equivalent
let result = foo ?? bar;

// Note that here, foo is evaluated at most twice,
// whereas above, with nullish coalescense, it is only
// evaluated once.
let result = (foo === null || foo === undefined) ? bar : foo;
```

###### Throw expressions

Throw statements are now expressions, rather than statements

```js
// Force parameters to be specified
function myFunction(a = throw new Error("Parameter 'a' must be specified")) {
    // Here, 'a' is guaranteed to not be 'undefined'
}

// Combine with nullish coalencing operator
const result = someFunction() ?? throw new Error("Unexpected undefined/null return value");
```

###### Pipeline operator

The pipeline operator inlines multiple chained function calls

```js
const a = 5;
const result = f1(f2(f3(5)));

// Or...

// f3 is applied first, then f2, followed by f1
const result = a |> f3 |> f2 |> f1;
```

##### Miscellaneous functions and properties

Feel free to google these functions for more details about what they do.

- `Set.prototype.intersection(set)`
- `Set.prototype.union(set)`
- `Set.prototype.difference(set)`
- `Set.prototype.symmetricDifference(set)`
- `Set.prototype.isDisjointFrom(set)`
- `Set.prototype.isSubsetOf(set)`
- `Set.prototype.isSupersetOf(set)`
- `Set.prototype.intersection(set)`
- `Map.prototype.upsert(key[, insertFn[, updateFn]])`
- `WeakMap.prototype.upsert(key[, insertFn[, updateFn]])`
- `Math.signBit(number)`
- `Math.clamp(value, min, max)`
- `Math.DEG_PER_RAD`
- `Math.RAD_PER_DEG`
- `Math.degrees(number)`
- `Math.radians(number)`
- `Math.fscale(value, min1, max1, min2, max2)`
- `Math.scale(value, min1, max1, min2, max2)`
- `Array.prototype.lastItem`
- `Array.prototype.lastIndex`
- `Map.groupBy(set, groupFn)`
- `Map.keyBy(set, keyFn)`
- `Map.prototype.deleteAll(...keys)`
- `Map.prototype.every(fn)`
- `Map.prototype.filter(fn)`
- `Map.prototype.find(fn)`
- `Map.prototype.findKey(fn)`
- `Map.prototype.includes(obj)`
- `Map.prototype.keyOf(obj)`
- `Map.prototype.mapKeys(fn)`
- `Map.prototype.mapValues(fn)`
- `Map.prototype.merge(map)`
- `Map.prototype.reduce(fn[, initialValue])`
- `Map.prototype.some(fn)`
- `Set.prototype.addAll(...items)`
- `Set.prototype.deleteAll(...items)`
- `Set.prototype.every(fn)`
- `Set.prototype.filter(fn)`
- `Set.prototype.find(fn)`
- `Set.prototype.join(string)`
- `Set.prototype.map(fn)`
- `Set.prototype.reduce(fn[, intiialValue])`
- `Set.prototype.some(fn)`
- `WeakMap.prototype.deleteAll(...keys)`
- `WeakSet.prototype.addAll(...items)`
- `WeakSet.prototype.deleteAll(...items)`

###### Class decorators

Class decorators allow one to abstract common operations into easy-to-use objects that are
similar to functions. Decorators are by far the most complex new addition to Rhino, and as such,
will not be explain fully here. Only a brief introduction will be provided. Take a look at the 
[official decorator proposal](https://github.com/tc39/proposal-decorators) for more information.

There are three built in decorators: `@wrap`, `@register`, and `@initialize`. 

###### @wrap

`@wrap` runs before a class is created, and remaps the method or class into something else. It accepts
one function, which in turn accepts one argument: The target being wrapped, which is either a class method,
or the class itself.

```js
class Foo {
    @wrap(method => () => method() * 2)
    x() {
        return 6;
    }
}

print((new Foo).x()); // '12'
```

###### @register

`@register` runs after a class is created, and receives more information about the class, as well as the thing
it is being applied to. It accepts one function, which in turn accepts two arguments: The target, which is
either the class if the decorators is applied to a static element, or the class prototype if applied to an
instance element; and the property key. The key is only passed in for public methods and fields. For classes
and private elements, the key is undefined.

```js
class Foo {
    @register((target, key) => print(`${target}: ${target[key]()}`))
    method() {
        return 6;
    }
}

// Will print '6', even will no instances of the class created
```

###### @initialize

`@initialize` is used to initialize class fields, and runs inside of the class constructor whenever the class
is instantiated. It accepts a function, which in turn accepts three arguments: the target instance (as it cannot
be applied to static elements), the property key, and its value. If the decorator is applied to a method, the value
field is set to undefined. If the decorator is applied to the class, both the property key and value will be undefined.

```js
let first = true;

class Foo {
    @initialize((target, key, value) => {
        if (first) { 
            print(`${key}: ${value}`);
            first = false;
        }
        target[key] = value;
    })
    x = 2;
}

new Foo(); // prints '2'
new Foo(); // does not print
```

###### User-defined decorators (UDDs)

Custom decorators can also be defined. They are similar to functions, but cannot contain arbitrary JavaScript. 
Instead, UDD declarations can only contain other decorators.

```js
decorator @foo(toPrint) {
    @wrap(method => () => method() * 3)
    @initialize(() => {
        print(toPrint)
    })
}

class Bar {
    @foo('hello world')
    x() {
        return 2;
    }   
}

const b = new Bar(); // prints 'hello world'
print(b.x());        // prints '6'
```
