package com.chattriggers.ctjs.launch.mixins.transformers;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Scoreboard.class)
public interface ScoreboardAccessor {
    @Accessor
    Map<String, Map<ScoreboardObjective, ScoreboardPlayerScore>> getPlayerObjectives();
}
