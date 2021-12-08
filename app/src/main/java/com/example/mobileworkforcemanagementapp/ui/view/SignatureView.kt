package com.example.mobileworkforcemanagementapp.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs


class SignatureView constructor(context: Context, attrs: AttributeSet): View(context, attrs) {
    private var strokeAdded: Boolean = false
    private var canvas : Canvas = Canvas()
    private var path : Path = Path()
    private var paint : Paint = Paint()
    private var bitMap : Bitmap? = null
    private var bitMapPaint : Paint = Paint(Paint.DITHER_FLAG)
    private var posX = 0f
    private var posY = 0f
    init {
        paint.isAntiAlias = true
        paint.isDither = true
        paint.color = Color.parseColor("#000000")
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 9f
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if(bitMap == null) {
            bitMap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            canvas = Canvas(bitMap!!)
        } else {
            bitMap = Bitmap.createScaledBitmap(bitMap!!, w, h, true)
            canvas = Canvas(bitMap!!)
            canvas.drawBitmap(bitMap!!, 0f, 0f, bitMapPaint)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            super.onDraw(canvas)
            with(canvas) {
                drawBitmap(bitMap!!, 0f, 0f, bitMapPaint)
                drawPath(path, paint)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            super.onTouchEvent(event)
            val posX = event.x
            val posY = event.y

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    path.reset()
                    path.moveTo(posX, posY)
                    this.posX = posX
                    this.posY = posY
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    path.lineTo(this.posX, this.posY)
                    canvas.drawPath(path, paint)
                    path.reset()
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = abs(posX - this.posX)
                    val dy = abs(posY - this.posY)
                    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                        path.quadTo(this.posX, this.posY, (posX + this.posX)/2, (posY + this.posY)/2)
                        this.posX = posX
                        this.posY = posY
                        strokeAdded = true
                    }
                    invalidate()
                }
            }
        }
        return true
    }

    fun isStrokeAdded(): Boolean {
        return strokeAdded
    }
    fun setBitmap(bitmap: Bitmap) {
        with(canvas) {
            drawBitmap(bitmap, 0f, 0f, bitMapPaint)
            drawPath(path, paint)
        }
        strokeAdded = true
    }

    fun clear() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY)
        strokeAdded = false
    }

    companion object {
        const val TOUCH_TOLERANCE = 4f
    }
}