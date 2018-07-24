package com.chattriggers.ctjs.minecraft.wrappers.objects.block;

import com.chattriggers.ctjs.minecraft.objects.message.Message;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import com.chattriggers.ctjs.minecraft.wrappers.World;
import lombok.Getter;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;

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
    public ArrayList<Message> getLines() {
        ArrayList<Message> lines = new ArrayList<>();
        for (IChatComponent component : this.sign.signText) {
            lines.add(new Message(component));
        }
        return lines;
    }

    /**
     * Gets the formatted lines from the sign as a String list.
     *
     * @return the String list
     */
    public ArrayList<String> getFormattedLines() {
        ArrayList<String> lines = new ArrayList<>();
        for (IChatComponent component : this.sign.signText) {
            lines.add(component.getFormattedText());
        }
        return lines;
    }

    /**
     * Gets the unformatted lines from the sign as a String list.
     *
     * @return the String list
     */
    public ArrayList<String> getUnformattedLines() {
        ArrayList<String> lines = new ArrayList<>();
        for (IChatComponent component: this.sign.signText) {
            lines.add(component.getUnformattedText());
        }
        return lines;
    }

    @Override
    public String toString() {
        return "Sign{"
                + "lines="+getLines()
                + ", name=" + this.block.getRegistryName()
                + ", x=" + getX()
                + ", y=" + getY()
                + ", z=" + getZ()
                + "}";
    }
}
