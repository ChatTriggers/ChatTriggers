# Development Workspace Setup
Setup is a little bit more involved than just a normal Forge dev workspace setup but isn't too hard. Follow these steps and you should be ready to submit pull requests.

### 0. Set up JDK 8 and set your JAVA_HOME path variable (use google)
### 1. Clone the repository to your computer<br>
![clone repo](http://i66.tinypic.com/9jdlp5.png)
  - Open the ct.js folder that you cloned, and go through the top level directory,
  as well as the `/gradle/wrapper/` directory copying any files ending in `.default`
  and pasting them in place, but without the `.default` extension. (Ex. Copy `gradlew.default`,
  paste it, and rename it so you now have `gradlew.default` and `gradlew`)
### 2. Open PowerShell/command prompt
  - shift + right click > open PowerShell
### 3. Run gradle setup commands (*note: don't use the "./" in command prompt*)
  - `./gradlew wrapper --gradle-version 2.9`
  - `./gradlew setupDecompWorkspace`
  - `./gradlew wrapper --gradle-version 3.5`
  - `./gradlew copySrg`
  - `./gradlew idea`
### 4. Set up IntelliJ Idea
  - open the project within IDEA by clicking the recently created .ipr file<br>
![open project](http://i65.tinypic.com/2irsoyc.png)
  - right click project > open module settings<br>
![open module settings](https://i.imgur.com/F7clio5.png)
  - project > project language level > SDK default (8)<br>
![set language level](http://i66.tinypic.com/2rormrn.png)
  - file > settings<br>
![open idea settings](http://i65.tinypic.com/35bco0h.png)
  - plugins > browse all repositories > search "lombok" > install<br>
![install lombok](http://i67.tinypic.com/t8sv2p.png)
  - still in settings > Build, Execution, Deployment > Annotation Processors > Enable annotation processing<br>
![enable annotation processing](http://i66.tinypic.com/676slz.png)
  - down that page > Annotation Processor options > green +
    - Option name: `reobfSrgFile`
    - Value: `location-of-fork\mappings\mcp-srg.srg`<br>
![set annotation processing option](https://i.imgur.com/PS2t8Yc.png)
There won't be any file there currently, but as soon as you build, change versions, or run
the `./gradlew copySrg` command, it will appear.
### 5. Debug client setup
  - run > edit configuration<br>
![edit configuration](http://i65.tinypic.com/t6yq7b.png)
  - Use classpath for module > select the main ct.js (it might be named differently depending on your fork)<br>
![classpath](http://i67.tinypic.com/15z34fd.png)
  - Program arguments
    - `--tweakClass com.chattriggers.ctjs.launch.CTJSTweaker`<br>
![edit program arguments](https://i.imgur.com/UcVOq71.png)
### 6. Developing for different versions
CT uses a preprocessor to develop the mod using one codebase for multiple versions. The preprocessor used 
is the same as the one the [Replay Mod uses](https://github.com/ReplayMod/ReplayMod/blob/develop/README.md#the-preprocessor).
You can read up more information on it there. One example of this code is:

```java
//#if MC>=11200
// This is the block for MC >= 1.12.0
category.addDetail(name, callable::call);
//#else
//$$ // This is the block for MC < 1.12.0
//$$ category.setDetail(name, callable::call);
//#endif
```

Note: Code for the default target Minecraft version (1.8.9) shall be placed in the first branch of the if-else-construct.
Additionally, all version specific imports shall be placed after all normal imports, but before any static or java.* imports.

#### To switch versions
  - Run `./gradlew changeMcVersion -PminecraftVersion=<version code>` <br>
  The versions supported are listed in the `versions.txt` file, with 1.8.9 (code `10809`) being the default.
  - Next, run `./gradlew copySrg setupDecompWorkspace` to generate the new mapping files, as well as decompile
  Minecraft to the new version.
  (Side Note: If the desired version is 1.8.9, you will need to run the gradle wrapper commands,
   excluding the idea one, found in instruction #3)
  - Finally, you will need to refresh the Gradle project in IntelliJ by pressing the spinning blue arrows in the
  Gradle panel on the right hand side of your workspace.
![refresh gradle](https://i.imgur.com/QoqEX4h.png)

Make sure to change back to MC version 1.8.9 before pushing or making pull requests.

Everything should now be set up and ready to open pull requests.
