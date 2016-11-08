package wheeloffortune.game;

public class Player {

	private int index;
	private int money = 0;

	public Player(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
	
}
