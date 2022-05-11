package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }




    init {




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
        //super.onDraw(canvas)
        drawInitialButton(canvas)


    }

    private fun drawInitialButton(canvas:Canvas?){
        //Draw background
        var rectangle = RectF(0f,0f,widthSize.toFloat(),heightSize.toFloat())
        canvas!!.clipRect(rectangle)
        canvas!!.drawColor(Color.BLUE)

        //Draw text
        var paintText: Paint = Paint()
        paintText.color = Color.WHITE
        paintText.textSize = 100f
        paintText.textAlign = Paint.Align.CENTER
        var baseline = calculateFontBaseline(paintText,rectangle)
        canvas!!.drawText("Download",widthSize.toFloat()/2,baseline,paintText)
    }

    private fun calculateFontBaseline(paint:Paint,rectangle:RectF):Float{
        var fontMetrics = paint.getFontMetrics()
        var distance = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom
        var baseline = rectangle.centerY()+distance

        return baseline

    }







}