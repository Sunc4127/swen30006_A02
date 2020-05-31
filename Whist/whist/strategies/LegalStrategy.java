package strategies;

import Whist.Suit;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static Whist.Whist.getLeadSuit;

public class LegalStrategy implements IPlayStrategy {
    @Override
    public Card selectCard(Hand hand) {

        Card[] cards = new Card[13];
        int y = 0;
        for (int i = 0; i < hand.getNumberOfCards(); i++) {
            if (hand.get(i).getSuit() == getLeadSuit()) {
                cards[y] = hand.get(i);
                y += 1;
            }
        }

        final Random random = ThreadLocalRandom.current();
        int position = 0;
        if (y == 0) {
            position = random.nextInt(hand.getNumberOfCards());
            return hand.get(position);
        } else {
            System.out.println(y);
            position = random.nextInt(y);
            return cards[position];
        }
    }
}