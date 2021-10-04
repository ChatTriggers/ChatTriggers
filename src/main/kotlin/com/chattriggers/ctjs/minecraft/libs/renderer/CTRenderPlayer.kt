package com.chattriggers.ctjs.minecraft.libs.renderer

import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.entity.RenderPlayer
import net.minecraft.client.renderer.entity.layers.*

//#if MC==11602
//$$ import net.minecraft.client.renderer.entity.model.PlayerModel
//$$ import net.minecraft.client.renderer.entity.model.BipedModel
//$$ import com.mojang.blaze3d.matrix.MatrixStack
//$$ import net.minecraft.client.renderer.IRenderTypeBuffer
//$$ import net.minecraft.util.text.ITextComponent
//#endif

class CTRenderPlayer(renderManager: RenderManager?, useSmallArms: Boolean) : RenderPlayer(renderManager, useSmallArms) {
    private var showNametag = true
    private var showArmor = true
    private var showCape = true
    internal var showHeldItem = true
    private var showArrows = true

    fun setOptions(
        showNametag: Boolean,
        showArmor: Boolean,
        showCape: Boolean,
        showHeldItem: Boolean,
        showArrows: Boolean
    ) {
        this.showNametag = showNametag
        this.showArmor = showArmor
        this.showCape = showCape
        this.showHeldItem = showHeldItem
        this.showArrows = showArrows

        layerRenderers.clear()

        if (showArmor)
            //#if MC==11602
            //$$ addLayer(BipedArmorLayer(this, BipedModel(0.5f), BipedModel(1.0f)))
            //#else
            addLayer(LayerBipedArmor(this))
            //#endif
        if (showHeldItem)
            addLayer(LayerHeldItem(this))
        if (showArrows)
            addLayer(LayerArrow(this))
        addLayer(LayerDeadmau5Head(this))
        if (showCape)
            addLayer(LayerCape(this))
        if (showArmor) {
            //#if MC==11602
            //$$ addLayer(HeadLayer(this))
            //#else
            addLayer(LayerCustomHead(this.getMainModel().bipedHead))
            //#endif
        }
    }

    override fun canRenderName(entity: AbstractClientPlayer?) = showNametag

    //#if MC==11602
    //$$ override fun renderName(
    //$$     entity: AbstractClientPlayerEntity,
    //$$     name: ITextComponent,
    //$$     matrixStack: MatrixStack,
    //$$     renderBuffer: IRenderTypeBuffer,
    //$$     color: Int
    //$$ ) {
    //$$     if (showNametag)
    //$$         super.renderName(entity, name, matrixStack, renderBuffer, color)
    //$$ }
    //#else
    override fun renderOffsetLivingLabel(
        entityIn: AbstractClientPlayer?,
        x: Double,
        y: Double,
        z: Double,
        str: String?,
        p_177069_9_: Float,
        p_177069_10_: Double
    ) {
        if (showNametag)
            super.renderOffsetLivingLabel(entityIn, x, y, z, str, p_177069_9_, p_177069_10_)
    }
    //#endif

    // TODO(1.16.2): It doesn't seem like 1.16 has an equivalent of these methods?
    //#if MC==10809
    override fun renderName(entity: AbstractClientPlayer?, x: Double, y: Double, z: Double) {
        if (showNametag)
            super.renderName(entity, x, y, z)
    }

    override fun renderLivingLabel(
        entityIn: AbstractClientPlayer?,
        str: String?,
        x: Double,
        y:Double,
        z: Double,
        maxDistance: Int
    ) {
        if (showNametag) super.renderLivingLabel(entityIn, str, x, y, z, maxDistance)
    }
    //#endif
}