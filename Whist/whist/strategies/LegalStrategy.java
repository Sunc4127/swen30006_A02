package strategies;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Random;

public class LegalStrategy implements IPlayStrategy {
    @Override
    public Card selectCard(Hand hand) {

        Random random = null;
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}