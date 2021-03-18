package ru.spiritblog.crazyeights

import android.graphics.Bitmap


class Card(newId: Int) {


    var id = newId
    var suit = 0
    var rank = 0

    lateinit var bmp: Bitmap

    private var scoreValue = 0


    init {
        suit = Math.round((id / 100) * 100.toDouble()).toInt()
        rank = id - suit
        if (rank == 8){
            scoreValue = 50
        } else if (rank == 14){
            scoreValue = 1
        } else if (rank > 9 && rank < 14){
            scoreValue = 10
        } else{
            scoreValue = rank
        }
    }


    public fun getScoreValue():Int{
        return scoreValue
    }



}