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

    init {
        titleG = BitmapFactory.decodeResource(resources, R.drawable.splash_graphic)
    }




    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
       canvas?.drawBitmap(titleG, 100F, 100F, null)
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