package strategies;

/**
 * An exception thrown when there is no trump card on table
 * but player try to find a trump card on table
 */
public class NoTrumpCardOnTableException extends Exception {
    public NoTrumpCardOnTableException(){
        super("No Trump Card On Table!");
    }
}
