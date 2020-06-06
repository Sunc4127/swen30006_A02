package strategies;

import Whist.Whist;
import ch.aplu.jcardgame.*;

import java.util.ArrayList;
import java.util.Random;


public class RandStrategy implements IPlayStrategy {
    @Override
    public Card selectCard(ArrayList<Card> cardList) {
        // random select card in hand
        Random random = Whist.getRandom();
        int x = random.nextInt(cardList.size());
        return cardList.get(x);
    }
}