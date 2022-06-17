package com.chattriggers.ctjs.minecraft.libs.renderer

import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.renderer.entity.RenderPlayer
import net.minecraft.client.renderer.entity.layers.LayerArrow
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor
import net.minecraft.client.renderer.entity.layers.LayerCape
import net.minecraft.client.renderer.entity.layers.LayerCustomHead
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head
import net.minecraft.client.renderer.entity.layers.LayerHeldItem

//#if MC<=11202
import net.minecraft.client.renderer.entity.RenderManager
//#else
//$$ import com.mojang.blaze3d.vertex.PoseStack
//$$ import net.minecraft.client.renderer.entity.layers.BeeStingerLayer
//$$ import net.minecraft.client.renderer.entity.layers.ElytraLayer
//$$ import net.minecraft.client.renderer.entity.layers.ParrotOnShoulderLayer
//$$ import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer
//$$ import net.minecraft.client.model.HumanoidModel
//$$ import net.minecraft.client.model.geom.ModelLayers
//$$ import net.minecraft.client.renderer.MultiBufferSource
//$$ import net.minecraft.client.renderer.entity.EntityRendererProvider
//$$ import net.minecraft.network.chat.Component
//#endif

//#if MC<=11202
internal class CTRenderPlayer(renderManager: RenderManager, useSmallArms: Boolean) : RenderPlayer(renderManager, useSmallArms) {
//#else
//$$ internal class CTRenderPlayer(
//$$     private val context: EntityRendererProvider.Context,
//$$     private val useSmallArms: Boolean,
//$$ ) : PlayerRenderer(context, useSmallArms) {
//#endif
    var showNametag = true
        private set
    var showArmor = true
        private set
    var showCape = true
        private set
    var showHeldItem = true
        private set
    var showArrows = true
        private set
    //#if MC>=11701
    //$$ var showParrot = true
    //$$     private set
    //$$ var showSpinAttackEffect = true
    //$$     private set
    //$$ var showBeeStinger = true
    //$$     private set
    //#endif

    fun setOptions(
        showNametag: Boolean,
        showArmor: Boolean,
        showCape: Boolean,
        showHeldItem: Boolean,
        showArrows: Boolean,
        //#if MC>=11701
        //$$ showParrot: Boolean,
        //$$ showSpinAttack: Boolean,
        //$$ showBeeStinger: Boolean,
        //#endif
    ) {
        this.showNametag = showNametag
        this.showArmor = showArmor
        this.showCape = showCape
        this.showHeldItem = showHeldItem
        this.showArrows = showArrows

        //#if MC<=11202
        layerRenderers.clear()

        if (showArmor)
            addLayer(LayerBipedArmor(this))
        if (showHeldItem)
            addLayer(LayerHeldItem(this))
        if (showArrows)
            addLayer(LayerArrow(this))
        addLayer(LayerDeadmau5Head(this))
        if (showCape)
            addLayer(LayerCape(this))
        if (showArmor)
            addLayer(LayerCustomHead(getMainModel().bipedHead))

        //#else
        //#if FORGE
        //$$ layers.clear()
        //#else
        //$$ features.clear()
        //#endif
        //$$
        //$$ if (showArmor) {
        //$$     addLayer(
        //$$         HumanoidArmorLayer(
        //$$             this,
        //$$             HumanoidModel(context.bakeLayer(if (useSmallArms) ModelLayers.PLAYER_SLIM_INNER_ARMOR else ModelLayers.PLAYER_INNER_ARMOR)),
        //$$             HumanoidModel(context.bakeLayer(if (useSmallArms) ModelLayers.PLAYER_SLIM_OUTER_ARMOR else ModelLayers.PLAYER_OUTER_ARMOR))
        //$$         )
        //$$     )
        //$$ }
        //$$
        //$$ if (showHeldItem)
        //$$     addLayer(PlayerItemInHandLayer(this))
        //$$ if (showArrows)
        //$$     addLayer(ArrowLayer(context, this))
        //$$ if (showArmor)
        //$$     addLayer(Deadmau5EarsLayer(this))
        //$$ if (showCape)
        //$$     addLayer(CapeLayer(this))
        //$$ if (showArmor) {
        //$$     addLayer(CustomHeadLayer(this, context.modelSet))
        //$$     addLayer(ElytraLayer(this, context.modelSet))
        //$$ }
        //$$ if (showParrot)
        //$$     addLayer(ParrotOnShoulderLayer(this, context.modelSet))
        //$$ if (showSpinAttackEffect)
        //$$     addLayer(SpinAttackEffectLayer(this, context.modelSet))
        //$$ if (showBeeStinger)
        //$$     addLayer(BeeStingerLayer(this))
        //#endif
    }

    //#if MC<=11202
    override fun canRenderName(entity: AbstractClientPlayer) = showNametag

    override fun renderOffsetLivingLabel(
        entityIn: AbstractClientPlayer,
        x: Double,
        y: Double,
        z: Double,
        str: String,
        p_177069_9_: Float,
        p_177069_10_: Double
    ) {
        if (showNametag) super.renderOffsetLivingLabel(entityIn, x, y, z, str, p_177069_9_, p_177069_10_)
    }

    override fun renderName(entity: AbstractClientPlayer, x: Double, y: Double, z: Double) {
        if (showNametag) super.renderName(entity, x, y, z)
    }

    override fun renderLivingLabel(
        entityIn: AbstractClientPlayer,
        str: String,
        x: Double,
        y: Double,
        z: Double,
        maxDistance: Int
    ) {
        if (showNametag) super.renderLivingLabel(entityIn, str, x, y, z, maxDistance)
    }
    //#else
    //#if FORGE
    //$$ override fun shouldShowName(arg: AbstractClientPlayer): Boolean {
    //#else
    //$$ override fun hasLabel(arg: AbstractClientPlayerEntity): Boolean {
    //#endif
    //$$     return showNametag
    //$$ }
    //$$
    //#if FORGE
    //$$ override fun renderNameTag(
    //#else
    //$$ override fun renderLabelIfPresent(
    //#endif
    //$$     arg: AbstractClientPlayer,
    //$$     arg2: Component,
    //$$     arg3: PoseStack,
    //$$     arg4: MultiBufferSource,
    //$$     i: Int
    //$$ ) {
    //$$     if (showNametag)
    //$$         super.renderNameTag(arg, arg2, arg3, arg4, i)
    //$$ }
    //#endif
}
