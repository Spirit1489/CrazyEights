package ru.spiritblog.crazyeights

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import android.view.View
import java.util.*

class CrazyEightView(context: Context) : View(context) {

    private var scrW: Int = 0
    private var scrH: Int = 0
    private var ctx: Context = context
    private var deck: MutableList<Card> = ArrayList<Card>()
    private var playerHand: MutableList<Card> = ArrayList<Card>()
    private var computerHand: MutableList<Card> = ArrayList<Card>()
    private var discardPile: MutableList<Card> = ArrayList<Card>()


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        scrW = w
        scrH = h

        initializeDeck()
        dealCards()

    }


    private fun initializeDeck() {

        for (i in 0..3) {
            for (j in 102..114) {
                var tempId = j + (i * 100)
                var tempCard = Card(tempId)
                var resourceId = resources.getIdentifier(
                    "card" + tempId,
                    "drawable", ctx.getPackageName()
                )
                var tempBitmap = BitmapFactory.decodeResource(ctx.getResources(), resourceId)

                var scaledCW = (scrW / 8)
                var scaledCH = (scaledCW * 1.28)
                var scaledBitmap = Bitmap.createScaledBitmap(
                    tempBitmap, scaledCW,
                    scaledCH.toInt(), false
                )

                tempCard.bmp = scaledBitmap
                deck.add(tempCard)


            }

        }


    }


    private fun dealCards() {
        Collections.shuffle(deck, Random())

        for (i in 0..6) {
            drawCard(playerHand)
            drawCard(computerHand)
        }

    }


    private fun drawCard(hand: MutableList<Card>) {

        hand.add(deck[0])
        deck.removeAt(0)

        if (deck.isEmpty()) {
            for (i in (discardPile.size - 1) downTo 0) {

                deck.add(discardPile.get(i))
                discardPile.removeAt(i)
                Collections.shuffle(deck, Random())


            }


        }


    }


}