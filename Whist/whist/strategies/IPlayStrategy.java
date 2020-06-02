package strategies;
import ch.aplu.jcardgame.*;
import java.util.ArrayList;

public interface IPlayStrategy {
    public Card selectCard(ArrayList<Card> cardList);
}