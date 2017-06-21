package uff.ic.geocataloguing.lib.command;

/**
 * @author Luiz Leme
 * @alias Client*/
public class Client {
	/**
	 * @directed*/
	private Receiver lnkReceiver = null;

	/**
	 * @link dependency*/
	private ConcreteCommand lnkConcreteCommand;

	public ConcreteCommand getLnkConcreteCommand() {
		return lnkConcreteCommand;
	}

	public void setLnkConcreteCommand(ConcreteCommand lnkConcreteCommand) {
		this.lnkConcreteCommand = lnkConcreteCommand;
	}

	public Receiver getLnkReceiver() {
		return lnkReceiver;
	}

	public void setLnkReceiver(Receiver lnkReceiver) {
		this.lnkReceiver = lnkReceiver;
	}

}
