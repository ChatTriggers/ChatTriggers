<div align="center">
  <p>
    <a href="https://chattriggers.com">
      <img src="https://chattriggers.com/assets/images/logo-final.png" width="546" alt="ChatTriggers.js" />
    </a>
  </p>
  <p>
    <a href="https://discord.gg/chattriggers">
      <img src="https://discordapp.com/api/guilds/119493402902528000/embed.png" alt="Discord" />
    </a>
    <a href="https://travis-ci.org/ChatTriggers/ChatTriggers">
      <img src="https://api.travis-ci.com/ChatTriggers/ChatTriggers.svg?branch=master" alt="Build Status" />
    </a>
    <a href="https://github.com/ChatTriggers/ChatTriggers/releases">
      <img src="https://img.shields.io/github/v/release/ChatTriggers/ChatTriggers.svg?include_prereleases" alt="Releases" />
    </a>
  </p>
</div>

[![forthebadge](https://forthebadge.com/images/badges/60-percent-of-the-time-works-every-time.svg)](https://forthebadge.com)<br>
ChatTriggers is always in development. Although we try to follow standard version conventions 
and deprecate old methods before removing them, there may be times where we need to restructure
 something to the point where deprecation is impossible. Modules can and will break at any time.

# About
ChatTriggers is a framework for Minecraft that allows for live scripting and client modification
using JavaScript. We provide libraries, wrappers, objects and more to make your life as a modder
as easy as possible. Even without the proper wrapper, you can still use exposed Minecraft methods
and variables but you will need knowledge of FML mappings. The entire ChatTriggers engine is built
on [Rhino](https://developer.mozilla.org/en-US/docs/Mozilla/Projects/Rhino) using Java 8
so you have access to any Rhino methods and functions. Rhino supports _some_ ES6 features,
as can be seen [here](https://mozilla.github.io/rhino/compat/engines.html).

The basic premise of ChatTriggers is that everything is based around Triggers.
From a modding standpoint, Triggers can be thought of as event listeners.
These can range from a chat Trigger that runs on a specific chat event matching criteria to a
render overlay Trigger that runs when the crosshair is being rendered. We are constantly adding
more Triggers and Trigger types as the mod evolves for more integration with Minecraft.

```JavaScript
register("chat", function(name, message, event) {
  cancel(event);
  ChatLib.chat(name + ": " + message);
}).setCriteria("<${name}> ${message}");

register("renderCrosshair", function(event) {
  cancel(event);
  Renderer.drawRect(
    0x50ffffff,
    Renderer.screen.getWidth() / 2 - 1,
    Renderer.screen.getHeight() / 2 - 1,
    2, 2
  );
});
```

You can learn the basics of scripting with ChatTriggers from the [Slate tutorial](https://www.chattriggers.com/slate/) and once you get the basics, check out the [JavaDocs](https://www.chattriggers.com/javadocs/) for a more in depth look at all of the available methods. 

# Releases
The [ChatTriggers website](https://www.chattriggers.com/) will always be kept up to date with the latest release. As of beta version 0.6.4, we have started to move the release changelog along with a .jar download (mirror of the website) to the [GitHub releases page](https://github.com/ChatTriggers/ChatTriggers/releases).

# Feature changes
Any major features are moved to separate branches before being merged into master. This will avoid the issue of waiting after a release to fix bugs when we want to be working on new features. Any contributors will have to abide by the same standard. New features get their own branch and bug fixes require a pull request on the master branch.

# Issues
Any issue can be opened using the normal [GitHub issue page](https://github.com/ChatTriggers/ChatTriggers/issues). Issues can be anything from bug reports to feature requests. For us to consider an issue to be valid, it needs a simple but effective title that conveys the problem in a few words and a well thought out and well written description.
### Bug Report
- Should be reproducible
- Needs a step by step guide on how to reproduce
- Any evidence of the bug occurring (e.g. images or video) is welcome
### Feature Request
- Needs a general description of the feature requests
- Specifics on what is being requested (e.g. what class you want it in or what it should do) is highly recommended

Duplicate issues will be merged to avoid too much clutter. If an issue is moved to "next" we will usually comment on it to explain how we expect to implement or fix that issue.


## Special Thanks To

<a href="https://www.ej-technologies.com/products/jprofiler/overview.html">
  <img src="https://www.ej-technologies.com/images/product_banners/jprofiler_large.png" alt="jProfiler" />
  The Java Profiler jProfiler
</a>
