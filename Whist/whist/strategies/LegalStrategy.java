package strategies;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import Whist.Whist;

public class LegalStrategy implements IPlayStrategy {

    @Override
    public Card selectCard(ArrayList<Card> cardList) {

        // Temporary cards holder
        Card[] cards = new Card[Whist.nbStartCards];

        // Check the number of cards from the same suit as the lead card
        int nbCards = 0;
        for (Card card: cardList) {
            if (card.getSuit() == Whist.getLeadSuit()) {
                cards[nbCards] = card;
                nbCards += 1;
            }
        }


        final Random random = ThreadLocalRandom.current();
        int position;
        // If no car is from the same suit, selected a card randomly
        if (nbCards == 0) {
            position = random.nextInt(cardList.size());
            return cardList.get(position);
        } else {
            // If is, randomly select a card from the temporary cards holder
            position = random.nextInt(nbCards);
            return cards[position];
        }
    }
}