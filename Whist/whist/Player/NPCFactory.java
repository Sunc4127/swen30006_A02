package Player;

import strategies.IPlayStrategy;
import strategies.PlayStrategyFactory;
import Whist.Property;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class NPCFactory {
    private static NPCFactory instance;
    private static INPC NPC = null;

    public INPC[] getNPC() throws IOException {
        int random = Property.getProperty("randomNum");
        int legal  = Property.getProperty("legalNum");
        int smart  = Property.getProperty("smartNum");
        int numNPC = Property.getProperty("numNPC");

        INPC[] arrayNPC = new INPC[numNPC];

        PlayStrategyFactory playStrategyFactory = strategies.PlayStrategyFactory.getInstance();

        // Based on the properties, create NPCs and store them to the NPC array with their specific play strategy
        for (int i = 0; i < numNPC; i++) {
            if (random > 0 ) {
                arrayNPC[i] = new RandNPC(playStrategyFactory.getPlayStrategy("Random"));
                random--;
                continue;
            }
            if (legal > 0 ) {
                arrayNPC[i] = new LegalNPC(playStrategyFactory.getPlayStrategy("Legal"));
                legal--;
                continue;
            }
            if (smart > 0 ) {
                arrayNPC[i] = new SmartNPC(playStrategyFactory.getPlayStrategy("Smart"));
                smart--;
                continue;
            }
        }

        return arrayNPC;
    }

    // Singleton pattern
    public static NPCFactory getInstance() {
        if (instance == null)
            instance = new NPCFactory();
        return instance;
    }
}
