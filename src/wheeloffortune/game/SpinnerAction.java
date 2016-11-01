package wheeloffortune.game;

public interface SpinnerAction {

	/**
	 * Returns whether the player should continue their turn
	 */
	boolean performAction();

	public static class Money implements SpinnerAction {
		private int amt;

		public Money(int amt) {
			this.amt = amt;
		}

		@Override
		public boolean performAction() {
			Game.getLogic().setMoneyOnSpinner(amt);
			return true;
		}

	}
	
	public static class Bankrupt implements SpinnerAction {

		@Override
		public boolean performAction() {
			Game.getLogic().getCurrentPlayer().setMoney(0);
			return false;
		}
		
	}
	
	public static class MissTurn implements SpinnerAction {

		@Override
		public boolean performAction() {
			Game.getLogic().nextPlayer();
			return false;
		}
		
	}

}
