package game.players;

public class Player implements Comparable<Player> {

	private String playerName;
	private boolean hasTurn;
	private int score;

	public Player(String playerName, boolean hasTurn, int score) {
		this.playerName = playerName;
		this.hasTurn = hasTurn;
		this.score = score;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public boolean isHasTurn() {
		return hasTurn;
	}

	public void setHasTurn(boolean hasTurn) {
		this.hasTurn = hasTurn;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return playerName + "'s score=" + score;
	}

	@Override
	public int compareTo(Player player) {
		if (this.score > player.score)
			return 1;
		return 0;
	}

}