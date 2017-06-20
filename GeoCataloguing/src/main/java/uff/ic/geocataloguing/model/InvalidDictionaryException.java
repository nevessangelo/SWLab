package uff.ic.geocataloguing.model;

import org.omg.CORBA.UserException;

public class InvalidDictionaryException extends UserException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6362587808468026162L;

	public InvalidDictionaryException(String reason) {
		super(reason);
	}

}
