package strategies;

import Whist.Suit;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static Whist.Whist.getLeadSuit;

public class LegalStrategy implements IPlayStrategy {
    public static final int START_CARDS = 13;
    @Override
    public Card selectCard(ArrayList<Card> cardList) {

        Card[] cards = new Card[START_CARDS];
        int y = 0;
        for (Card c: cardList) {
            if (c.getSuit() == getLeadSuit()) {
                cards[y] = c;
                y += 1;
            }
        }

        final Random random = ThreadLocalRandom.current();
        int position = 0;
        if (y == 0) {
            position = random.nextInt(cardList.size());
            return cardList.get(position);
        } else {
            position = random.nextInt(y);
            return cards[position];
        }
    }
}