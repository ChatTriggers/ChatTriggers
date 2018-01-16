package com.chattriggers.ctjs.minecraft.wrappers;

import com.chattriggers.ctjs.minecraft.libs.MinecraftVars;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;

public class Potions {
    public static PotionEffect[] getPotionEffects() {
        if (MinecraftVars.getPlayer() == null) return new PotionEffect[]{};

        ArrayList<PotionEffect> effects = new ArrayList<>();
        for (PotionEffect effect : MinecraftVars.getPlayer().getActivePotionEffects()) {
            effects.add(effect);
        }
        return effects.toArray(new PotionEffect[effects.size()]);
    }
}
