package com.chattriggers.ctjs.minecraft.wrappers.objects.block;

import com.chattriggers.ctjs.minecraft.objects.message.Message;
import com.chattriggers.ctjs.minecraft.wrappers.World;
import lombok.Getter;
import net.minecraft.block.BlockSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;

public class Sign extends Block{
    @Getter
    private TileEntitySign sign;
    
    public Sign(BlockSign sign, BlockPos pos) {
        this.block = sign;
        this.setBlockPos(pos);

        this.sign = (TileEntitySign) World.getWorld().getTileEntity(pos);
    }

    public ArrayList<Message> getLines() {
        ArrayList<Message> lines = new ArrayList<>();
        for (IChatComponent component : this.sign.signText) {
            lines.add(new Message(component));
        }
        return lines;
    }

    public ArrayList<String> getFormattedLines() {
        ArrayList<String> lines = new ArrayList<>();
        for (IChatComponent component : this.sign.signText) {
            lines.add(component.getFormattedText());
        }
        return lines;
    }

    public ArrayList<String> getUnformattedLines() {
        ArrayList<String> lines = new ArrayList<>();
        for (IChatComponent component: this.sign.signText) {
            lines.add(component.getUnformattedText());
        }
        return lines;
    }

    public Message getLine(int i) {
        if (this.sign.signText[i] == null) return new Message();
        return new Message(this.sign.signText[i]);
    }

    public String getFormattedLine(int i) {
        if (this.sign.signText[i] == null) return "";
        return this.sign.signText[i].getFormattedText();
    }

    public String getUnformattedLine(int i) {
        if (this.sign.signText[i] == null) return "";
        return this.sign.signText[i].getUnformattedText();
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
