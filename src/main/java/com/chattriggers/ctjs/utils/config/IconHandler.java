package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.minecraft.libs.renderer.Image;
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.wrappers.World;
import com.chattriggers.ctjs.utils.console.Console;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IconHandler {
    private List<Icon> icons;

    public IconHandler() {
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
        int y;
        boolean main;
        Image image;
        String url;

        private Icon(String name, String image_url, String url) {
            this.y = 0;
            this.main = true;
            this.image = Renderer.image(name).download(image_url, true).setScale(0.25f);
            this.url = url;
        }

        private Icon(String name, String image_url, String url, int y) {
            this.y = y;
            this.main = false;
            this.image = Renderer.image(name).download(image_url, true).setScale(0.083f);
            this.url = url;
        }

        private void draw() {
            if (this.main) {
                this.image.setY(Renderer.screen.getHeight() - 65).draw();
            } else {
                this.image
                        .setX(65)
                        .setY(Renderer.screen.getHeight() - (this.y * 21.3f))
                        .draw();
            }
        }

        private void click(int x, int y) {
            if (x > this.image.getX() && x < this.image.getX() + 256 * this.image.getScale()
            && y > this.image.getY() && y < this.image.getY() + 256 * this.image.getScale()) {
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
