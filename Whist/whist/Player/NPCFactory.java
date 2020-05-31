package Player;

import strategies.DumbStrategy;
import strategies.LegalStrategy;
import strategies.SmartStrategy;

public class NPCFactory {
    private static NPCFactory instance;
    private static IPlayer player = null;

    public IPlayer getPlayer(String playType) {
        if (playType.equals("random")) {
            player = new RandNPC();
        } else if (playType.equals("Legal")) {
            player = new LegalNPC();
        } else if (playType.equals("Smart")) {
            player = new SmartNPC();
        }

        return player;
    }


    public static NPCFactory getInstance() {
        if (instance == null)
            instance = new NPCFactory();
        return instance;
    }
}
