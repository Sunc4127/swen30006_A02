package Player;

import strategies.IPlayStrategy;

public class SmartNPC implements INPC{
    private static IPlayStrategy playStrategy;

    public SmartNPC(IPlayStrategy playStrategy) {
        this.playStrategy = playStrategy;
    }

    public IPlayStrategy getPlayStrategy() {
        return playStrategy;
    }
}
