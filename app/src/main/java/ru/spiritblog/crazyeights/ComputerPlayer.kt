package ru.spiritblog.crazyeights

public class ComputerPlayer {

    public fun playCard(hand: List<Card>, suit: Int, rank: Int): Int {
        var play = 0

        for (i in hand.indices) {

            var tempId = hand.get(i).id
            var tempRank = hand.get(i).rank
            var tempSuit = hand.get(i).suit

            if (tempRank != 8) {
                if (rank == 8) {
                    if (suit == tempSuit) {
                        play = tempId
                    }

                } else if (suit == tempSuit || rank == tempRank) {
                    play = tempId
                }

            }

        }


        if (play == 0) {

            for (i in hand.indices) {
                var tempId = hand.get(i).id
                if (tempId == 108 || tempId == 208 ||
                    tempId == 308 || tempId == 408
                ) {
                    play = tempId
                }

            }

        }

        return play

    }


    fun chooseSuit(hand: List<Card>): Int {
        var suit = 100
        var numDiamonds = 0
        var numClubs = 0
        var numHearts = 0
        var numSpades = 0
        for (i in hand.indices) {
            val tempRank = hand[i].rank
            val tempSuit = hand[i].suit
            if (tempRank != 8) {
                if (tempSuit == 100) {
                    numDiamonds++
                } else if (tempSuit == 200) {
                    numClubs++
                } else if (tempSuit == 300) {
                    numHearts++
                } else if (tempSuit == 400) {
                    numSpades++
                }
            }
        }
        if (numClubs > numDiamonds && numClubs > numHearts && numClubs > numSpades) {
            suit = 200
        } else if (numHearts > numDiamonds && numHearts > numClubs && numHearts > numSpades) {
            suit = 300
        } else if (numSpades > numDiamonds && numSpades > numClubs && numSpades > numHearts) {
            suit = 400
        }
        return suit
    }









}