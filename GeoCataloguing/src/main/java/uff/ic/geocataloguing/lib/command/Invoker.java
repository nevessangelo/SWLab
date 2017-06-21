package uff.ic.geocataloguing.lib.command;

import java.util.Vector;

/**
 * @alias Invoker*/
public class Invoker {
	/**
	 * @link aggregation
	 * @associates <{Command}>
	 * @directed*/
	private Vector lnkCommand = null;

	public Vector getLnkCommand() {
		return lnkCommand;
	}

	public void setLnkCommand(Vector lnkCommand) {
		this.lnkCommand = lnkCommand;
	}
}
