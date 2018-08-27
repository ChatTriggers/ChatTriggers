//package com.chattriggers.ctjs.minecraft.libs;
//
//import com.chattriggers.ctjs.minecraft.libs.renderer.Image;
//import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
//import com.chattriggers.ctjs.minecraft.wrappers.Client;
//import com.chattriggers.ctjs.minecraft.wrappers.Player;
//import lombok.Getter;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.FontRenderer;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.entity.RenderManager;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import org.lwjgl.opengl.GL11;
//
////#if MC<=10809
//import net.minecraft.client.renderer.WorldRenderer;
////#else
////$$ import net.minecraft.client.renderer.BufferBuilder;
////#endif
//
//public class Tessellator {
//    @Getter
//    private static Tessellator instance;
//
//    private net.minecraft.client.renderer.Tessellator tessellator;
//    //#if MC<=10809
//    private WorldRenderer worldRenderer;
//    //#else
//    //$$ private BufferBuilder worldRenderer;
//    //#endif
//    private boolean firstVertex;
//    private boolean began;
//    private boolean colorized;
//
//    public Tessellator() {
//        instance = this;
//
//        this.tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
//        //#if MC<=10809
//        this.worldRenderer = this.tessellator.getWorldRenderer();
//        //#else
//        //$$ this.worldRenderer = this.tessellator.getBuffer();
//        //#endif
//        this.firstVertex = true;
//        this.began = false;
//        this.colorized = false;
//    }
//
//    /**
//     * Binds a texture to the client for the Tessellator to use.
//     *
//     * @param texture the texture to bind
//     * @return the Tessellator to allow for method chaining
//     */
//    public Tessellator bindTexture(Image texture) {
//        GlStateManager.bindTexture(texture.getTexture().getGlTextureId());
//
//        return this;
//    }
//
//    /**
//     * Begin drawing with the Tessellator.
//     *
//     * @param drawMode the GL draw mode
//     * @param textured if the Tessellator is textured
//     * @return the Tessellator to allow for method chaining
//     * @see com.chattriggers.ctjs.minecraft.libs.renderer.Shape#setDrawMode(int)
//     */
//    public Tessellator begin(int drawMode, boolean textured) {
//        GlStateManager.pushMatrix();
//
//        GlStateManager.enableBlend();
//        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
//
//        RenderManager renderManager = Client.getMinecraft().getRenderManager();
//        GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ);
//
//        this.worldRenderer.begin(drawMode, textured ? DefaultVertexFormats.POSITION_TEX : DefaultVertexFormats.POSITION);
//        this.firstVertex = true;
//        this.began = true;
//
//        return this;
//    }
//
//    /**
//     * Begin drawing with the Tessellator
//     * with default draw mode of quads and textured.
//     *
//     * @return the Tessellator to allow for method chaining
//     */
//    public Tessellator begin() {
//        return begin(GL11.GL_QUADS, true);
//    }
//
//    /**
//     * Colorize the Tessellator.
//     *
//     * @param red   the red value between 0 and 1
//     * @param green the green value between 0 and 1
//     * @param blue  the blue value between 0 and 1
//     * @param alpha the alpha value between 0 and 1
//     * @return the Tessellator to allow for method chaining
//     */
//    public Tessellator colorize(float red, float green, float blue, float alpha) {
//        GlStateManager.color(red, green, blue, alpha);
//        this.colorized = true;
//
//        return this;
//    }
//
//    /**
//     * Rotates the Tessellator in 3d space.
//     * Similar to {@link com.chattriggers.ctjs.minecraft.libs.renderer.Renderer#rotate(float)}
//     *
//     * @param angle the angle to rotate
//     * @param x     if the rotation is around the x axis
//     * @param y     if the rotation is around the y axis
//     * @param z     if the rotation is around the z axis
//     * @return the Tessellator to allow for method chaining
//     */
//    public Tessellator rotate(float angle, float x, float y, float z) {
//        GlStateManager.rotate(angle, x, y, z);
//
//        return this;
//    }
//
//    /**
//     * Translates the Tessellator in 3d space.
//     * Similar to {@link com.chattriggers.ctjs.minecraft.libs.renderer.Renderer#translate(float, float)}
//     *
//     * @param x the x position
//     * @param y the y position
//     * @param z the z position
//     * @return the Tessellator to allow for method chaining
//     */
//    public Tessellator translate(float x, float y, float z) {
//        GlStateManager.translate(x, y, z);
//
//        return this;
//    }
//
//    /**
//     * Scales the Tessellator in 3d space.
//     * Similar to {@link com.chattriggers.ctjs.minecraft.libs.renderer.Renderer#scale(float, float)}
//     *
//     * @param x scale in the x direction
//     * @param y scale in the y direction
//     * @param z scale in the z direction
//     * @return the Tessellator to allow for method chaining
//     */
//    public Tessellator scale(float x, float y, float z) {
//        GlStateManager.scale(x, y, z);
//
//        return this;
//    }
//
//    /**
//     * Scales the Tessellator in equally 3d space.
//     * Similar to {@link com.chattriggers.ctjs.minecraft.libs.renderer.Renderer#scale(float)}
//     *
//     * @param scale the scale
//     * @return the Tessellator to allow for method chaining
//     */
//    public Tessellator scale(float scale) {
//        return scale(scale, scale, scale);
//    }
//
//    /**
//     * Sets a new vertex in the Tessellator.
//     *
//     * @param x the x position
//     * @param y the y position
//     * @param z the z position
//     * @return the Tessellator to allow for method chaining
//     */
//    public Tessellator pos(float x, float y, float z) {
//        if (!this.began)
//            this.begin();
//        if (!this.firstVertex)
//            this.worldRenderer.endVertex();
//        this.worldRenderer.pos(x, y, z);
//        this.firstVertex = false;
//
//        return this;
//    }
//
//    /**
//     * Sets the texture location on the last defined vertex.
//     * Use directly after using {@link Tessellator#pos(float, float, float)}
//     *
//     * @param u the u position in the texture
//     * @param v the v position in the texture
//     * @return the Tessellator to allow for method chaining
//     */
//    public Tessellator tex(float u, float v) {
//        this.worldRenderer.tex(u, v);
//
//        return this;
//    }
//
//    /**
//     * Finalizes and draws the Tessellator.
//     */
//    public void draw() {
//        if (!began) return;
//
//        this.worldRenderer.endVertex();
//
//        if (!this.colorized)
//            colorize(1F, 1F, 1F, 1F);
//
//        this.tessellator.draw();
//        this.began = false;
//        this.colorized = false;
//
//        GlStateManager.disableBlend();
//
//        GlStateManager.popMatrix();
//    }
//
//    /**
//     * Renders floating lines of text in the 3D world at a specific position.
//     *
//     * @param text           The string array of text to render
//     * @param x              X coordinate in the game world
//     * @param y              Y coordinate in the game world
//     * @param z              Z coordinate in the game world
//     * @param renderBlackBox render a pretty black border behind the text
//     * @param partialTicks   the partial ticks of the current render pass, passed in through the world render trigger
//     * @param scale          the scale of the text
//     * @param color          the color of the text
//     * @param increase       whether or not to scale the text up as the player moves away
//     */
//    public void drawString(String text, float x, float y, float z, boolean renderBlackBox, float partialTicks, float scale, int color, boolean increase) {
//        Minecraft mc = Minecraft.getMinecraft();
//
//        RenderManager renderManager = mc.getRenderManager();
//        FontRenderer fontRenderer = Renderer.getFontRenderer();
//
//        float playerX = (float) (Player.getPlayer().lastTickPosX + (Player.getPlayer().posX - Player.getPlayer().lastTickPosX) * partialTicks);
//        float playerY = (float) (Player.getPlayer().lastTickPosY + (Player.getPlayer().posY - Player.getPlayer().lastTickPosY) * partialTicks);
//        float playerZ = (float) (Player.getPlayer().lastTickPosZ + (Player.getPlayer().posZ - Player.getPlayer().lastTickPosZ) * partialTicks);
//
//        float dx = x - playerX;
//        float dy = y - playerY;
//        float dz = z - playerZ;
//
//        if (increase) {
//            float distance = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
//            float multiplier = distance / 120f; //mobs only render ~120 blocks away
//            scale *= (0.45f * multiplier);
//        }
//
//        GL11.glColor4f(1f, 1f, 1f, 0.5f);
//        GL11.glPushMatrix();
//        GL11.glTranslatef(dx, dy, dz);
//        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
//        GL11.glScalef(-scale, -scale, scale);
//        GL11.glDisable(GL11.GL_LIGHTING);
//        GL11.glDepthMask(false);
//        GL11.glDisable(GL11.GL_DEPTH_TEST);
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//
//        int textWidth = fontRenderer.getStringWidth(text);
//
//        if (renderBlackBox) {
//            int j = textWidth / 2;
//            GlStateManager.disableTexture2D();
//            worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
//            worldRenderer.pos((double) (-j - 1), (double) (-1), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
//            worldRenderer.pos((double) (-j - 1), (double) (8), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
//            worldRenderer.pos((double) (j + 1), (double) (8), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
//            worldRenderer.pos((double) (j + 1), (double) (-1), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
//            tessellator.draw();
//            GlStateManager.enableTexture2D();
//        }
//
//        fontRenderer.drawString(text, -textWidth / 2, 0, color);
//
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glDepthMask(true);
//        GL11.glEnable(GL11.GL_DEPTH_TEST);
//        GL11.glPopMatrix();
//    }
//}