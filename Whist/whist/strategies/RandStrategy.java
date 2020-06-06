package strategies;

import Whist.Whist;
import ch.aplu.jcardgame.*;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static Whist.Whist.getLeadSuit;

public class RandStrategy implements IPlayStrategy {
    @Override
    public Card selectCard(ArrayList<Card> cardList) {
        // Same code copied from Whist
        Random random = Whist.getRandom();
        int x = random.nextInt(cardList.size());
        return cardList.get(x);
    }
}