package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.PlayerEntityRenderer
import net.minecraft.client.render.entity.feature.*
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

class CTPlayerEntityRenderer(
    private val ctx: EntityRendererFactory.Context,
    private val slim: Boolean,
) : PlayerEntityRenderer(ctx, slim) {
    private var showNametag = true
    private var showArmor = true
    private var showCape = true
    private var showHeldItem = true
    private var showArrows = true

    fun setOptions(
        showNametag: Boolean,
        showArmor: Boolean,
        showCape: Boolean,
        showHeldItem: Boolean,
        showArrows: Boolean
    ) {
        this.showNametag = showNametag

        if (showArmor != this.showArmor) {
            this.showArmor = showArmor
            if (this.showArmor) {
                addFeature(ArmorFeatureRenderer(
                    this,
                    BipedEntityModel(ctx.getPart(if (slim) EntityModelLayers.PLAYER_SLIM_INNER_ARMOR else EntityModelLayers.PLAYER_INNER_ARMOR)),
                    BipedEntityModel(ctx.getPart(if (slim) EntityModelLayers.PLAYER_SLIM_OUTER_ARMOR else EntityModelLayers.PLAYER_OUTER_ARMOR))
                ))
            } else {
                features.removeIf { it is ArmorFeatureRenderer<*, *, *> }
            }
        }

        if (showCape != this.showCape) {
            this.showCape = showCape
            if (this.showCape) {
                addFeature(CapeFeatureRenderer(this))
            } else {
                features.removeIf { it is CapeFeatureRenderer }
            }
        }

        if (showHeldItem != this.showHeldItem) {
            this.showHeldItem = showHeldItem
            if (this.showHeldItem) {
                addFeature(PlayerHeldItemFeatureRenderer(this))
            } else {
                features.removeIf { it is PlayerHeldItemFeatureRenderer }
            }
        }

        if (showArrows != this.showArrows) {
            this.showArrows = showArrows
            if (this.showArrows) {
                addFeature(StuckArrowsFeatureRenderer(ctx, this))
            } else {
                features.removeIf { it is StuckArrowsFeatureRenderer<*, *> }
            }
        }
    }

    override fun hasLabel(livingEntity: AbstractClientPlayerEntity?) = showNametag

    override fun renderLabelIfPresent(
        abstractClientPlayerEntity: AbstractClientPlayerEntity?,
        text: Text?,
        matrixStack: MatrixStack?,
        vertexConsumerProvider: VertexConsumerProvider?,
        i: Int
    ) {
        if (showNametag)
            super.renderLabelIfPresent(abstractClientPlayerEntity, text, matrixStack, vertexConsumerProvider, i)
    }
}
