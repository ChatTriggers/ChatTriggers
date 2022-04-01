# Development Workspace Setup
Set up the repository as you would any normal Forge project,
import into your IDE of choice (IntelliJ is our preference), run the decompile task,
and set up your run configurations.

ChatTriggers uses a custom loading plugin, and as such, requires modifying the
run configuration in your development environment. 
Add `-Dfml.coreMods.load=dev.falsehonesty.asmhelper.core.AsmHelperLoadingPlugin` to your list of
VM Options. Additionally, the run configuration needs to be modified to get Essential working.
Add `--tweakClass gg.essential.loader.stage0.EssentialSetupTweaker` to the program arguments.


# Developing for different versions
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
  Gradle panel on the right hand side of your workspace.<br>
![refresh gradle](https://i.imgur.com/QoqEX4h.png)

Make sure to change back to MC version 1.8.9 before pushing or making pull requests.

Everything should now be set up and ready to open pull requests.
