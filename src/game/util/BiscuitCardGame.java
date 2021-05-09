package game.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import game.cards.Card;
import game.cards.CardSuit;
import game.players.Player;

import java.util.Random;
import java.util.Scanner;

public class BiscuitCardGame {
	private int SUITSIZE = 13;
	private final int NUMBEROFSUITS = 4;
	private int NUMBEROFCARDS = SUITSIZE * NUMBEROFSUITS;

	private ArrayList<Card> clubCards = new ArrayList<>(SUITSIZE);
	private ArrayList<Card> diamondCards = new ArrayList<>(SUITSIZE);
	private ArrayList<Card> heartCards = new ArrayList<>(SUITSIZE);
	private ArrayList<Card> spadeCards = new ArrayList<>(SUITSIZE);
	private ArrayList<Card> cards = new ArrayList<>(NUMBEROFCARDS);
	private ArrayList<Card> shuffledCards = new ArrayList<>(NUMBEROFCARDS);
	private HashMap<Integer, Card> openedCards = new HashMap<>();
	private HashMap<Integer, Card> removedCards = new HashMap<>();

	private ArrayList<Player> players = new ArrayList<>();
	private Player currentPlayer;

	private boolean isGameOver;
	private int remainingCards;

	private Scanner s = new Scanner(System.in);

	private void addPlayers() {
		System.out.print("Enter number of players(2-4): ");
		boolean inValid = true;
		int numOfPlayers = 0;
		while (inValid) {
			numOfPlayers = s.nextInt();
			if (numOfPlayers >= 2 && numOfPlayers <= 4) {
				inValid = false;
			} else {
				System.out.print("Invalid Input!! Enter number of players(2-4): ");
			}
		}
		s.nextLine();
		for (int i = 0; i <= numOfPlayers - 1; i++) {
			System.out.print("Enter " + (i + 1) + " player name: ");
			String name = s.nextLine();
			players.add(new Player(name, false, 0));
		}
	}

	private void initializeCards() {
		boolean inValid = true;
		System.out.print("Enter Suit Size (4-13): ");
		while (inValid) {
			SUITSIZE = s.nextInt();
			if (SUITSIZE >= 4 && SUITSIZE <= 13) {
				NUMBEROFCARDS = SUITSIZE * NUMBEROFSUITS;
				inValid = false;
			} else {
				System.out.print("Enter Valid Suit Size (4-13): ");
			}
		}
		isGameOver = false;
		remainingCards = NUMBEROFCARDS;
		clubCards.clear();
		diamondCards.clear();
		heartCards.clear();
		spadeCards.clear();
		cards.clear();
		shuffledCards.clear();

		for (int i = 1; i <= SUITSIZE; i++) {
			clubCards.add(new Card(CardSuit.CLUB, i));
			diamondCards.add(new Card(CardSuit.DIAMOND, i));
			heartCards.add(new Card(CardSuit.HEART, i));
			spadeCards.add(new Card(CardSuit.SPADE, i));
			cards.add(clubCards.get(i - 1));
			cards.add(diamondCards.get(i - 1));
			cards.add(heartCards.get(i - 1));
			cards.add(spadeCards.get(i - 1));
			for (int j = 1; j <= NUMBEROFSUITS; j++)
				shuffledCards.add((i * j) - 1, null);
		}
		System.out.println("Shuffling Cards....\n");
		callSleep(1000);
		shuffleCards();
		System.out.println("Cards Shuffled!! GET READY");
		System.out.println("Printing Cards.....\n");
		callSleep(1000);
		printCards();

	}

	private void callSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void shuffleCards() {
		Random rand = new Random();
		boolean isNull = true;
		int shuffledIndex;
		for (int cardsIndex = 0; cardsIndex < NUMBEROFCARDS; cardsIndex++) {
			isNull = true;
			while (isNull) {
				shuffledIndex = rand.nextInt(NUMBEROFCARDS);
				if (shuffledIndex < NUMBEROFCARDS && shuffledCards.get(shuffledIndex) == null) {
					shuffledCards.set(shuffledIndex, cards.get(cardsIndex));
					isNull = false;
				}
			}
		}
	}

	private void play() {
		currentPlayer = players.get(0);
		currentPlayer.setHasTurn(true);
		while (!isGameOver) {
			takePlayerMove();
		}
	}

	private void takePlayerMove() {
		System.out.print(currentPlayer.getPlayerName() + "'s move!" + " Enter your choice: ");
		int num = -1;
		boolean inValid = true;
		while (inValid) {
			if (s.hasNextInt()) {
				num = s.nextInt();
				if (num >= 1 && num <= NUMBEROFCARDS) {
					if (!openedCards.containsKey(num - 1) && !removedCards.containsKey(num - 1)) {
						inValid = false;
						checkMatch(num - 1);
					} else {
						System.out.println("Enter another number, this card is already opened!!");
						System.out.println("Enter your choice again: ");
					}
				} else if (num == 999) {
					inValid = false;
					exit();
				} else {
					System.out.print("Enter number lesser than " + NUMBEROFCARDS + "!!: ");
				}
			}
		}
	}

	private void checkMatch(int index) {
		Card currentCard = shuffledCards.get(index);
		System.out.println("Opened Card: " + currentCard);
		if (openedCards.size() > 1)
			System.out.println("Founding Match...");
		callSleep(500);
		if (checkOpenedCards(currentCard)) {
			System.out.println("--Match Found!--");
			for (Entry<Integer, Card> entry : openedCards.entrySet()) {
				if (entry.getValue().getNumber().equals(currentCard.getNumber())) {
					openedCards.remove(entry.getKey());
					removedCards.put(entry.getKey(), entry.getValue());
					removedCards.put(index, currentCard);
					remainingCards -= 2;
					currentPlayer.setScore(currentPlayer.getScore() + 1);
					break;
				}
			}
		} else {
			openedCards.put(index, currentCard);
			if (openedCards.size() > 1)
				System.out.print("--Match Not Found!!");
			System.out.println("--Card added to open cards--");
			changeCurrentPlayer();
		}
		if (remainingCards == 0) {
			gameOver();
		} else {
			printCards();
			takePlayerMove();
		}
	}

	private boolean checkOpenedCards(Card currentCard) {
		for (Card openedCard : openedCards.values()) {
			if (openedCard.getNumber().equals(currentCard.getNumber()))
				return true;
		}
		return false;
	}

	private void changeCurrentPlayer() {
		int index = players.indexOf(currentPlayer);
		if (index == players.size() - 1)
			index = 0;
		else
			index++;
		currentPlayer = players.get(index);
	}

	public void printCards() {
		System.out.print("\t\tColumn:\t ");
		for (int col = 1; col <= SUITSIZE; col++)
			if (col >= 10)
				System.out.print(col + "\t");
			else
				System.out.print(col + "\t");
		System.out.println("\n");
		int index = 0;
		for (int row = 1; row <= NUMBEROFSUITS; row++) {
			System.out.print("\t\tRow " + row + ":\t ");
			for (int col = 1; col <= SUITSIZE; col++) {
				Card card = shuffledCards.get(index++);
				if (openedCards.containsValue(card)) {
					System.out.print(card + "\t");
				} else if (removedCards.containsValue(card)) {
					System.out.print("O:O \t");
				} else {
					System.out.print("X:X \t");
				}
			}
			System.out.println("\n");
		}
		printScores(false);
	}

	private void printScores(boolean gameOver) {
		if (gameOver) {
			Collections.sort(players);
			System.out.println(" Rank\t Name \t Score");
			for (int i = 0; i < players.size(); i++) {
				System.out.println((i + 1) + ".\t" + players.get(i).getPlayerName() + "\t" + players.get(i).getScore());
			}
		} else {
			System.out.print("Scores: ");
			for (int i = 0; i < players.size(); i++) {
				System.out.print(players.get(i).getPlayerName() + ":" + players.get(i).getScore() + "  ");
			}
			System.out.println();
		}
		for (int i = 0; i < SUITSIZE; i++) {
			System.out.print("=========");
		}
		System.out.println("\n");
	}

	private void gameOver() {
		isGameOver = true;
		System.out.println("Game Over!!");
		printScores(true);
	}

	private void exit() {
		isGameOver = true;
		System.out.println("Exiting!!");
		printScores(false);
	}

	public void startGame() {
		System.out.println("\n\n\t-----Welcome to BISCUIT CARD GAME-----\n\n");
		addPlayers();
		initializeCards();
		play();
	}
}
