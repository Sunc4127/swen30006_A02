package strategies;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;

/**Composite class for smartStrategy
 * Find the highest rank card in a Card ArrayList
 * */
public class CompositeHighestCard extends CompositeSmartStrategy {
    @Override
    public Card selectCard(ArrayList<Card> cardList) {
        Card highest = cardList.get(0);
        for( Card c: cardList ){
            if( c.getRankId() < highest.getRankId() ){
                highest = c;
            }
        }
        return highest;
    }
}
