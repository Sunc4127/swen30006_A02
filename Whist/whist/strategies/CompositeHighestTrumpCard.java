package strategies;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import static Whist.Whist.getTrumps;

/**Composite class for smartStrategy
 * Find the highest rank Trump card in a Card ArrayList
 * */
public class CompositeHighestTrumpCard extends CompositeSmartStrategy {
    @Override
    public Card selectCard(ArrayList<Card> cardList) {
        Card highest = null;
        for( Card c: cardList ){
            if( c.getSuit() == getTrumps() ){
                if( highest == null ){
                    highest = c;
                }else if( c.getRankId() < highest.getRankId() ){
                    highest = c;
                }
            }
        }
        return highest;
    }
}
