package com.chattriggers.ctjs.browser.components

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.input.UIPasswordInput
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.state.State
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick

class TextInput(password: Boolean = false) : UIContainer() {
    private var onChangeAction: (String) -> Unit = {}
    private var onTabAction: () -> Unit = {}
    private var onEnterAction: () -> Unit = {}

    private val textHolder = UIBlock().constrain {
        width = 100.percent()
        height = ChildBasedSizeConstraint() + (PADDING * 2).pixels()
        color = VigilancePalette.getDarkHighlight().toConstraint()
    } childOf this effect OutlineEffect(
        VigilancePalette.getDivider(),
        1f
    )

    private val textInput = (if (password) UIPasswordInput() else UITextInput()).constrain {
        x = PADDING.pixels()
        y = PADDING.pixels()
        width = 100.percent() + (PADDING * 2).pixels()
    } childOf textHolder

    init {
        constrain {
            width = 100.percent()
            height = ChildBasedSizeConstraint()
        }

        textInput.onUpdate {
            onChangeAction(it)
        }.onKeyType { _, keyCode ->
            if (keyCode == 15) { // <tab>
                onTabAction()
            } else if (keyCode == 28) { // <enter>
                onEnterAction()
            }
        }.onLeftClick {
            it.stopPropagation()
            textInput.grabWindowFocus()
        }.onFocus {
            textInput.setActive(true)
        }.onFocusLost {
            textInput.setActive(false)
        }
    }

    override fun afterInitialization() {
        super.afterInitialization()

        textInput.constrain {
            width = 100.percent() boundTo this@TextInput.parent
        }
    }

    fun grabFocus() {
        textInput.grabWindowFocus()
        textInput.focus()
    }

    fun releaseFocus() {
        textInput.releaseWindowFocus()
        textInput.loseFocus()
    }

    fun getText() = textInput.getText()

    fun onChange(action: (String) -> Unit) = apply {
        onChangeAction = action
    }

    fun onTab(action: () -> Unit) = apply {
        onTabAction = action
    }

    fun onEnter(action: () -> Unit) = apply {
        onEnterAction = action
    }

    companion object {
        private const val PADDING = 5
    }
}
