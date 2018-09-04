package com.polidea.cockpit.paramsedition.layout

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.support.annotation.IdRes
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import com.polidea.cockpit.R


internal class CockpitLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

    var startDrag: Boolean = false
    private var state = State.EXPANDED
    private lateinit var draggableView: View
    private lateinit var collapsedDraggableViewProperties: DraggableViewProperties

    private val dragHelper: ViewDragHelper = ViewDragHelper.create(this, 1f, DragHelperCallback())

    fun setDraggableView(@IdRes draggableViewId: Int) {
        draggableView = findViewById(draggableViewId)
    }

    fun expand() {
        state = State.EXPANDING
        animateDraggableViewToNextState(collapsedDraggableViewProperties.margin, 0,
                collapsedDraggableViewProperties.top.toFloat(), 0f,
                collapsedDraggableViewProperties.height, height, State.EXPANDED)
    }

    fun collapse() {
        state = State.COLLAPSING
        animateDraggableViewToNextState(0, collapsedDraggableViewProperties.margin,
                0f, collapsedDraggableViewProperties.top.toFloat(),
                height, collapsedDraggableViewProperties.height, State.COLLAPSED)
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            postInvalidateOnAnimation()
        } else {
            val draggableViewMargins = draggableView.layoutParams as MarginLayoutParams
            if (state == State.COLLAPSED)
                collapsedDraggableViewProperties.top = draggableView.top - draggableViewMargins.topMargin
            Log.d("myk", "computeScroll state ${state.name} draggableView.top ${draggableView.top} translation ${draggableView.translationY} collapsedDraggableViewProperties ${collapsedDraggableViewProperties.top}")
        }
//        Log.d("myk", "computeScroll draggableViewYOffset $draggableViewYOffset")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!::collapsedDraggableViewProperties.isInitialized)
            collapsedDraggableViewProperties = createDefaultCollapsedDraggableViewProperties()

        Log.d("myk", "onLayout state ${state.name} top ${collapsedDraggableViewProperties.top}")
//        if (state == State.COLLAPSED)
//            draggableView.offsetTopAndBottom(collapsedDraggableViewProperties.top)

    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            dragHelper.cancel()
            return false
        }
        if (startDrag && action == MotionEvent.ACTION_MOVE) {
            startDrag = false
            dragHelper.captureChildView(draggableView, ev.getPointerId(0))
        }

        return dragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        dragHelper.processTouchEvent(ev)
        return true
    }

    private fun createDefaultCollapsedDraggableViewProperties() =
            context.resources.let {
                val cockpitMargin = it.getDimension(R.dimen.cockpit_draggable_margin).toInt()
                val cockpitHeight = it.getDimension(R.dimen.cockpit_draggable_min_height).toInt()
                val cockpitTop = 0
//                val cockpitTop = height - cockpitHeight - 2 * cockpitMargin
                DraggableViewProperties(cockpitMargin, cockpitTop, cockpitHeight)
            }

    private fun animateDraggableViewToNextState(marginFrom: Int, marginTo: Int, topFrom: Float,
                                                topTo: Float, heightFrom: Int, heightTo: Int, newState: State) {
        Log.d("myk", "animateDraggableViewScale marginFrom: $marginFrom, marginTo: $marginTo, topFrom: $topFrom, topTo: $topTo, heightFrom: $heightFrom, heightTo: $heightTo")
        val animator = AnimatorSet()
        val leftMarginAnimation = ValueAnimator.ofInt(marginFrom, marginTo)
        leftMarginAnimation.addUpdateListener {
            val marginParams = draggableView.layoutParams as MarginLayoutParams
            marginParams.leftMargin = it.animatedValue as Int
            draggableView.layoutParams = marginParams
        }
        val rightMarginAnimation = ValueAnimator.ofInt(marginFrom, marginTo)
        rightMarginAnimation.addUpdateListener {
            val marginParams = draggableView.layoutParams as MarginLayoutParams
            marginParams.rightMargin = it.animatedValue as Int
            draggableView.layoutParams = marginParams
        }
        val topMarginAnimation = ValueAnimator.ofInt(marginFrom, marginTo)
        rightMarginAnimation.addUpdateListener {
            val marginParams = draggableView.layoutParams as MarginLayoutParams
            marginParams.topMargin = it.animatedValue as Int
            draggableView.layoutParams = marginParams
        }
        val bottomMarginAnimation = ValueAnimator.ofInt(marginFrom, marginTo)
        rightMarginAnimation.addUpdateListener {
            val marginParams = draggableView.layoutParams as MarginLayoutParams
            marginParams.bottomMargin = it.animatedValue as Int
            draggableView.layoutParams = marginParams
        }
        val topPosAnimation = ValueAnimator.ofFloat(topFrom, topTo)
        topPosAnimation.addUpdateListener {
            draggableView.translationY = it.animatedValue as Float
        }
        val heightAnimation = ValueAnimator.ofInt(heightFrom, heightTo)
        heightAnimation.addUpdateListener {
            val params = draggableView.layoutParams
            params.height = it.animatedValue as Int
            draggableView.layoutParams = params
        }

        animator.playTogether(topMarginAnimation, bottomMarginAnimation,
                leftMarginAnimation, rightMarginAnimation,
                topPosAnimation, heightAnimation)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                state = newState
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
        animator.start()
    }

    private inner class DragHelperCallback : ViewDragHelper.Callback() {

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            invalidate()
        }

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return false
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return (child.layoutParams as MarginLayoutParams).leftMargin
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            val draggableViewMargins = draggableView.layoutParams as MarginLayoutParams
            val topBound = draggableViewMargins.topMargin - draggableView.translationY.toInt()
            val bottomBound = height - draggableView.height - draggableViewMargins.bottomMargin - draggableView.translationY.toInt()
            Log.d("myk", "clampViewPositionVertical draggableViewTop ${draggableView.top} top $top topBound $topBound bottomBound $bottomBound")
            return Math.min(Math.max(top, topBound), bottomBound)
        }
    }

    private data class DraggableViewProperties(val margin: Int, var top: Int, val height: Int)
    private enum class State { EXPANDING, EXPANDED, COLLAPSING, COLLAPSED }
}