package com.polidea.cockpit.sample.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import com.polidea.cockpit.cockpit.Cockpit
import com.polidea.cockpit.sample.R
import kotlinx.android.synthetic.main.view_morphing_button.view.*

class MorphingButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    var buttonBackground: Drawable?
        get() = button.background
        set(value) {
            button.background = value
        }

    var successBackground: Drawable?
        get() = success_view.background
        set(value) {
            success_view.background = value
        }

    private var wrappedClickListener: OnClickListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_morphing_button, this)
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        wrappedClickListener = OnClickListener {
            listener?.onClick(it)
            showSuccess()
        }
        button.setOnClickListener(wrappedClickListener)
    }

    private fun showSuccess() {
        val successAnimation = morphAnimation(button, success_view)
        successAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                val reverseAnimation = morphAnimation(success_view, button)
                reverseAnimation.startDelay = REVERSE_ANIMATION_DELAY_MS
                reverseAnimation.start()
            }
        })
        successAnimation.start()
    }

    private fun morphAnimation(viewToFadeOut: View, viewToFadeIn: View): Animator {
        val fadeOutAnimation = ObjectAnimator.ofFloat(viewToFadeOut, View.ALPHA, 1.0f, 0.0f)
        fadeOutAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                viewToFadeOut.visibility = View.INVISIBLE
            }
        })

        val fadeInAnimation = ObjectAnimator.ofFloat(viewToFadeIn, View.ALPHA, 0.0f, 1.0f)
        fadeInAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                viewToFadeIn.visibility = View.VISIBLE
            }
        })
        val animatorSet = AnimatorSet()
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                // ignore clicks when animation is running
                button.setOnClickListener(null)
            }

            override fun onAnimationEnd(animation: Animator?) {
                button.setOnClickListener(wrappedClickListener)
            }
        })
        animatorSet.playTogether(fadeOutAnimation, fadeInAnimation)
        animatorSet.duration = Cockpit.getAnimationSpeed().toLong()
        return animatorSet
    }

    companion object {
        const val REVERSE_ANIMATION_DELAY_MS = 1000L
    }
}