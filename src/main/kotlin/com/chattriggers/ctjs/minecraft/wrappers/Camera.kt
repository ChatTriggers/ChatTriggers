package com.chattriggers.ctjs.minecraft.wrappers

/**
 * Used from [Client.camera] to get the current camera properties
 */
class Camera {
    private val renderManager = Client.getMinecraft().renderManager

    /**
     * @return the X position of the camera
     */
    fun getX(): Double = renderManager.viewerPosX

    /**
     * @return the Y position of the camera
     */
    fun getY(): Double = Client.getMinecraft().renderManager.viewerPosY

    /**
     * @return the Z position of the camera
     */
    fun getZ(): Double = Client.getMinecraft().renderManager.viewerPosZ
}