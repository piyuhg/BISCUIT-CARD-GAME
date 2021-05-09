package game.cards;

public enum CardSuit {

	CLUB("C"), DIAMOND("D"), HEART("H"), SPADE("S");

	private String type;

	private CardSuit(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}
}