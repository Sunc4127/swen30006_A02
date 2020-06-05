package Player;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import Whist.*;

public class HumanPlayer {

    private Hand hand;

    // Create human player by adding ChardListener to its hand
    public HumanPlayer(Hand hand, Card selected) {
        this.hand = hand;
        // Set up human player for interaction
        CardListener cardListener = new CardAdapter() {
            private Card card;

            public void leftDoubleClicked(Card card) {
                Whist.setCard(card);
				hand.setTouchEnabled(false);
			}
        };
        hand.addCardListener(cardListener);
    }

    public Hand getHand() {
        return hand;
    }

    // Set TouchEnabled to the opposite boolean value
    public void setHand(boolean isTouchEnabled) {
        if (isTouchEnabled)
            hand.setTouchEnabled(true);
        else
            hand.setTouchEnabled(false);
    }
}
