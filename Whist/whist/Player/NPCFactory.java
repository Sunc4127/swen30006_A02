package Player;

import strategies.IPlayStrategy;
import strategies.PlayStrategyFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class NPCFactory {
    private static NPCFactory instance;
    private static INPC NPC = null;
    private int randomNum = 0;
    private int legalNum  = 0;
    private int smartNum  = 0;
    private int numNPC    = 0;

    public INPC[] getNPC(String fileName) throws IOException {

        readPropertyFile(fileName);
        int random = randomNum;
        int legal  = legalNum;
        int smart  = smartNum;

        INPC[] arrayNPC = new INPC[numNPC];

        PlayStrategyFactory playStrategyFactory = strategies.PlayStrategyFactory.getInstance();

        for (int i = 0; i < numNPC; i++) {
            if (random > 0 ) {
                arrayNPC[i] = new RandNPC(playStrategyFactory.getPlayStrategy("Random"));
                continue;
            }
            if (legal > 0 ) {
                arrayNPC[i] = new LegalNPC(playStrategyFactory.getPlayStrategy("Legal"));
                continue;
            }
            if (smart > 0 ) {
                arrayNPC[i] = new SmartNPC(playStrategyFactory.getPlayStrategy("Smart"));
                continue;
            }
        }
        Collections.shuffle(Arrays.asList(arrayNPC));

        return arrayNPC;
    }


    public static NPCFactory getInstance() {
        if (instance == null)
            instance = new NPCFactory();
        return instance;
    }

    private void readPropertyFile(String fileName) throws IOException {
        Properties NPCProperties = new Properties();
        FileReader inStream = null;
        try {
            inStream = new FileReader(fileName);
            NPCProperties.load(inStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
        randomNum = Integer.parseInt(NPCProperties.getProperty("randomNum"));
        legalNum  = Integer.parseInt(NPCProperties.getProperty("legalNum"));
        smartNum  = Integer.parseInt(NPCProperties.getProperty("smartNum"));
        numNPC    = Integer.parseInt(NPCProperties.getProperty("numNPC"));
    }
}
