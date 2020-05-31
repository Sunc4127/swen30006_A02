package strategies;

import ch.aplu.jcardgame.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DumbStrategy implements IPlayStrategy {
    @Override
    public Card selectCard(Hand hand) {
        final Random random = ThreadLocalRandom.current();
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}