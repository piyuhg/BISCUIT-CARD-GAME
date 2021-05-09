package game.cards;

public class Card {

	private CardSuit type;
	private String number;

	public Card(CardSuit type, int number) {
		this.type = type;
		this.number = this.getCardNumber(number);
	}

	public CardSuit getType() {
		return type;
	}

	public void setType(CardSuit type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	private String getCardNumber(int number) {
		if (number > 1 && number < 11)
			return String.valueOf(number);
		else {
			switch (number) {
			case 1:
				return "A";
			case 11:
				return "J";
			case 12:
				return "Q";
			case 13:
				return "K";
			default:
				return "Invalid";
			}
		}
	}

	@Override
	public String toString() {
		return this.type + ":" + this.number;
	}
}