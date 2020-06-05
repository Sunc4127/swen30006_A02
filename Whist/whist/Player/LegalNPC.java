package Player;

import strategies.IPlayStrategy;

public class LegalNPC implements INPC {
    private static IPlayStrategy playStrategy;

    public LegalNPC(IPlayStrategy playStrategy) {
        this.playStrategy = playStrategy;
    }

    @Override
    public IPlayStrategy getPlayStrategy() {
        return playStrategy;
    }
}
