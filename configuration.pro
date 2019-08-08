-libraryjars <java.home>/lib/rt.jar

#-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

-dontwarn org.apache.**
-dontwarn com.google.**
-dontwarn org.objectweb.**
-dontwarn net.minecraft.**
-dontwarn net.minecraftforge.**
-dontwarn javax.**
-dontwarn jdk.nashorn.**
-dontwarn org.lwjgl.**
-dontwarn paulscode.**
-dontwarn org.jetbrains.**
-dontwarn org.spongepowered.**
-dontwarn com.mojang.**
-dontwarn com.chattriggers.**

-keep class com.chattriggers.** { *; }
-keep class org.spongepowered.** { *; }
-keep class kotlin.reflect.jvm.** { *; }

-keepattributes *Annotation*

-repackageclasses 'ct'