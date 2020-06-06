package strategies;

import ch.aplu.jcardgame.*;

import java.util.ArrayList;

import static Whist.Whist.getLeadSuit;
import static Whist.Whist.getTrumps;
import static Whist.Whist.getCardOnTable;
import static Whist.Whist.getWinningCard;

public class SmartStrategy extends CompositeSmartStrategy {
    CompositeHighestCard highestCard = new CompositeHighestCard();
    CompositeLowestCard lowestCard = new CompositeLowestCard();
    CompositeHighestTrumpCard highestTrumpCard = new CompositeHighestTrumpCard();
    CompositeLowestTrumpCard lowestTrumpCard = new CompositeLowestTrumpCard();

    @Override
    public Card selectCard(ArrayList<Card> cardList) {
        // legal cards in hand
        ArrayList<Card> legalCards = new ArrayList<>();
        // trump cards in hand
        ArrayList<Card> trumpCards = new ArrayList<>();
        // non-trump cards in hand
        ArrayList<Card> NonTrumpCards = new ArrayList<>();

        for(Card c: cardList){
            if( c.getSuit() == getTrumps() ){
                trumpCards.add(c);
            }else{
                NonTrumpCards.add(c);
            }
        }

        /** select lead card which means selecting highest card in hands*/
        if( getLeadSuit() == null ){
            if(NonTrumpCards.size() > 0)
                return highestCard.selectCard( NonTrumpCards );
            else
                return highestCard.selectCard( cardList );
        }
        else{

            for( Card c: cardList ){
                if( c.getSuit() == getLeadSuit() )
                    legalCards.add(c);
            }

            /**no legal can be selected */
            if( legalCards.size() < 1 ){
                /**having trump card to play*/
                if( getTrumps() != getLeadSuit() && trumpCards.size() > 0){
                    /**there is no trump card on table*/
                    if(highestTrumpCard.selectCard( getCardOnTable() ) == null ){
                        return lowestCard.selectCard( trumpCards );
                    }
                    else{
                        /**there is a higher rank trump card on table */
                        if(highestTrumpCard.selectCard( getCardOnTable() ).getRankId() <=
                                highestCard.selectCard( trumpCards ).getRankId()){
                            return lowestCard.selectCard( NonTrumpCards);
                        }
                        else{
                            return highestCard.selectCard( trumpCards );
                        }
                    }
                }
                /**do not have trump card to play */
                else{
                    return lowestCard.selectCard( cardList );
                }

            }
            else{
                /**has a higher legal card*/
                if(highestCard.selectCard( legalCards ).getRankId()
                        < getWinningCard().getRankId() ){
                   return highestCard.selectCard( legalCards );
                }
                /** do not have a higher legal card*/
                else{
                    return lowestCard.selectCard( legalCards );
                }

            }
        }
    }
}




