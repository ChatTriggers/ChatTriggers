# ct.js [![Build Status](https://travis-ci.org/ChatTriggers/ct.js.svg?branch=master)](https://travis-ci.org/ChatTriggers/ct.js) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/f3bccfe6845d4f6b8733c3948314ea95)](https://www.codacy.com/app/FalseHonesty/ct.js?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ChatTriggers/ct.js&amp;utm_campaign=Badge_Grade)
### *Beta*
CTjs is currently in early beta. Although we try to follow standard version conventions and deprecate old method before removing them, there may be times where we need to restructure something to the point where deprecation is impossible. Modules can and will break at any  time.

# About
CTjs is a framework for minecraft that allows for live scripting and client modification using JavaScript. We provide libraries, wrappers, objects and more to make your life as a modder as easy as possible. Even without the proper wrapper, you can still use exposed Minecraft methods and variables but you will need knowledge of FML mappings. The entire CTjs engine is built on Nashorn using Java 8 so you have access to any Nashorn methods and functions and up to ES5 support. As soon as the Minecraft launcher supports a higher version of Java, ES6 will also be supported.

The basic premise of CTjs is that everything is based around Triggers. From a modding standpoint, Triggers can be thought of as event listeners. These can range from a chat Trigger that runs on a specific chat event matching criteria to a render overlay Trigger that runs when the crosshair is being rendered. We are constantly adding more Triggers and Trigger types as the mod evolves for more integration with Minecraft.
```JavaScript
register("chat", "chatFunction").setChatCriteria("<${name}> ${message}");
function chatFunction(name, message, event) {
  cancel(event);
  chat("${name}: ${message}");
}

register("renderCrosshair", "crosshairFunction");
function crosshairFunction(event) {
  cancel(event);
  RenderLib.rectangle(
    0x50ffffff,
    RenderLib.screen.getWidth()/2 - 1,
    RenderLib.screen.getHeight()/2 -1,
    2, 2
  ).draw();
}
```

You can learn the basics of scripting with CTjs from the [Slate tutorial](https://www.chattriggers.com/slate/) and once you get the basics, check out the [JavaDocs](https://www.chattriggers.com/javadocs/) for a more in depth look at all of the available methods. 

# Releases
The [ChatTriggers website](https://www.chattriggers.com/) will always be kept up to date with the latest release. As of beta version 0.6.4, we have started to move the release changelog along with a .jar download (mirror of the website) to the [GitHub releases page](https://github.com/ChatTriggers/ct.js/releases).

# Feature changes
Starting in beta 0.10, we are moving feature changes to seperate branches labeled as such. This will avoid the issue of waiting after a release to fix bugs when we want to be working on new features. Any contributors will have to abide by the same standard. New features get their own branch and bug fixes require a pull request on the master branch.

# Issues
Any issue can be opened using the normal [GitHub issue page](https://github.com/ChatTriggers/ct.js/issues). Issues can be anything from bug reports to feature requests. For us to consider an issue to be valid, its needs a simple, but effective title that conveys the problem in a few words and a well thought out and well written description.
### Bug Report
- should be reproducable 
- needs a step by step guide on how to reproduce
- any evidence of the bug occuring (e.g. images or video) is welcome
### Feature Request
- needs a general description of the feature requestes
- specifics on what is being requested (e.g. what class you want it in or what it should do) is highly recommended

Duplicate issues will be merged to avoid too much clutter. If an issue is moved to "next" we will usually comment on it to explain how we expect to implement or fix that issue.

# Development Workspace Setup
Setup is a little bit more involved than just a normal Forge dev workspace setup but isn't too hard. Follow these steps and you should be ready to submit pull requests.

### 0. Set up JDK 8 and set your JAVA_HOME path variable (use google)
### 1. Clone the repository to your computer<br>
![clone repo](http://i66.tinypic.com/9jdlp5.png)
### 2. Open PowerShell/command prompt
  - shift + right click > open PowerShell
### 3. Run gradle setup commands
  - cmd         `gradlew setupDecompWorkspace`
  - PowerShell  `./gradlew setupDecompWorkspace`
  - cmd         `gradlew idea`
  - PowerShell  `./gradlew idea`
### 4. Set up intellij idea
  - right click project > open module settings<br>
![open module settings](https://i.imgur.com/F7clio5.png)
  - project > project language level > SDK default (8)<br>
![set language level](http://i66.tinypic.com/2rormrn.png)
  - file > settings<br>
![open idea settings](http://i65.tinypic.com/35bco0h.png)
  - plugins > brows all repositories > search "lombok" > install<br>
![install lombok](http://i67.tinypic.com/t8sv2p.png)
  - still in settings > Build, Execution, Deployment > Annotation Processors > Enable annotation processing
![enable annotation processing](http://i66.tinypic.com/676slz.png)
### 5. Debug client setup
  - run > edit configuration<br>
![edit configuration](http://i65.tinypic.com/t6yq7b.png)
  - Use classpath for module > select the main ct.js (it might be named differently depending on your fork)<br>
![classpath](http://i67.tinypic.com/15z34fd.png)

Everything should be set up and ready to open pull requests
