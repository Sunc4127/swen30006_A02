package strategies;

import ch.aplu.jcardgame.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import static Whist.Whist.getLeadSuit;
import static Whist.Whist.getTrumps;
import static Whist.Whist.getCardOnTable;
import static Whist.Whist.getCardPlayed;
import static Whist.Whist.getWinningCard;

public class SmartStrategy extends SmartComponent implements IPlayStrategy {
    HighestCard highestCard = new HighestCard();
    LowestCard lowestCard = new LowestCard();
    HighestTrumpCard highestTrumpCard = new HighestTrumpCard();
    LowestTrumpCard lowestTrumpCard = new LowestTrumpCard();


    @Override
    public Card selectCard(Hand hand) {

        return selectCard(hand.getCardList());

    }

    /**
     * 考虑 其他对手的牌（matrix）
     *
     * trump牌
     *
     * 其他人最少的suit
     *
     * 队友积分快赢了
     *
     * 选lead的时候 选自己最多花色的最大的
     *
     *
     * */




    @Override
    public Card selectCard(ArrayList<Card> cardList) {
        ArrayList<Card> legalCards = new ArrayList<>();
        ArrayList<Card> trumpCards = new ArrayList<>();
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
            /**there is a legal move*/
            else{
                /** there is a trump card on table*/
                if( highestTrumpCard.selectCard( getCardOnTable()) != null ){
                    /** has higher trump card*/
                    if(trumpCards.size() > 0 && highestTrumpCard.selectCard( getCardOnTable() ).getRankId() >
                        highestCard.selectCard( trumpCards).getRankId() ){
                        return lowestTrumpCard.selectCard( trumpCards );
                    }
                    /** do not have a higher trump card*/
                    else{
                        return lowestCard.selectCard( legalCards );
                    }
                }
                /**there is no trump card on table*/
                else{
                    /** has trump card in hand*/
                    if( trumpCards.size() > 0){
                        return lowestCard.selectCard( trumpCards );
                    }
                    /** do not have trump in hand*/
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
                /**
                 * there is a trump card on table
                 *      have higher trump card
                 *          -> play the higher trump card
                 *      do not have higher trump card
                 *          -> play the lowest legal card
                 * there is no trump card on table
                 *      have trump card
                 *          -> play the lowest trump card
                 *      do not have trump card
                 *          have higher normal card
                 *              -> play the higher card
                 *          do not have higher card
                 *              -> play the lowest card
                 * */
                //return highestCard.selectCard( legalCards );
            }

        }


    }
}




