package ru.spiritblog.crazyeights

import android.app.Dialog
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.*
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
    private var myTurn = false
    private var deck: MutableList<Card> = ArrayList<Card>()
    private var playerHand: MutableList<Card> = ArrayList<Card>()
    private var computerHand: MutableList<Card> = ArrayList<Card>()
    private var discardPile: MutableList<Card> = ArrayList<Card>()
    private var movingIdx = -1
    private var movingX = 0F
    private var movingY = 0F
    private var computerPlayer = ComputerPlayer()
    private var validRank = 8
    private var validSuit = 0
    private lateinit var nextCardBtn: Bitmap
    private var computerScore = 0
    private var myScore = 0
    private var currScore = 0


    init {

        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = scale * 15


    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        scrW = w
        scrH = h

        scaledCW = scrW / 8
        scaledCH = (scaledCW * 1.28).toInt()

        var tempBitmap = BitmapFactory.decodeResource(ctx.resources, R.drawable.card_back)
        cardBack = Bitmap.createScaledBitmap(tempBitmap, scaledCW, scaledCH, false)
        nextCardBtn = BitmapFactory.decodeResource(resources, R.drawable.arrow_next)


        initializeDeck()
        dealCards()
        drawCard(discardPile)
        validSuit = discardPile[0].suit
        validRank = discardPile[0].rank



        myTurn = Random().nextBoolean()
        if (!myTurn) {
            computerPlay()
        }

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

    private fun computerPlay() {
        var tempPlay = 0

        while (tempPlay == 0) {
            tempPlay = computerPlayer.playCard(computerHand, validSuit, validRank)
            if (tempPlay == 0) {
                drawCard(computerHand)
            }
        }

        if (tempPlay == 108 || tempPlay == 208 || tempPlay == 308 || tempPlay == 408) {

            validRank = 8
            validSuit = computerPlayer.chooseSuit(computerHand)
            var suitText = ""
            if (validSuit == 100) {
                suitText = "Diamonds"
            } else if (validSuit == 200) {
                suitText = "Clubs"
            } else if (validSuit == 300) {
                suitText = "Hearts"
            } else if (validSuit == 400) {
                suitText = "Spades"
            }

            Toast.makeText(ctx, "Computer chose $suitText", Toast.LENGTH_SHORT).show()

        } else {
            validSuit = Math.round(((tempPlay / 100) * 100).toDouble()).toInt()
            validRank = tempPlay - validSuit
        }

        for (i in 0 until computerHand.size) {
            var tempCard = computerHand[i]
            if (tempPlay == tempCard.id) {
                discardPile.add(0, computerHand[i])
                computerHand.removeAt(i)
                break
            }

        }


        if (computerHand.isEmpty()) {
            endHand()
        }


        myTurn = true


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


        // Show the moving card


        for (i in playerHand.indices) {

            if (i == movingIdx) {
                canvas.drawBitmap(playerHand[i].bmp, movingX, movingY, null)
            } else {
                if (i < 7) {
                    canvas.drawBitmap(
                        playerHand[i].bmp, i * (scaledCW + 5).toFloat(),
                        scrH - scaledCH - paint.textSize - (50 * scale), null
                    )
                }
            }
            invalidate()
            setToFullScreen()


        }


        // Draw the arrow when the player`s cards exceed seven

        if (playerHand.size > 7) {

            canvas.drawBitmap(
                nextCardBtn, scrW - nextCardBtn.width - (30 * scale),
                scrH - nextCardBtn.height - scaledCH - (90 * scale), null
            )

        }

        for (i in playerHand.indices) {
            if (i == movingIdx) {
                canvas.drawBitmap(playerHand.get(i).bmp, movingX, movingY, null)
            } else {
                if (i < 7) {
                    canvas.drawBitmap(
                        playerHand.get(i).bmp,
                        i * (scaledCW + 5).toFloat(),
                        scrH - scaledCH - paint.textSize - (50 * scale), null
                    )
                }
            }
        }


        // Draw scores

        canvas.drawText(
            "Opponent Score: $computerScore", 10F,
            paint.textSize - 10, paint
        )

        canvas.drawText(
            "My Score: $myScore", 10F,
            scrH - paint.textSize - 10, paint
        )


    }


    override fun onTouchEvent(event: MotionEvent): Boolean {


        var eventaction = event.action
        var X = event.x
        var Y = event.y




        when (eventaction) {

            MotionEvent.ACTION_DOWN -> if (myTurn) {
                for (i in 0..6) {
                    if (X > i * (scaledCW + 5) && X < i * (scaledCW + 5) + scaledCW &&
                        Y > scrH - scaledCH - paint.textSize - (50 * scale)
                    ) {

                        movingIdx = i
                        movingX = X - (30 * scale)
                        movingY = Y - (70 * scale)

                    }
                }
            }


            MotionEvent.ACTION_MOVE -> {
                movingX = X - (30 * scale)
                movingY = Y - (70 * scale)
            }


            MotionEvent.ACTION_UP -> {
                if (movingIdx > -1 &&
                    X > (scrW / 2) - (100 * scale) &&
                    X < (scrW / 2) + (100 * scale) &&
                    Y > (scrH / 2) - (100 * scale) &&
                    Y < (scrH / 2) + (100 * scale) && (
                            playerHand.get(movingIdx).rank == 8 ||
                                    playerHand.get(movingIdx).rank == validRank ||
                                    playerHand.get(movingIdx).suit == validSuit)
                ) {
                    validRank = playerHand.get(movingIdx).rank
                    validRank = playerHand.get(movingIdx).suit
                    discardPile.add(0, playerHand.get(movingIdx))
                    playerHand.removeAt(movingIdx)
                    if (playerHand.isEmpty()) {
                        endHand()
                    } else {
                        if (validRank == 8) {
                            changeSuit()
                        } else {
                            myTurn = false
                            computerPlay()
                        }
                    }


                }

                if (movingIdx > -1 && myTurn &&
                    X > (scrW / 2) - (100 * scale) &&
                    X < (scrW / 2) + (100 * scale) &&
                    Y > (scrH / 2) - (100 * scale) &&
                    Y < (scrH / 2) + (100 * scale)
                ) {

                    if (isValidDraw()) {
                        drawCard(playerHand)

                    } else {
                        Toast.makeText(ctx, "You have a valid play.", Toast.LENGTH_SHORT)
                            .show()
                    }


                }


            }

        }

        invalidate()
        return true


    }


    private fun setToFullScreen() {
        systemUiVisibility = (SYSTEM_UI_FLAG_LOW_PROFILE
                or SYSTEM_UI_FLAG_FULLSCREEN
                or SYSTEM_UI_FLAG_LAYOUT_STABLE
                or SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        Log.d(javaClass.name, "setToFullScreen called")

    }


    private fun changeSuit() {

        val changeSuitDlg = Dialog(ctx)
        changeSuitDlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        changeSuitDlg.setContentView(R.layout.choose_suit_dialog)
        val spinner: Spinner = changeSuitDlg.findViewById(R.id.suitSpinner)
        var adapter: ArrayAdapter<CharSequence> =
            ArrayAdapter.createFromResource(
                ctx,
                R.array.suits,
                android.R.layout.simple_spinner_item
            )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        var okButton: Button = changeSuitDlg.findViewById(R.id.okButton)

        okButton.setOnClickListener {
            validSuit = (spinner.selectedItemPosition + 1) * 100
            var suitText = ""
            if (validSuit == 100) {
                suitText = "Diamonds"
            } else if (validSuit == 200) {
                suitText = "Clubs"
            } else if (validSuit == 300) {
                suitText = "Hearts"
            } else if (validSuit == 400) {
                suitText = "Spades"
            }

            changeSuitDlg.dismiss()
            Toast.makeText(ctx, "You chose $suitText", Toast.LENGTH_SHORT)
                .show()
            myTurn = false
            computerPlay()

        }


    }


    private fun isValidDraw(): Boolean {
        var canDraw: Boolean = true
        for (i in playerHand.indices) {
            val tempId = playerHand[i].id
            val tempRank = playerHand[i].rank
            val tempSuit = playerHand[i].suit
            if (validSuit == tempSuit || validRank == tempRank ||
                tempId == 108 || tempId == 208 || tempId == 308 || tempId == 408
            ) {
                canDraw = false
            }
        }

        return canDraw

    }


    private fun updateScores() {
        for (i in playerHand.indices) {
            computerScore += playerHand[i].getScoreValue()
            currScore += playerHand[i].getScoreValue()
        }

        for (i in computerHand.indices) {
            myScore += computerHand[i].getScoreValue()
            currScore += computerHand[i].getScoreValue()
        }


    }


    private fun endHand() {

        var endHandMsg = ""
        val endHandDlg: Dialog = Dialog(ctx)
        endHandDlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        endHandDlg.setContentView(R.layout.end_hand_dialog)
        updateScores()


        var endHandText: TextView = endHandDlg.findViewById(R.id.endHandText)
        if (playerHand.isEmpty()) {
            if (myScore >= 300) {
                endHandMsg = String.format("You won. You have %d points. Play again?", myScore)
            } else {
                endHandMsg = String.format("You lost, you only got %d", currScore)
            }
        } else if (computerHand.isEmpty()) {
            if (computerScore >= 300) {
                endHandMsg = String.format(
                    "Opponent scored %d. You lost. Play again?",
                    computerScore
                )
            } else {
                endHandMsg = String.format("Opponent has lost. He scored %d points.", currScore)
            }
            endHandText.text = endHandMsg
        }


        var nextHandBtn: Button = endHandDlg.findViewById(R.id.nextHandButton)

        if (computerScore >= 300 || myScore >= 300) {
            nextHandBtn.text = "New Game"
        }



        nextHandBtn.setOnClickListener {
            if (computerScore >= 300 || myScore >= 300) {
                myScore = 0
                computerScore = 0
            }

            initNewHand()
            endHandDlg.dismiss()


        }

        endHandDlg.show()


    }


    private fun initNewHand() {
        currScore = 0

        if (playerHand.isEmpty()) {
            myTurn = true
        } else if (computerHand.isEmpty()) {
            myTurn = false
        }

        deck.addAll(discardPile)
        deck.addAll(playerHand)
        deck.addAll(computerHand)
        discardPile.clear()
        playerHand.clear()
        computerHand.clear()
        dealCards()
        drawCard(discardPile)
        validSuit = discardPile[0].suit
        validRank = discardPile[0].rank
        if (!myTurn) {
            computerPlay()
        }

    }


}