package com.chattriggers.ctjs.minecraft.mixins;

import com.chattriggers.ctjs.commands.Command;
import com.chattriggers.ctjs.minecraft.imixins.IClientCommandHandler;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.Set;

@Mixin(CommandHandler.class)
public class MixinClientCommandHandler implements IClientCommandHandler {
    @Shadow @Final
    private Set<ICommand> commandSet;
    @Shadow @Final
    private Map<String, ICommand> commandMap;

    @Override
    public void removeCTCommands() {
        commandSet.removeIf(iCommand -> iCommand instanceof Command);
        commandMap.entrySet().removeIf(entry -> entry.getValue() instanceof Command);
    }
}
