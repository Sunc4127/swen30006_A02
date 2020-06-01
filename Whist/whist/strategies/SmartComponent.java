package strategies;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;

public abstract class SmartComponent {
    public abstract Card selectCard(ArrayList<Card> cardList) throws NoTrumpCardOnTableException;
}
