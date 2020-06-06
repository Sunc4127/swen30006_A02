package Player;

import Whist.Property;
import strategies.PlayStrategyFactory;

import java.io.IOException;
import java.util.HashMap;

public class NPCFactory {
    private static final int RANDOM = 0;
    private static final int LEGAL = 1;
    private static final int SMART = 2;
    private static NPCFactory instance;

    public HashMap<Integer, INPC> getNPCList() throws  IOException{
        int[] positionArray;

        HashMap<Integer, INPC> NPCList = new HashMap<>();

        PlayStrategyFactory playStrategyFactory = strategies.PlayStrategyFactory.getInstance();

        // Based on the properties, create NPCs and store them to the NPC array with their specific play strategy
        positionArray = Property.getPropertyArray("playerPosition");
        for(int i = 0; i < positionArray.length; i++){
            if(positionArray[i] == RANDOM){
                NPCList.put(i, new RandNPC(playStrategyFactory.getPlayStrategy("Random")));
            }
            else if(positionArray[i] == LEGAL){
                NPCList.put(i, new LegalNPC(playStrategyFactory.getPlayStrategy("Legal")));
            }
            else if(positionArray[i] == SMART){
                NPCList.put(i, new SmartNPC(playStrategyFactory.getPlayStrategy("Smart")));
            }
        }

        return NPCList;

    }

    // Singleton pattern
    public static NPCFactory getInstance() {
        if (instance == null)
            instance = new NPCFactory();
        return instance;
    }

}
