package strategies;
import ch.aplu.jcardgame.*;

public interface IPlayStrategy {
    public Card selectCard(Hand hand);
}