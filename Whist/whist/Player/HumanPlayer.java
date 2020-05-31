package Player;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import Whist.*;

public class HumanPlayer {

    private Hand hands;

    public HumanPlayer(Hand hands, Card selected) {
        this.hands = hands;
        // Set up human player for interaction
        CardListener cardListener = new CardAdapter() {
            private Card card;

            public void leftDoubleClicked(Card card) {
                Whist.setCard(card);
				hands.setTouchEnabled(false);
			}
        };
        hands.addCardListener(cardListener);
    }

    public Hand getHands() {
        return hands;
    }
    public void setHands(boolean isTouchEnabled) {
        if (isTouchEnabled)
            hands.setTouchEnabled(true);
        else
            hands.setTouchEnabled(false);
    }
}
