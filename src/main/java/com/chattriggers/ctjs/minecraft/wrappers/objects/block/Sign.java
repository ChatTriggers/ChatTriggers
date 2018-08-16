package com.chattriggers.ctjs.minecraft.wrappers.objects.block;

import com.chattriggers.ctjs.minecraft.objects.message.Message;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import com.chattriggers.ctjs.minecraft.wrappers.World;
import lombok.Getter;
import net.minecraft.tileentity.TileEntitySign;

//#if MC<=10809
import net.minecraft.util.IChatComponent;
//#else
//$$ import net.minecraft.util.text.ITextComponent;
//#endif

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Sign extends Block{
    @Getter
    private TileEntitySign sign;

    /**
     * Creates a new Sign object wrapper.<br>
     * Returned with {@link Player#lookingAt()} when looking at a sign.<br>
     * Extends {@link Block}.
     *
     * @param block the {@link Block} to convert to a Sign
     */
    public Sign(Block block) {
        this.block = block.getBlock();
        this.setBlockPos(block.getBlockPos());

        this.sign = (TileEntitySign) World.getWorld().getTileEntity(this.getBlockPos());
    }

    /**
     * Gets the lines from the sign as a {@link Message} list.
     *
     * @return the {@link Message} list
     */
    public List<Message> getLines() {
        return Arrays.stream(this.sign.signText)
                .map(Message::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets the formatted lines from the sign as a String list.
     *
     * @return the String list
     */
    public List<String> getFormattedLines() {
        return Arrays.stream(this.sign.signText)
                //#if MC<=10809
                .map(IChatComponent::getFormattedText)
                //#else
                //$$ .map(ITextComponent::getFormattedText)
                //#endif
                .collect(Collectors.toList());
    }

    /**
     * Gets the unformatted lines from the sign as a String list.
     *
     * @return the String list
     */
    public List<String> getUnformattedLines() {
        return Arrays.stream(this.sign.signText)
                //#if MC<=10809
                .map(IChatComponent::getUnformattedText)
                //#else
                //$$ .map(ITextComponent::getUnformattedText)
                //#endif
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Sign{"
                + "lines="+ getLines()
                + ", name=" + this.block.getRegistryName()
                + ", x=" + getX()
                + ", y=" + getY()
                + ", z=" + getZ()
                + "}";
    }
}
