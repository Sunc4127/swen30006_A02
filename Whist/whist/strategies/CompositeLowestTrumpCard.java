package strategies;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;

import static Whist.Whist.getTrumps;
import static Whist.Whist.getWinningCard;

/**Composite class for smartStrategy
 * Find the lowest rank Trump card that has a higher rank
 * then the winning card on table in a Card ArrayList
 * */
public class CompositeLowestTrumpCard extends CompositeSmartStrategy {
    @Override
    public Card selectCard(ArrayList<Card> cardList){ //throws NoTrumpCardOnTableException {
        Card lowest = null;
        Card highestTrumpOnTable = getWinningCard();

        for( Card c: cardList ){
            if( c.getSuit() == getTrumps() ){
                if( lowest == null  && c.getRankId() < highestTrumpOnTable.getRankId() ){
                    lowest = c;
                }else if( c.getRankId() > lowest.getRankId() && c.getRankId() < highestTrumpOnTable.getRankId() ){
                    lowest = c;
                }
            }
        }
        return lowest;
    }
}
