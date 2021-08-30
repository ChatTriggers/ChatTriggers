package com.chattriggers.ctjs.browser.components

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.browser.ModuleBrowser
import com.chattriggers.ctjs.browser.WebsiteAPI
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.Window
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.elementa.transitions.SlideFromTransition
import gg.essential.elementa.transitions.SlideToTransition
import gg.essential.elementa.transitions.Transition
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.utils.onLeftClick
import java.awt.Color
import java.net.URL

class AccountModal(private val browser: ModuleBrowser) : Modal(
    HighlightedBlock(
        backgroundColor = VigilancePalette.getBackground(),
        highlightColor = VigilancePalette.getHighlight(),
        highlightHoverColor = VigilancePalette.getBrightHighlight(),
    )
) {
    private var isOnLoginScreen = true

    private val content by UIContainer().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 230.pixels()
        height = 170.pixels()
    } childOf container

    private val buttonContainer by UIContainer().constrain {
        x = CenterConstraint()
        y = 10.pixels()
        width = ChildBasedSizeConstraint() - 1.pixel()
        height = ChildBasedMaxSizeConstraint()
    } childOf content

    private val loginButton by ButtonComponent("Login").constrain {
        x = 0.pixels()
    }.onClick {
        setLogin(true)
    } childOf buttonContainer

    private val registerButton by ButtonComponent("Register").constrain {
        x = SiblingConstraint() - 1.pixel()
    }.onClick {
        setLogin(false)
    } childOf buttonContainer

    private val leftDivider by UIBlock(VigilancePalette.getDivider()).constrain {
        x = SiblingConstraint(alignOpposite = true) boundTo buttonContainer
        y = CenterConstraint() boundTo buttonContainer
        width = 40.pixels()
        height = 1.pixel()
    } childOf content

    private val rightDivider by UIBlock(VigilancePalette.getDivider()).constrain {
        x = SiblingConstraint() boundTo buttonContainer
        y = CenterConstraint() boundTo buttonContainer
        width = 40.pixels()
        height = 1.pixel()
    } childOf content

    private val usernameField by InputField("Username").constrain {
        y = SiblingConstraint(20f) boundTo buttonContainer
    } childOf content

    private val emailField by InputField("Email", initiallyHidden = true) childOf content

    private val passwordField by InputField("Password", password = true) childOf content

    private val confirmPasswordField by InputField("Confirm Password", password = true, initiallyHidden = true) childOf content

    private val submit by ButtonComponent("Submit").constrain {
        x = CenterConstraint()
        y = SiblingConstraint(20f)
    } childOf content

    private val submitButtonError by SubmitButtonError().constrain {
        x = SiblingConstraint(10f)
        y = CenterConstraint() boundTo submit
    } childOf content

    init {
        submit.onLeftClick {
            if (Window.ofOrNull(submitButtonError) == null)
                return@onLeftClick

            if (isOnLoginScreen) {
                val username = usernameField.getText().trim()
                val result = WebsiteAPI.login(username, passwordField.getText().trim())
                if (!result)
                    TODO()

                fadeOut()
                browser.onLoggedIn(username)
            } else {
                val username = usernameField.getText().trim()
                val email = emailField.getText().trim()
                val password = passwordField.getText().trim()
                val result = WebsiteAPI.createAccount(username, email, password)
                if (!result)
                    TODO()

                fadeOut()
                browser.onLoggedIn(username)
            }
        }
    }

    override fun afterInitialization() {
        super.afterInitialization()
        loginButton.setActive(true)
        validateInputs()

        // Inspector(this) childOf this
    }

    private fun setLogin(login: Boolean) {
        if (isOnLoginScreen == login)
            return

        isOnLoginScreen = !isOnLoginScreen
        loginButton.setActive(login)
        registerButton.setActive(!login)

        val method = if (login) InputField::transitionOut else InputField::transitionIn
        method(emailField)
        method(confirmPasswordField)
        validateInputs()

        content.animate {
            setHeightAnimation(Animations.OUT_EXP, 0.5f, (if (login) 170 else 270).pixels())
        }
    }

    private fun validateInputs() {
        val errors = mutableListOf<String>()

        if (isOnLoginScreen) {
            if (usernameField.getText().isBlank())
                errors.add("The username field must not be blank")
            if (passwordField.getText().isBlank())
                errors.add("The password field must not be blank")
        } else {
            val username = usernameField.getText().trim()
            val email = emailField.getText().trim()
            val password = passwordField.getText().trim()
            val passwordConfirmation = confirmPasswordField.getText().trim()

            if (username.isEmpty()) {
                errors.add("The username field must not be blank")
            } else if (!username.matches(usernameRegex)) {
                errors.add("Your username must have between 3 and 16 alphanumeric characters")
            }

            if (email.isEmpty()) {
                errors.add("The email field must not be blank")
            } else if (!email.matches(emailRegex)) {
                errors.add("Your email must be a valid email address")
            }

            if (password.isEmpty()) {
                errors.add("The password field must not be blank")
            } else if (password.length < 8) {
                errors.add("Your password must have at least 8 characters")
            }

            if (passwordConfirmation.isEmpty()) {
                errors.add("The confirm password field must not be blank")
            } else if (passwordConfirmation != password) {
                errors.add("The password confirmation does not match your password")
            }
        }

        if (errors.isEmpty()) {
            submitButtonError.hide()
        } else {
            submitButtonError.unhide()
        }

        submitButtonError.setErrors(errors)
    }

    inner class InputField(text: String, password: Boolean = false, initiallyHidden: Boolean = false) : UIContainer() {
        private val container by UIContainer().constrain {
            y = 1.pixels(alignOpposite = true)
            width = ChildBasedMaxSizeConstraint()
        } childOf this

        private val title by UIText(text, shadow = false).constrain {
            x = 1.pixel()
            color = VigilancePalette.getDarkText().toConstraint()
        } childOf container

        private val input by TextInput(password).constrain {
            x = 1.pixel()
            y = SiblingConstraint(5f)
            width = 150.pixels()
        } childOf container

        init {
            constrain {
                x = CenterConstraint()
                y = SiblingConstraint(if (initiallyHidden) 0f else 10f)
                width = ChildBasedSizeConstraint() + 2.pixels()
                height = if (initiallyHidden) {
                    0.pixels()
                } else ChildBasedSizeConstraint() + 2.pixels()
            }

            container.constrain {
                height = basicHeightConstraint {
                    title.getHeight() + input.getHeight() + 5f
                }
            }

            enableEffect(ScissorEffect())

            input.onChange {
                validateInputs()
            }
        }

        fun transitionIn() {
            animate {
                setYAnimation(Animations.OUT_EXP, 0.5f, SiblingConstraint(10f))
                setHeightAnimation(Animations.OUT_EXP, 0.5f, ChildBasedSizeConstraint() + 2.pixels())
            }
        }

        fun transitionOut() {
            animate {
                setYAnimation(Animations.OUT_EXP, 0.5f, SiblingConstraint(0f))
                setHeightAnimation(Animations.OUT_EXP, 0.5f, 0.pixels())
            }
        }

        fun getText() = input.getText()
    }

    companion object {
        private val usernameRegex = """\w{3,16}""".toRegex()
        // The simplest email regex the world has ever seen. By no means meant to
        // _actually_ validate emails, the backend can do that
        private val emailRegex = """.+@.+\..+""".toRegex()
    }
}
