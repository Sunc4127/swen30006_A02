package strategies;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;

public abstract class CompositeSmartStrategy implements IPlayStrategy {
    public abstract Card selectCard(ArrayList<Card> cardList) ;
}
