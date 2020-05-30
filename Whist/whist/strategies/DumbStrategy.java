package strategies;

public class DumbStrategy implements IPlayStrategy {
    @Override
    public void selectCard() {
        System.out.println("Random!!!!!");
    }
}