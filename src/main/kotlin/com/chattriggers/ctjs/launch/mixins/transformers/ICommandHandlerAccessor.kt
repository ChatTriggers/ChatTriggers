package com.chattriggers.ctjs.launch.mixins.transformers

//#if MC==10809
import net.minecraft.command.CommandHandler
import net.minecraft.command.ICommand
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(CommandHandler::class)
interface ICommandHandlerAccessor {
    @Accessor
    fun getCommandMap(): MutableMap<String, ICommand>

    @Accessor
    fun getCommandSet(): MutableSet<ICommand>
}

fun CommandHandler.asMixinAccessor() = this as ICommandHandlerAccessor
//#endif
