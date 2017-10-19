package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.Reference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ConfigGui extends GuiConfig {
    public ConfigGui(GuiScreen parentScreen) {
        super(parentScreen,
                new ConfigElement(
                    CTJS.getInstance().getConfig().getConfig().getCategory("ct")
                ).getChildElements(),
                Reference.MODID,
                false,
                false,
                "ChatTriggers"
        );
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
    }
}
