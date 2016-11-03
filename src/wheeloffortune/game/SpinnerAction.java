package wheeloffortune.game;

public interface SpinnerAction {

	void performAction();
	
	boolean endsTurn();

	public static class Money implements SpinnerAction {
		private int amt;

		public Money(int amt) {
			this.amt = amt;
		}

		@Override
		public void performAction() {
			Game.getLogic().setMoneyOnSpinner(amt);
		}
		
		@Override
		public boolean endsTurn() {
			return false;
		}
		
		@Override
		public String toString() {
			return "$" + amt;
		}

	}
	
	public static class Bankrupt implements SpinnerAction {

		@Override
		public void performAction() {
			Game.getLogic().getCurrentPlayer().setMoney(0);
		}
		
		@Override
		public boolean endsTurn() {
			return true;
		}
		
		@Override
		public String toString() {
			return "BACKRUPT!!!";
		}
		
	}
	
	public static class MissTurn implements SpinnerAction {

		@Override
		public void performAction() {
			Game.getLogic().nextPlayer();
		}
		
		@Override
		public boolean endsTurn() {
			return true;
		}
		
		@Override
		public String toString() {
			return "Miss a turn";
		}
		
	}

}
