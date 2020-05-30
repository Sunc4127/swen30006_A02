package strategies;

public class PlayStrategyFactory {
    
    public IPlayStrategy playStrategy = null;

    public static  PlayStrategyFactory instance; 


    public  IPlayStrategy getPlayStrategy(String playType) {
        if (playType.equals("random")) {
            playStrategy = new DumbStrategy();
        } else if (playType.equals("Legal")) {
            playStrategy = new LegalStrategy();
        } else if (playType.equals("Smart")) {
            playStrategy = new SmartStrategy();
        }

        return playStrategy;
    }

    public static PlayStrategyFactory getInstance() {
        if (instance == null)
            instance = new PlayStrategyFactory();
        return instance;
    }
}