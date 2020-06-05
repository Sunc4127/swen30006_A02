package Player;

import strategies.IPlayStrategy;

public class RandNPC implements  INPC {
    private static IPlayStrategy playStrategy;

    public RandNPC(IPlayStrategy playStrategy) {
        this.playStrategy = playStrategy;
    }

    @Override
    public IPlayStrategy getPlayStrategy() {
        return playStrategy;
    }
}
