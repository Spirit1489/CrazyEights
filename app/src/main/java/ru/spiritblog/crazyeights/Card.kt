package ru.spiritblog.crazyeights

import android.graphics.Bitmap


class Card(newId: Int) {


    private var id = newId
    private var suit = 0
    private var rank = 0

    lateinit var bmp: Bitmap


    private var scoreValue = 0


}