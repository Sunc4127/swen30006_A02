package strategies;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;

/**Composite class for smartStrategy
 * Find the lowest rank card in a Card ArrayList
 * */
public class CompositeLowestCard extends CompositeSmartStrategy {
    @Override
    public Card selectCard(ArrayList<Card> cardList) {
        Card lowest = cardList.get(0);
        for( Card c: cardList ){
            if( c.getRankId() > lowest.getRankId() ){
                lowest = c;
            }
        }
        return lowest;
    }
}
