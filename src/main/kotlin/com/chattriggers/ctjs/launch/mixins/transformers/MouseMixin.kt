package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.client.Mouse
import net.minecraft.client.gui.screen.Screen
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Unique
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Desc
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.LocalCapture

// TODO("fabric"): Make these cancellable?
@Mixin(Mouse::class)
class MouseMixin {
    @Inject(
        method = ["onMouseScroll"],
        at = [At(
            value = "INVOKE",
            desc = Desc(
                "mouseScroll",
                owner = Screen::class,
                ret = Boolean::class,
                args = [Double::class, Double::class, Double::class],
            ),
        )],
        locals = LocalCapture.CAPTURE_FAILHARD,
    )
    fun injectOnMouseScroll(screen: Screen, mouseX: Double, mouseY: Double, delta: Double) {
        // TriggerType.Scrolled.triggerAll(mouseX, mouseY, delta)
        scrollListeners.forEach {
            it(mouseX, mouseY, delta)
        }
    }

    @Inject(
        method = ["method_1611"],
        at = [At("HEAD")],
        locals = LocalCapture.CAPTURE_FAILHARD,
    )
    fun inject_method_1611(screen: Screen, mouseX: Double, mouseY: Double, button: Int) {
        // TriggerType.Clicked.triggerAll(mouseX, mouseY, button, /* pressed = */ true)
        clickListeners.forEach {
            it(mouseX, mouseY, button, true)
        }
    }

    @Inject(
        method = ["method_1605"],
        at = [At("HEAD")],
        locals = LocalCapture.CAPTURE_FAILHARD,
    )
    fun inject_method_1605(screen: Screen, mouseX: Double, mouseY: Double, button: Int) {
        // TriggerType.Clicked.triggerAll(mouseX, mouseY, button, /* pressed = */ false)
        clickListeners.forEach {
            it(mouseX, mouseY, button, false)
        }
    }

    @Inject(
        method = ["method_1602"],
        at = [At("HEAD")],
        locals = LocalCapture.CAPTURE_FAILHARD,
    )
    fun inject_method_1602(
        screen: Screen,
        mouseX: Double,
        mouseY: Double,
        button: Int,
        deltaX: Double,
        deltaY: Double,
    ) {
        // TriggerType.Dragged.triggerAll(deltaX, deltaY, mouseX, mouseY, button)
        draggedListeners.forEach {
            it(deltaX, deltaY, mouseX, mouseY, button)
        }
    }


    companion object {
        @Unique @JvmStatic
        private val scrollListeners = mutableListOf<(x: Double, y: Double, delta: Double) -> Unit>()

        @Unique @JvmStatic
        private val clickListeners = mutableListOf<(x: Double, y: Double, button: Int, pressed: Boolean) -> Unit>()

        @Unique @JvmStatic
        private val draggedListeners = mutableListOf<(deltaX: Double, deltaY: Double, x: Double, y: Double, button: Int) -> Unit>()

        @Unique @JvmStatic
        fun registerScrollListener(listener: (x: Double, y: Double, delta: Double) -> Unit) {
            scrollListeners.add(listener)
        }

        @Unique @JvmStatic
        fun registerClickListener(listener: (x: Double, y: Double, button: Int, pressed: Boolean) -> Unit) {
            clickListeners.add(listener)
        }

        @Unique @JvmStatic
        fun registerDraggedListener(listener: (deltaX: Double, deltaY: Double, x: Double, y: Double, button: Int) -> Unit) {
            draggedListeners.add(listener)
        }

        @Unique @JvmStatic
        fun registerTriggerListeners() {
            registerScrollListener(TriggerType.Scrolled::triggerAll)
            registerClickListener(TriggerType.Clicked::triggerAll)
            registerDraggedListener(TriggerType.Dragged::triggerAll)
        }

        @Unique @JvmStatic
        fun clearListeners() {
            scrollListeners.clear()
            clickListeners.clear()
            draggedListeners.clear()
        }
    }
}
