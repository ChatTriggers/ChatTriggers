package com.chattriggers.ctjs.minecraft.objects

import com.chattriggers.ctjs.minecraft.libs.EventLib
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.utils.kotlin.KotlinListener
import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@KotlinListener
object CPS {
    private var sysTime = Client.getSystemTime()

    private var leftClicks = mutableListOf<Int>()
    private var rightClicks = mutableListOf<Int>()
    private var leftClicksAverage = mutableListOf<Double>()
    private var rightClicksAverage = mutableListOf<Double>()

    private var leftClicksMax = 0
    private var rightClicksMax = 0

    init { MinecraftForge.EVENT_BUS.register(this) }
    @SubscribeEvent
    private fun update(event: RenderGameOverlayEvent) = clickCalc()
    @SubscribeEvent
    private fun click(event: MouseEvent) {
        if (EventLib.getButtonState(event)) {
            when (EventLib.getButton(event)) {
                0 -> CPS.addLeftClicks()
                1 -> CPS.addRightClicks()
            }
        }
    }

    @JvmStatic fun getLeftClicksMax() = this.leftClicksMax
    @JvmStatic fun getRightClicksMax() = this.rightClicksMax

    @JvmStatic fun getLeftClicks() = this.leftClicks.size
    @JvmStatic fun getRightClicks() = this.rightClicks.size

    @JvmStatic
    fun getLeftClicksAverage(): Int {
        if (this.leftClicksAverage.isEmpty()) return 0

        var clicks = 0.0
        for (click in this.leftClicksAverage) clicks += click
        return Math.round(clicks / this.leftClicksAverage.size).toInt()
    }

    @JvmStatic
    fun getRightClicksAverage(): Int {
        if (this.rightClicksAverage.isEmpty()) return 0

        var clicks = 0.0
        for (click in this.rightClicksAverage) clicks += click
        return Math.round(clicks / this.rightClicksAverage.size).toInt()
    }

    private
    fun clickCalc() {
        while (Client.getSystemTime() > sysTime + 50L) {
            sysTime += 50L

            decreaseClicks(leftClicks)
            decreaseClicks(rightClicks)

            leftClicksAverage.add(leftClicks.size.toDouble())
            rightClicksAverage.add(rightClicks.size.toDouble())

            limitAverage(leftClicksAverage)
            limitAverage(rightClicksAverage)

            clearOldLeft()
            clearOldRight()
        }
        findMax()
    }

    private fun limitAverage(average: MutableList<Double>) {
        if (average.size > 100) average.removeAt(0)
    }

    private fun clearOldLeft() {
        if (leftClicksAverage.isNotEmpty() && leftClicksAverage[leftClicksAverage.size - 1] == 0.0) {
            leftClicksAverage.clear()
            leftClicksMax = 0
        }
    }

    private fun clearOldRight() {
        if (rightClicksAverage.isNotEmpty() && rightClicksAverage[rightClicksAverage.size - 1] == 0.0) {
            rightClicksAverage.clear()
            rightClicksMax = 0
        }
    }

    private fun findMax() {
        if (leftClicks.size > leftClicksMax) leftClicksMax = leftClicks.size
        if (rightClicks.size > rightClicksMax) rightClicksMax = rightClicks.size
    }

    private fun addLeftClicks() = leftClicks.add(20)
    private fun addRightClicks() = rightClicks.add(20)

    private fun decreaseClicks(clicks: MutableList<Int>) {
        if (clicks.isNotEmpty()) {
            for (i in clicks) {
                clicks[i] = clicks[i] - 1
                if (clicks[i] == 0) clicks.removeAt(i)
            }
        }
    }
}