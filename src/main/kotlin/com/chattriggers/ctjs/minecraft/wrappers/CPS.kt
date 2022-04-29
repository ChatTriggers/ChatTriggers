package com.chattriggers.ctjs.minecraft.wrappers

import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.roundToInt

object CPS {
    private var sysTime = Client.getSystemTime()

    private var leftClicks = mutableListOf<Int>()
    private var rightClicks = mutableListOf<Int>()
    private var leftClicksAverage = mutableListOf<Double>()
    private var rightClicksAverage = mutableListOf<Double>()

    private var leftClicksMax = 0
    private var rightClicksMax = 0

    @SubscribeEvent
    fun update(event: RenderGameOverlayEvent.Pre) {
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

            findMax()
        }
    }

    @SubscribeEvent
    fun click(event: MouseEvent) {
        if (event.buttonstate) {
            when (event.button) {
                0 -> leftClicks.add(20)
                1 -> rightClicks.add(20)
            }
        }
    }

    @JvmStatic
    fun getLeftClicksMax(): Int = leftClicksMax

    @JvmStatic
    fun getRightClicksMax(): Int = rightClicksMax

    @JvmStatic
    fun getLeftClicks(): Int = leftClicks.size

    @JvmStatic
    fun getRightClicks(): Int = rightClicks.size

    @JvmStatic
    fun getLeftClicksAverage(): Int {
        if (leftClicksAverage.isEmpty()) return 0

        var clicks = 0.0
        for (click in leftClicksAverage) clicks += click
        return (clicks / leftClicksAverage.size).roundToInt()
    }

    @JvmStatic
    fun getRightClicksAverage(): Int {
        if (rightClicksAverage.isEmpty()) return 0

        var clicks = 0.0
        for (click in rightClicksAverage) clicks += click
        return (clicks / rightClicksAverage.size).roundToInt()
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

    private fun decreaseClicks(clicks: MutableList<Int>) {
        if (clicks.isNotEmpty()) {
            val toRemove = mutableListOf<Int>()

            for (i in clicks.indices) {
                clicks[i] = clicks[i] - 1
                if (clicks[i] == 0) toRemove.add(i)
            }

            toRemove.sortedDescending().forEach(clicks::removeAt)
        }
    }
}
