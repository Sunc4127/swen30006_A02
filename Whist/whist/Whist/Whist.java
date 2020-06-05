package Whist;// Whist.Whist.java

import Player.HumanPlayer;
import Player.INPC;
import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("serial")
public class Whist extends CardGame {

	final String trumpImage[] = { "bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif" };

	static final Random random = ThreadLocalRandom.current();

	// return random Enum value
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
		int x = random.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

	// return random Card from Hand
	public static Card randomCard(Hand hand) {
		int x = random.nextInt(hand.getNumberOfCards());
		return hand.get(x);
	}

	// return random Card from ArrayList
	public static Card randomCard(ArrayList<Card> list) {
		int x = random.nextInt(list.size());
		return list.get(x);
	}

	public boolean rankGreater(Card card1, Card card2) {
		return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
	}

	private final String version = "1.0";
	public final int nbPlayers = 4;
	public final int nbStartCards = 13;
	public int winningScore = 11;
	private final int handWidth = 400;
	private final int trickWidth = 40;
	private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
	private final Location[] handLocations = { new Location(350, 625), new Location(75, 350), new Location(350, 75),
			new Location(625, 350) };
	private final Location[] scoreLocations = { new Location(575, 675), new Location(25, 575), new Location(575, 25),
			new Location(650, 575) };
	private Actor[] scoreActors = { null, null, null, null };
	private final Location trickLocation = new Location(350, 350);
	private final Location textLocation = new Location(350, 450);
	private int thinkingTime = 10;
	private Hand[] hands;
	private Location hideLocation = new Location(-500, -500);
	private Location trumpsActorLocation = new Location(50, 50);
	private boolean enforceRules = false;

	/** cards that had been played */
	private  static ArrayList<Card> cardPlayed = new ArrayList<>();

	/** recently played cards */
	private static ArrayList<Card> cardOnTable = new ArrayList<>();

	public static ArrayList<Card> getCardPlayed(){
		return cardPlayed;
	}

	public static ArrayList<Card> getCardOnTable(){
		return cardOnTable;
	}

	public void setStatus(String string) {
		setStatusText(string);
	}

	private int[] scores = new int[nbPlayers];

	Font bigFont = new Font("Serif", Font.BOLD, 36);

	private void initScore() {
		for (int i = 0; i < nbPlayers; i++) {
			scores[i] = 0;
			scoreActors[i] = new TextActor("0", Color.WHITE, bgColor, bigFont);
			addActor(scoreActors[i], scoreLocations[i]);
		}
	}

	private void updateScore(int player) {
		removeActor(scoreActors[player]);
		scoreActors[player] = new TextActor(String.valueOf(scores[player]), Color.WHITE, bgColor, bigFont);
		addActor(scoreActors[player], scoreLocations[player]);
	}

	private static Card selected;
	private HumanPlayer humanPlayer;

	public static void setCard(Card card) {
		selected = card;
	}

	private INPC[] arrayNPC = null;

	private void initRound() throws IOException {
		hands = deck.dealingOut(nbPlayers, nbStartCards); // Last element of hands is leftover cards; these are ignored
		for (int i = 0; i < nbPlayers; i++) {
			hands[i].sort(Hand.SortType.SUITPRIORITY, true);
		}

		Property.readPropertyFile("whist/"+"smart.properties");
		winningScore = Property.getProperty("winningScore");
		thinkingTime = Property.getProperty("thinkingTime");

		arrayNPC = Player.NPCFactory.getInstance().getNPC();
		if (arrayNPC.length != 4)
			humanPlayer = new HumanPlayer(hands[0], selected);

		// graphics
		RowLayout[] layouts = new RowLayout[nbPlayers];
		for (int i = 0; i < nbPlayers; i++) {
			layouts[i] = new RowLayout(handLocations[i], handWidth);
			layouts[i].setRotationAngle(90 * i);
			// layouts[i].setStepDelay(10);
			hands[i].setView(this, layouts[i]);
			hands[i].setTargetArea(new TargetArea(trickLocation));
			hands[i].draw();
		}
	}

	private static Suit lead = null;
	public static Suit getLeadSuit() {
		return lead;
	}

	private static Suit trumps = null;
	public static Suit getTrumps(){
		return trumps;
	}

	private static Card winningCard = null;
	public static Card getWinningCard(){
		return winningCard;
	}

	private Optional<Integer> playRound() { // Returns winner, if any
		// Select and display trump suit
		trumps = randomEnum(Suit.class);
		final Actor trumpsActor = new Actor("sprites/" + trumpImage[trumps.ordinal()]);
		addActor(trumpsActor, trumpsActorLocation);
		// End trump suit
		Hand trick;
		int winner;

		int nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round
		for (int i = 0; i < nbStartCards; i++) {
			trick = new Hand(deck);
			selected = null;

			if (0 == nextPlayer && arrayNPC.length != 4) { // Select lead depending on player type
				humanPlayer.setHand(true);
				setStatus("Player 0 double-click on card to lead.");
				while (null == selected)
					delay(100);
			} else {
				setStatusText("Player " + nextPlayer + " thinking...");
				delay(thinkingTime);
				//selected = randomCard(hands[nextPlayer]);
				int position = nextPlayer;
				if (arrayNPC.length != 4)
					position -= 1;
				selected = arrayNPC[position].getPlayStrategy().selectCard(hands[nextPlayer].getCardList());
			}
			// Lead with selected card
			trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards() + 2) * trickWidth));
			trick.draw();
			selected.setVerso(false);
			// No restrictions on the card being lead
			lead = (Suit) selected.getSuit();
			selected.transfer(trick, true); // transfer to trick (includes graphic effect)
			winner = nextPlayer;
			winningCard = selected;
			cardOnTable.add(selected);
			cardPlayed.add(selected);
			// End Lead
			for (int j = 1; j < nbPlayers; j++) {
				if (++nextPlayer >= nbPlayers)
					nextPlayer = 0; // From last back to first
				selected = null;
				if (0 == nextPlayer && arrayNPC.length != 4) {
					humanPlayer.setHand(true);
					setStatus("Player 0 double-click on card to follow.");
					while (null == selected)
						delay(100);
				} else {
					int position = nextPlayer;
					if (arrayNPC.length != 4)
						position -= 1;
					setStatusText("Player " + nextPlayer + " thinking...");
					delay(thinkingTime);
					selected = arrayNPC[position].getPlayStrategy().selectCard(hands[nextPlayer].getCardList() );
				}
				// Follow with selected card
				trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards() + 2) * trickWidth));
				trick.draw();
				selected.setVerso(false); // In case it is upside down
				// Check: Following card must follow suit if possible
				if (selected.getSuit() != lead && hands[nextPlayer].getNumberOfCardsWithSuit(lead) > 0) {
					// Rule violation
					String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selected;
					System.out.println(violation);
					if (enforceRules)
						try {
							throw (new BrokeRuleException(violation));
						} catch (BrokeRuleException e) {
							e.printStackTrace();
							System.out.println("A cheating player spoiled the game!");
							System.exit(0);
						}
				}
				// End Check
				selected.transfer(trick, true); // transfer to trick (includes graphic effect)
				System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + winningCard.getRankId());
				System.out.println(" played: suit = " + selected.getSuit() + ", rank = " + selected.getRankId());
				if ( // beat current winner with higher card
				(selected.getSuit() == winningCard.getSuit() && rankGreater(selected, winningCard)) ||
				// trumped when non-trump was winning
						(selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
					System.out.println("NEW WINNER");
					winner = nextPlayer;
					winningCard = selected;
				}
				// End Follow
				cardOnTable.add(selected);
				cardPlayed.add(selected);
			}
			/** initialize lead, winningCard and cardOnTable's value*/
			winningCard = null;
			lead = null;
			cardOnTable= new ArrayList<>();

			delay(600);
			trick.setView(this, new RowLayout(hideLocation, 0));
			trick.draw();
			nextPlayer = winner;
			setStatusText("Player " + nextPlayer + " wins trick.");
			scores[nextPlayer]++;
			updateScore(nextPlayer);
			if (winningScore == scores[nextPlayer])
				return Optional.of(nextPlayer);
		}
		cardPlayed = new ArrayList<>();
		removeActor(trumpsActor);
		return Optional.empty();
	}

	public Whist() throws IOException {
		super(700, 700, 30);
		setTitle("Whist.Whist (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
		setStatusText("Initializing...");
		initScore();
		Optional<Integer> winner;
		do {
			initRound();
			winner = playRound();
		} while (!winner.isPresent());
		addActor(new Actor("sprites/gameover.gif"), textLocation);
		setStatusText("Game over. Winner is player: " + winner.get());
		refresh();
	}

	public static void main(String[] args) throws IOException {
		new Whist();
	}

}
