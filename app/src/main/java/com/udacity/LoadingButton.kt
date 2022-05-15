package com.udacity

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var bgColor: Int = Color.BLACK
    private var textColor: Int = Color.BLACK
    private var progress: Double = 0.0
    private var loadingAnimation = ValueAnimator()
    private var boundRectangle:RectF = RectF()
    private var circleLoadingAngle = 0f
    private var circleAnimation = ValueAnimator()
    private val circlePaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }

    private val updateListener = ValueAnimator.AnimatorUpdateListener{
        progress = (it.animatedValue as Float).toDouble()
        invalidate()
        requestLayout()
    }


    init {
        isClickable = true
        loadingAnimation = AnimatorInflater.loadAnimator(
            context,
            R.animator.loading_animation
        ) as ValueAnimator

        loadingAnimation.addUpdateListener(updateListener)

        val attr = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0,
            0
        )

        try{
            bgColor = attr.getColor(
                R.styleable.LoadingButton_bgColor,
                ContextCompat.getColor(context,R.color.colorPrimary)
            )

            textColor = attr.getColor(
                R.styleable.LoadingButton_textColor,
                ContextCompat.getColor(context,R.color.white)
            )
        } finally{
            attr.recycle()
        }
    }


    override fun performClick(): Boolean {
        super.performClick()
        if(buttonState == ButtonState.Completed) {
            buttonState = ButtonState.Loading
        }
        animation()

        return true
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawInitialButton(canvas)
        loadingAnimation(canvas)
        drawText(canvas)
        drawCircle(canvas)
    }

    private fun animation(){
        loadingAnimation.start()
        beginCircleAnimation()
    }

    fun hasCompletedDownload(){
        loadingAnimation.cancel()
        endCircleAnimation()
        buttonState = ButtonState.Completed
        invalidate()
        requestLayout()
    }

    private fun drawInitialButton(canvas:Canvas?){
        boundRectangle = RectF(0f,0f,widthSize.toFloat(),heightSize.toFloat())
        canvas!!.clipRect(boundRectangle)
        canvas!!.drawColor(Color.BLUE)
    }

    private fun drawText(canvas:Canvas?){
        var buttonText = "Download"
        if(buttonState == ButtonState.Loading){
            buttonText = "Busy..."
        }
        //Draw text
        var rectangle = RectF(0f,0f,widthSize.toFloat(),heightSize.toFloat())
        var paintText: Paint = Paint()
        paintText.color = Color.WHITE
        paintText.textSize = 100f
        paintText.textAlign = Paint.Align.CENTER
        var baseline = calculateFontBaseline(paintText,rectangle)
        canvas!!.drawText(buttonText,widthSize.toFloat()/2,baseline,paintText)
    }

    private fun loadingAnimation(canvas:Canvas?){
        if(buttonState == ButtonState.Loading){
            var paintRect = Paint()
            paintRect.color =  Color.GREEN

            var rectangle = RectF(0f,0f,(widthSize * (progress/100)).toFloat(),heightSize.toFloat())
            canvas!!.drawRect(rectangle,paintRect)
        }

    }

    private fun calculateFontBaseline(paint:Paint,rectangle:RectF):Float{
        var fontMetrics = paint.getFontMetrics()
        var distance = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom
        var baseline = rectangle.centerY()+distance

        return baseline

    }


    private fun beginCircleAnimation() {
        circleAnimation = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 2000
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                circleLoadingAngle = it.animatedValue as Float
                invalidate()
            }
        }
        circleAnimation.start()
    }


    private fun endCircleAnimation() {
        val startValue = loadingAnimation.animatedValue
        circleAnimation.end()
        circleAnimation = ValueAnimator.ofFloat(
            startValue as Float,
            360f
        ).apply {
            duration =  0
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                circleLoadingAngle = it.animatedValue as Float
                if (circleAnimation.animatedFraction == 1f) {
                    circleLoadingAngle = 0f
                }
                invalidate()
            }
        }
        circleAnimation.start()
    }


    private fun drawCircle(canvas: Canvas?) {
        canvas?.drawArc(
            0f,
            0f,
            boundRectangle.height().toFloat(),
            boundRectangle.height().toFloat(),
            0f,
            circleLoadingAngle,
            true,
            circlePaint
        )
    }









}