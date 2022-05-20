package com.chattriggers.ctjs.launch.mixins.transformers;

//#if MC<=11202
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.Set;

@Mixin(CommandHandler.class)
public interface CommandHandlerAccessor {
    @Accessor
    Map<String, ICommand> getCommandMap();

    @Accessor
    Set<ICommand> getCommandSet();
}
//#endif
