package com.chattriggers.ctjs.minecraft.libs;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import lombok.experimental.UtilityClass;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@UtilityClass
@SideOnly(Side.CLIENT)
@Deprecated
public class WorldLib {
    /**
     * Play a sound at the player location.
     *
     * @param name   the name of the sound
     * @param volume the volume of the sound
     * @param pitch  the pitch of the sound
     * @deprecated use {@link com.chattriggers.ctjs.minecraft.wrappers.World#playSound(String, float, float)}
     */
    @Deprecated
    public static void playSound(String name, float volume, float pitch) {
        CTJS.getInstance().getConsole().printDeprecatedWarning("WorldLib.playSound(String, float, float)");
        Player.getPlayer().playSound(name, volume, pitch);
    }

    /**
     * Display a title.
     *
     * @param title    title text
     * @param subtitle subtitle text
     * @param fadeIn   time to fade in
     * @param time     time to stay on screen
     * @param fadeOut  time to fade out
     * @deprecated use {@link com.chattriggers.ctjs.minecraft.wrappers.World#playSound(String, float, float)}
     */
    @Deprecated
    public static void showTitle(String title, String subtitle, int fadeIn, int time, int fadeOut) {
        CTJS.getInstance().getConsole().printDeprecatedWarning("WorldLib.showTitle(String, String, int, int, int)");
        Client.getMinecraft().ingameGUI.displayTitle(ChatLib.addColor(title), null, fadeIn, time, fadeOut);
        Client.getMinecraft().ingameGUI.displayTitle(null, ChatLib.addColor(subtitle), 0, 0, 0);
        Client.getMinecraft().ingameGUI.displayTitle(null, null, fadeIn, time, fadeOut);
    }
}
