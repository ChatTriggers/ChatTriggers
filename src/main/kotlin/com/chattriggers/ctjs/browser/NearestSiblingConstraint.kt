package com.chattriggers.ctjs.browser

import gg.essential.elementa.UIComponent
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.resolution.ConstraintVisitor

/**
 * For some reason SiblingConstraint gets the positions/sizes of _all_
 * siblings which come before it, instead of just the immediate sibling.
 * This one doesn't do that.
 */
class NearestSiblingConstraint @JvmOverloads constructor(
    val padding: Float = 0f,
    val alignOpposite: Boolean = false
) : PositionConstraint, PaddingConstraint {
    override var cachedValue = 0f
    override var recalculate = true
    override var constrainTo: UIComponent? = null

    override fun getXPositionImpl(component: UIComponent): Float {
        constrainTo?.let {
            return if (alignOpposite) {
                it.getLeft() - component.getWidth() - padding
            } else {
                it.getRight() + padding
            }
        }

        val index = component.parent.children.indexOf(component)

        if (alignOpposite) {
            if (index == 0) return component.parent.getRight() - component.getWidth()
            val sibling = component.parent.children[index - 1]
            return sibling.getLeft() - component.getWidth() - padding
        } else {
            if (index == 0) return component.parent.getLeft()
            val sibling = component.parent.children[index - 1]
            return sibling.getRight() + padding
        }
    }

    override fun getYPositionImpl(component: UIComponent): Float {
        constrainTo?.let {
            return if (alignOpposite) {
                it.getTop() - component.getHeight() - padding
            } else {
                it.getBottom() + padding
            }
        }

        val index = component.parent.children.indexOf(component)

        if (alignOpposite) {
            if (index == 0) return component.parent.getBottom() - component.getHeight()
            val sibling = component.parent.children[index - 1]
            return sibling.getTop() - component.getHeight() - padding
        } else {
            if (index == 0) return component.parent.getTop()
            val sibling = component.parent.children[index - 1]
            return sibling.getBottom() + padding
        }
    }

    override fun visitImpl(visitor: ConstraintVisitor, type: ConstraintType) {
        val indexInParent = visitor.component.let { it.parent.children.indexOf(it) }

        when (type) {
            ConstraintType.X -> {
                if (alignOpposite) {
                    visitor.visitSelf(ConstraintType.WIDTH)

                    if (indexInParent <= 0) {
                        visitor.visitParent(ConstraintType.X)
                        visitor.visitParent(ConstraintType.WIDTH)
                        return
                    }

                    for (n in indexInParent - 1 downTo 0) {
                        visitor.visitSibling(ConstraintType.X, n)
                        visitor.visitSibling(ConstraintType.WIDTH, n)
                    }
                } else {
                    if (indexInParent <= 0) {
                        visitor.visitParent(ConstraintType.X)
                        return
                    }

                    for (n in indexInParent - 1 downTo 0) {
                        visitor.visitSibling(ConstraintType.X, n)
                        // TODO: Avoid this width call when not actually called in getXPositionImpl
                        visitor.visitSibling(ConstraintType.WIDTH, n)
                    }
                }
            }
            ConstraintType.Y -> {
                if (alignOpposite) {
                    visitor.visitSelf(ConstraintType.HEIGHT)

                    if (indexInParent <= 0) {
                        visitor.visitParent(ConstraintType.Y)
                        visitor.visitParent(ConstraintType.HEIGHT)
                        return
                    }

                    for (n in indexInParent - 1 downTo 0) {
                        visitor.visitSibling(ConstraintType.Y, n)
                        visitor.visitSibling(ConstraintType.HEIGHT, n)
                    }
                } else {
                    if (indexInParent <= 0) {
                        visitor.visitParent(ConstraintType.Y)
                        return
                    }

                    for (n in indexInParent - 1 downTo 0) {
                        visitor.visitSibling(ConstraintType.Y, n)
                        // TODO: Avoid this width call when not actually called in getXPositionImpl
                        visitor.visitSibling(ConstraintType.HEIGHT, n)
                    }
                }
            }
            else -> throw IllegalArgumentException(type.prettyName)
        }
    }

    override fun getVerticalPadding(component: UIComponent): Float {
        return padding
    }

    override fun getHorizontalPadding(component: UIComponent): Float {
        return padding
    }
}
