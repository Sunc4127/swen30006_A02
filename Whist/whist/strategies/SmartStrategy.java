package strategies;

import ch.aplu.jcardgame.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static Whist.Whist.getLeadSuit;

public class SmartStrategy implements IPlayStrategy {
    @Override


    public Card selectCard(Hand hand) {
        final Random random = ThreadLocalRandom.current();
        int x = random.nextInt(hand.getNumberOfCards());

        ArrayList<Card> legalCards = new ArrayList<>();
        Card highest;

        /** select lead card which means selecting highest card in hands*/
        if( getLeadSuit() == null ){
            return highestRankCard( hand.getCardList() );
        }
        else{

            for( Card c: hand.getCardList() ){
                if( c.getSuit() == getLeadSuit() )
                    legalCards.add(c);
            }

            /**no legal can be selected */
            if( legalCards.size() < 1 ){
                return lowestRankCard( hand.getCardList() );
            }
            else{
                return highestRankCard( legalCards );
            }

        }
    }

    public Card highestRankCard(List<Card> cardList){
        Card highest = cardList.get(0);
        for( Card c: cardList ){
            if( c.getRankId() > highest.getRankId() ){
                highest = c;
            }
        }
        return highest;
    }

    public Card lowestRankCard(List<Card> cardList){
        Card lowest = cardList.get(0);
        for( Card c: cardList ){
            if( c.getRankId() <= lowest.getRankId() ){
                lowest = c;
            }
        }
        return lowest;
    }


}




