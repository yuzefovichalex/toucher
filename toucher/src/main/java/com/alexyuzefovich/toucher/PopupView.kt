package com.alexyuzefovich.toucher

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

class PopupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val ANIMATION_DURATION = 275L
        private const val VIEW_OFFSET = 8
        private const val ICON_INNER_OFFSET = 24
    }

    private var currentScale: Float = 0f
    private val scaleAnimator: ValueAnimator = ValueAnimator().apply {
        duration =
            ANIMATION_DURATION
        addUpdateListener {
            currentScale = it.animatedValue as Float
            invalidate()
        }
    }

    private val backgroundPaint: Paint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        isDither = true
    }

    private val anchorPreDrawListener: ViewTreeObserver.OnPreDrawListener =
        ViewTreeObserver.OnPreDrawListener {
            placeNearAnchor()
            true
        }

    private var parent: ViewGroup? = null
    private var currentAnchor: View? = null

    //TODO
    var icon: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }
    private var iconColor: Int = Color.WHITE
        set(value) {
            field = value
            postInvalidate()
        }
    private val iconPaint = Paint().apply {
        colorFilter = PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
    }
    private val iconRect = RectF()

    var clickAction: (() -> Unit)? = null

    init {
        setOnClickListener {
            hide(clickAction)
        }
    }

    fun show(anchorView: View) {
        currentAnchor = anchorView
        anchorView.viewTreeObserver.addOnPreDrawListener(anchorPreDrawListener)
        val parent = anchorView.rootView
        if (parent is ViewGroup) {
            this.parent = parent
            parent.addView(this)
        }
    }

    fun hide(endAction: (() -> Unit)? = null) {
        scaleAnimator.run {
            setFloatValues(currentScale, 0f)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) { }

                override fun onAnimationEnd(animation: Animator?) {
                    scaleAnimator.removeListener(this)
                    currentAnchor?.viewTreeObserver?.removeOnPreDrawListener(anchorPreDrawListener)
                    currentAnchor = null
                    parent?.removeView(this@PopupView)
                    endAction?.invoke()
                }

                override fun onAnimationCancel(animation: Animator?) { }

                override fun onAnimationStart(animation: Animator?) { }
            })
            start()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        scaleAnimator.run {
            setFloatValues(currentScale, 1f)
            scaleAnimator.start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        placeNearAnchor()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //TODO add dp
        setMeasuredDimension(32, 32)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = centerX * currentScale
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)
        icon?.let {
            iconRect.set(
                centerX - radius + ICON_INNER_OFFSET,
                centerY - radius + ICON_INNER_OFFSET,
                centerX + radius - ICON_INNER_OFFSET,
                centerY + radius - ICON_INNER_OFFSET
            )
            canvas.drawBitmap(it, null, iconRect, iconPaint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        scaleAnimator.cancel()
    }

    override fun setOnClickListener(listener: OnClickListener?) {

    }

    private fun placeNearAnchor() {
        currentAnchor?.let {
            val rect = Rect().apply { it.getGlobalVisibleRect(this) }
            val top = rect.top.toFloat()
            val right = rect.right.toFloat()
            x = right + VIEW_OFFSET
            y = top - height - VIEW_OFFSET
        }
    }

    class Builder(context: Context) {

        private val popupView: PopupView =
            PopupView(context)

        fun setIcon(icon: Bitmap): Builder {
            popupView.icon = icon
            return this
        }

        fun setIconColor(color: Int): Builder {
            popupView.iconColor = color
            return this
        }

        fun build(): PopupView = popupView

    }

}