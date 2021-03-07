package ru.spiritblog.crazyeights

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.security.AccessController.getContext

private const val TAG = "SplashScreen"


public class SplashScreen(context: Context) : View(context) {

    // Title
    private lateinit var titleG: Bitmap
    private var scrW: Int = 0
    private var scrH: Int = 0

    // PlayButton
    private lateinit var playBtnUp: Bitmap
    private lateinit var playBtnDn: Bitmap
    private var playBtnPressed: Boolean = false


    init {
        titleG = BitmapFactory.decodeResource(resources, R.drawable.splash_graphic)

        playBtnUp = BitmapFactory.decodeResource(resources, R.drawable.btn_up)
        playBtnDn = BitmapFactory.decodeResource(resources, R.drawable.btn_down)
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

        var playBtnLeftPos = (scrW - playBtnUp.width) / 2
        if (playBtnPressed) {
            canvas?.drawBitmap(
                playBtnDn, playBtnLeftPos.toFloat(),
                (scrH * 0.5).toFloat(), null
            )
        } else {
            canvas?.drawBitmap(
                playBtnUp, playBtnLeftPos.toFloat(),
                (scrH * 0.5).toFloat(), null
            )
        }

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        var evtAction = event.action

        var x = event.x.toInt()
        var y = event.y.toInt()



        when (evtAction) {

            MotionEvent.ACTION_DOWN -> {
                var btnLeft = (scrW - playBtnUp.width) / 2
                var btnRight = btnLeft + playBtnUp.width
                var btnTop = (scrH * 0.5)
                var btnBottom = btnTop + playBtnUp.height

                var withinBtnBounds = (x > btnLeft) &&
                        (x < btnRight) &&
                        (y > btnTop) &&
                        (y < btnBottom)
                if (withinBtnBounds) {
                    playBtnPressed = true
                }


            }


            MotionEvent.ACTION_UP -> {
                if (playBtnPressed) {
                    // Launch main game screen
                }

            }


            MotionEvent.ACTION_MOVE -> {
            }
        }

        invalidate()
        return true

    }


}