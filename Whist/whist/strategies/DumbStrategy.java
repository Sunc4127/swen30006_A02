package strategies;

import ch.aplu.jcardgame.*;
import java.util.Random;

public class DumbStrategy implements IPlayStrategy {
    @Override
    public Card selectCard(Hand hand) {
        Random random = null;
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}