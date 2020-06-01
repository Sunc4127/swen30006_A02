package strategies;

import ch.aplu.jcardgame.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static Whist.Whist.getLeadSuit;

public class RandStrategy implements IPlayStrategy {
    @Override
    public Card selectCard(Hand hand) {
        Random random = null;
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}