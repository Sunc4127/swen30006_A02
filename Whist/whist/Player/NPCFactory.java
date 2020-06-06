package Player;

import Whist.Property;
import strategies.PlayStrategyFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

    public HashMap<Integer, INPC> getNPCList() throws  IOException{
        int random = Property.getProperty("randomNum");
        int legal  = Property.getProperty("legalNum");
        int smart  = Property.getProperty("smartNum");
        int numNPC = Property.getProperty("numNPC");
        int[] positionArray;

        HashMap<Integer, INPC> NPCList = new HashMap<>();
        INPC[] arrayNPC = new INPC[numNPC];

        PlayStrategyFactory playStrategyFactory = strategies.PlayStrategyFactory.getInstance();

        // Based on the properties, create NPCs and store them to the NPC array with their specific play strategy
        positionArray = Property.getPropertyArray("playerPosition");
        for(int i = 0; i < positionArray.length; i++){
            //NPCList.put(i,);
            if(positionArray[i] == 0){
                NPCList.put(i, new RandNPC(playStrategyFactory.getPlayStrategy("Random")));
            }
            else if(positionArray[i] == 1){
                NPCList.put(i, new LegalNPC(playStrategyFactory.getPlayStrategy("Legal")));
            }
            else if(positionArray[i] == 2){
                NPCList.put(i, new SmartNPC(playStrategyFactory.getPlayStrategy("Smart")));
            }
        }
        /**
        if (Property.ifPropertyExist("playerPosition")) {
            positionArray = Property.getPropertyArray("playerPosition");
            for(int i = 0; i < positionArray.length && positionArray[i] != 3; i++){
                //NPCList.put(i,);
                if(positionArray[i] == 0){
                    NPCList.put(i, new RandNPC(playStrategyFactory.getPlayStrategy("Random")));
                }
                else if(positionArray[i] == 1){
                    NPCList.put(i, new LegalNPC(playStrategyFactory.getPlayStrategy("Legal")));
                }
                else if(positionArray[i] == 2){
                    NPCList.put(i, new SmartNPC(playStrategyFactory.getPlayStrategy("Smart")));
                }
            }
        }
        else{
            for (int i = 0; i < numNPC; i++) {
                if (random > 0 ) {
                    NPCList.put(i, new RandNPC(playStrategyFactory.getPlayStrategy("Random")));
                    random--;
                    continue;
                }
                if (legal > 0 ) {
                    NPCList.put(i, new LegalNPC(playStrategyFactory.getPlayStrategy("Legal")));
                    legal--;
                    continue;
                }
                if (smart > 0 ) {
                    NPCList.put(i, new SmartNPC(playStrategyFactory.getPlayStrategy("Smart")));
                    smart--;
                    continue;
                }
            }
        }
        */
        return NPCList;

    }

    // Singleton pattern
    public static NPCFactory getInstance() {
        if (instance == null)
            instance = new NPCFactory();
        return instance;
    }
}
