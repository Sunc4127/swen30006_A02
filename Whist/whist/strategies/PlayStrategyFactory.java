package strategies;

public class PlayStrategyFactory {
    
    private static IPlayStrategy playStrategy = null;

    private static  PlayStrategyFactory instance;

    // Create strategies based on playType
    public  IPlayStrategy getPlayStrategy(String playType) {
        if (playType.equals("Random")) {
            playStrategy = new RandStrategy();
        } else if (playType.equals("Legal")) {
            playStrategy = new LegalStrategy();
        } else if (playType.equals("Smart")) {
            playStrategy = new SmartStrategy();
        }
        return playStrategy;
    }

    // Singleton pattern
    public static PlayStrategyFactory getInstance() {
        if (instance == null)
            instance = new PlayStrategyFactory();
        return instance;
    }
}