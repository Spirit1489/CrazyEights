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


}