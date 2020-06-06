package Whist;// Whist.Whist.java

import Player.HumanPlayer;
import Player.INPC;
import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("serial")
public class Whist extends CardGame {
	private static final int HUMAN_PLAYER = 3;
	/**control the chang of random seed, basically it can be any other positive integer that greater than 1*/
	private static final int SEED_CHANGE = 7;

	final String trumpImage[] = { "bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif" };

	static Random random ;
	public static Random getRandom(){
		return random;
	}

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
		Card selected = list.remove(x);
		return selected;
	}

	private final String version = "2.0";
	private final int nbPlayers = 4;
	private static int nbStartCards = 13;
	private int winningScore = 11;
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

	private int turn = 0;
	private int[] playerPosition;
	private static Card selected;
	private HashMap<Integer, HumanPlayer> humanPlayer ;
	private HashMap<Integer, INPC> NPCList;
	private int[] scores = new int[nbPlayers];
	private static Suit lead = null;
	private static Suit trumps = null;
	private static Card winningCard = null;

	Font bigFont = new Font("Serif", Font.BOLD, 36);

	/** cards that had been played */
	private  static ArrayList<Card> cardPlayed = new ArrayList<>();

	/** recently played cards */
	private static ArrayList<Card> cardOnTable = new ArrayList<>();

	/**getter*/
	public static Suit getLeadSuit() {
		return lead;
	}

	public static Suit getTrumps(){
		return trumps;
	}

	public static Card getWinningCard(){
		return winningCard;
	}

	public static ArrayList<Card> getCardPlayed(){
		return cardPlayed;
	}

	public static ArrayList<Card> getCardOnTable(){
		return cardOnTable;
	}

	/**original code*/
	public void setStatus(String string) {
		setStatusText(string);
	}

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

	public static void setCard(Card card) {
		selected = card;
	}

	public boolean rankGreater(Card card1, Card card2) {
		return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
	}

	/**shuffle cards on hand, and reduce them to nbStartCards*/
	private void shuffleHand(){

		ArrayList<Card> allCards = new ArrayList<>();
		for(Hand hand: hands){
			for(Card card:hand.getCardList()){
				allCards.add(card);
			}
		}

		for(int i = 0; i < nbPlayers; i++){
			hands[i] = new Hand(this.deck);
			for(int j = 0; j < nbStartCards; j++){
				hands[i].insert(randomCard(allCards),false);
			}
		}
	}

	private void initRound() throws IOException {
		//System.out.println("new round");
		NPCList = new HashMap<>();
		humanPlayer = new HashMap<>();
		hands = deck.dealingOut(nbPlayers, Rank.values().length,false); // Last element of hands is leftover cards; these are ignored
		/**need to shuffle cards and reduce to nStartCards*/
		shuffleHand();

		for (int i = 0; i < nbPlayers; i++) {
			hands[i].sort(Hand.SortType.SUITPRIORITY, true);
		}
		/**initialize all NPC*/
		NPCList = Player.NPCFactory.getInstance().getNPCList();
		/**initialize all human player*/
		if( NPCList.size() < nbPlayers){
			for(int i = 0; i < nbPlayers; i++ ){
				if(playerPosition[i] == HUMAN_PLAYER){
					humanPlayer.put(i,new HumanPlayer(hands[i], selected));
				}
			}
		}
		/**
		for(int i =0; i< NPCList.size();i++){
			System.out.println("npc "+i+" position"+NPCList.keySet().toArray()[i]);
		}
		for(int i = 0; i<humanPlayer.size(); i++){
			System.out.println("human player"+ i +" position "+ humanPlayer.keySet().toArray()[i]);
		}
		*/

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

	private Optional<Integer>  playRound(){
		// Returns winner, if any
		// Select and display trump suit
		trumps = randomEnum(Suit.class);
		final Actor trumpsActor = new Actor("sprites/" + trumpImage[trumps.ordinal()]);
		addActor(trumpsActor, trumpsActorLocation);
		// End trump suit
		Hand trick;
		int winner;
		int nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round

		for(int i = 0; i< nbStartCards; i++){
			trick = new Hand(deck);
			selected = null;
			if( playerPosition[nextPlayer] == HUMAN_PLAYER){
				humanPlayer.get(nextPlayer).setHand(true);
				setStatus("Player"+ nextPlayer + " double-click on card to lead.");
				while (null == selected)
					delay(100);
			}
			else{
				setStatusText("Player " + nextPlayer + " thinking...");
				delay(thinkingTime);
				selected = NPCList.get(nextPlayer).getPlayStrategy().selectCard(hands[nextPlayer].getCardList());
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
			for(int j = 1; j < nbPlayers; j++){
				if (++nextPlayer >= nbPlayers)
					nextPlayer = 0; // From last back to first
				selected = null;
				if(playerPosition[nextPlayer] == HUMAN_PLAYER){
					humanPlayer.get(nextPlayer).setHand(true);
					setStatus("Player"+ nextPlayer + " double-click on card to lead.");
					while (null == selected)
						delay(100);
				}
				else{
					setStatusText("Player " + nextPlayer + " thinking...");
					delay(thinkingTime);
					selected = NPCList.get(nextPlayer).getPlayStrategy().selectCard(hands[nextPlayer].getCardList());
				}
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
		//Property.readPropertyFile("whist/"+"whist.properties");
		//Property.readPropertyFile("whist/"+"original.properties");
		//Property.readPropertyFile("whist/"+"legal.properties");
		Property.readPropertyFile("whist/"+"smart.properties");

		playerPosition = new int[nbPlayers];

		random = new Random(Property.getProperty("Seed"));
		//System.out.println("system seed:"+ Property.getProperty(("Seed")));
		if(Property.ifPropertyExist("winningScore")){
			winningScore= Property.getProperty(("winningScore"));
		}
		if(Property.ifPropertyExist("thinkingTime")){
			thinkingTime = Property.getProperty(("thinkingTime"));
		}

		if(Property.ifPropertyExist("nbStartCards")){
			nbStartCards = Property.getProperty(("nbStartCards"));
			if(nbStartCards>Rank.values().length){
				fail("Error in Deck.dealing out.\n" + Rank.values().length*Suit.values().length + " cards in deck. Not enough for" + "\n" + nbPlayers + (nbPlayers > 1 ? " players with " : "player with ") + nbStartCards + (nbStartCards > 1 ? " cards per player." : "card per player.") + "\nApplication will terminate.");
			}
		}

		if(Property.ifPropertyExist("playerPosition")){
			playerPosition = Property.getPropertyArray("playerPosition");
		}

		do {
			initRound();
			winner = playRound();
			/** update random seed*/
			turn++;
			random= new Random(Property.getProperty("Seed")+(int)Math.pow(SEED_CHANGE,turn));
		} while (!winner.isPresent());

		addActor(new Actor("sprites/gameover.gif"), textLocation);
		setStatusText("Game over. Winner is player: " + winner.get());
		refresh();
	}

	public static void main(String[] args) throws IOException {
		new Whist();
	}

}
