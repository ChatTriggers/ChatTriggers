package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.minecraft.libs.renderer.Image;
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.wrappers.World;
import com.chattriggers.ctjs.utils.console.Console;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class IconHandler {
    private List<Icon> icons;

    IconHandler() {
        this.icons = new ArrayList<>();

        String imageUrl = "https://chattriggers.com/assets/images/";

        this.icons.add(new Icon("CT_logo.png", imageUrl + "logo-icon.png", "https://chattriggers.com/"));
        this.icons.add(new Icon("CT_Patreon.png", imageUrl + "Patreon-dark.png", "https://www.patreon.com/ChatTriggers", 1));
        this.icons.add(new Icon("CT_Github.png", imageUrl + "github-dark.png", "https://github.com/ChatTriggers/ct.js", 2));
        this.icons.add(new Icon("CT_Discord.png", imageUrl + "discord-dark.png", "https://discordapp.com/invite/0fNjZyopOvBHZyG8", 3));
    }

    public void drawIcons() {
        for (Icon icon : icons) {
            icon.draw();
        }
    }

    public void clickIcons(int x, int y) {
        for (Icon icon : icons) {
            icon.click(x, y);
        }
    }

    private class Icon {
        private int y;
        private boolean main;
        private Image image;
        private String url;

        private Icon(String name, String image_url, String url) {
            this.y = 0;
            this.main = true;
            this.image = Image.load(name, image_url);
            this.url = url;
        }

        private Icon(String name, String image_url, String url, int y) {
            this.y = y;
            this.main = false;
            this.image = Image.load(name, image_url);
            this.url = url;
        }

        private void draw() {
            if (this.main) {
                this.image.draw(0, Renderer.screen.getHeight() - 65, 64);
            } else {
                this.image.draw(65, (int) (Renderer.screen.getHeight() - (this.y * 21.3f)), 64 / 3);
            }
        }

        private void click(int x, int y) {
            float ix, iy, size;

            if (this.main) {
                ix = 0;
                iy = Renderer.screen.getHeight() - 65;
                size = 64;
            } else {
                ix = 65;
                iy = Renderer.screen.getHeight() - (this.y * 21.3f);
                size = 64 / 3;
            }

            if (x > ix && x < ix + size && y > iy && y < iy + size) {
                try {
                    Desktop.getDesktop().browse(new URL(this.url).toURI());
                    World.playSound("gui.button.press", 100, 1);
                } catch (Exception exception) {
                    Console.getConsole().printStackTrace(exception);
                }
            }
        }
    }
}
