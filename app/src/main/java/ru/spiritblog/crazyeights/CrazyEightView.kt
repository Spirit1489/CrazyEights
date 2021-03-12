package ru.spiritblog.crazyeights

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.view.View
import java.util.*

class CrazyEightView(context: Context) : View(context) {

    private var scrW: Int = 0
    private var scrH: Int = 0
    private var ctx: Context = context
    private var scale = ctx.resources.displayMetrics.density
    private var paint = Paint()
    private var scaledCW = 0
    private var scaledCH = 0
    private lateinit var cardBack: Bitmap
    private var deck: MutableList<Card> = ArrayList<Card>()
    private var playerHand: MutableList<Card> = ArrayList<Card>()
    private var computerHand: MutableList<Card> = ArrayList<Card>()
    private var discardPile: MutableList<Card> = ArrayList<Card>()


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        scrW = w
        scrH = h

        initializeDeck()
        dealCards()

        // Draw the computers`s hand

        scaledCW = scrW / 8
        scaledCH = (scaledCW * 1.28).toInt()

        var tempBitmap = BitmapFactory.decodeResource(ctx.resources, R.drawable.card_back)
        cardBack = Bitmap.createScaledBitmap(tempBitmap, scaledCW, scaledCH, false)


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


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        // Draw the computerHand

        for (i in 0 until computerHand.size) {
            canvas.drawBitmap(
                cardBack, i * (scale * 5),
                paint.textSize + (50 * scale), null
            )
        }


        // Draw the humanPlayer hand


        for (i in 0 until playerHand.size) {

            canvas.drawBitmap(
                playerHand[i].bmp, i * (scaledCW + 5).toFloat(),
                scrH - scaledCH - paint.textSize - (50 * scale), null
            )

        }


        // Draw pile

        var cbackLeft = (scrW / 2) - cardBack.width - 10
        var cbackTop = (scrH / 2) - (cardBack.height / 2)
        canvas.drawBitmap(cardBack, cbackLeft.toFloat(), cbackTop.toFloat(), null)

        // Discard pile

        if (discardPile.isNotEmpty()) {

            canvas.drawBitmap(
                discardPile[0].bmp, (scrW / 2) + 10.toFloat(),
                (scrH / 2) - (cardBack.height / 2).toFloat(), null
            )


        }


    }


}