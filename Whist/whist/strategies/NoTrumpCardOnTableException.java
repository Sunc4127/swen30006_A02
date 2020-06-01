package strategies;

import Player.NPCFactory;

public class NoTrumpCardOnTableException extends Exception {
    public NoTrumpCardOnTableException(){
        super("No Trump Card On Table!");
    }
}
