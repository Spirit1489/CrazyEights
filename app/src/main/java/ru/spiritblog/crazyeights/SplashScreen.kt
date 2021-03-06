package ru.spiritblog.crazyeights

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.security.AccessController.getContext

private const val TAG = "SplashScreen"


public class SplashScreen(context: Context) : View(context) {


    private lateinit var titleG: Bitmap
    private var scrW: Int = 0
    private var scrH: Int = 0

    init {
        titleG = BitmapFactory.decodeResource(resources, R.drawable.splash_graphic)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        scrW = w
        scrH = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var titleGLeftPos = (scrW - titleG.width) / 2
        canvas?.drawBitmap(titleG, titleGLeftPos.toFloat(), 100F, null)
    }


//    override fun onTouchEvent(evt: MotionEvent): Boolean {
//
//        var action = evt.action
//
//        when (action) {
//
//            MotionEvent.ACTION_DOWN -> Log.d(TAG, "Down")
//            MotionEvent.ACTION_UP -> Log.d(TAG, "Up")
//            MotionEvent.ACTION_MOVE -> {
//                Log.d(TAG, "Move")
//                cx = evt.x
//                cy = evt.y
//            }
//        }
//
//        invalidate()
//        return true
//
//    }


}