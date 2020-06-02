package strategies;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;

import static Whist.Whist.getTrumps;
import static Whist.Whist.getWinningCard;

public class CompositeLowestTrumpCard extends CompositeSmartStrategy {
    @Override
    public Card selectCard(ArrayList<Card> cardList){ //throws NoTrumpCardOnTableException {
        Card lowest = null;
        Card highestTrumpOnTable = getWinningCard();
        /**
        for(Card c: getCardOnTable() ){
            if( c.getSuit() == getTrumps() ){
                if( highestTrumpOnTable == null || c.getRankId() < highestTrumpOnTable.getRankId())
                    highestTrumpOnTable = c;
            }
        }
         */
        /**
        if( highestTrumpOnTable == null) {
            throw new NoTrumpCardOnTableException();
        }*/

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
