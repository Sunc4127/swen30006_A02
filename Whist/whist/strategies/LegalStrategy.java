package strategies;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.Random;

import static Whist.Whist.getLeadSuit;
import static Whist.Whist.getRandom;

public class LegalStrategy implements IPlayStrategy {

    @Override
    public Card selectCard(ArrayList<Card> cardList) {

        // legal cards in hand
        ArrayList<Card> legalCards = new ArrayList<>();

        // Check the number of cards from the same suit as the lead card
        for( Card c: cardList ){
            if( c.getSuit() == getLeadSuit() )
                legalCards.add(c);
        }


        final Random random = getRandom();
        int position;
        // If no car is from the same suit, selected a card randomly
        if (legalCards.size() <1 ) {
            position = random.nextInt(cardList.size());
            return cardList.get(position);
        } else {
            // If is, randomly select a card from the temporary cards holder
            position = random.nextInt(legalCards.size());
            return legalCards.get(position);
        }
    }
}