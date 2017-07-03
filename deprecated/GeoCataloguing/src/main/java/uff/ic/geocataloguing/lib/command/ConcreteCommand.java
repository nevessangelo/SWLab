package uff.ic.geocataloguing.lib.command;

/**
 * @author Luiz Leme
 * @alias ConcreteCommand*/
public class ConcreteCommand extends Command {
	public void execute() { // TODO
	}

	/**
	 * @return Returns the state.
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state
	 *            The state to set.
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @directed*/
	private Receiver lnkReceiver = null;

	private int state;

	public Receiver getLnkReceiver() {
		return lnkReceiver;
	}

	public void setLnkReceiver(Receiver lnkReceiver) {
		this.lnkReceiver = lnkReceiver;
	}
}
