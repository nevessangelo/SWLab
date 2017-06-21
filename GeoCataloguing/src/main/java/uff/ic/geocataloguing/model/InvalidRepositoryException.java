package uff.ic.geocataloguing.model;

import org.omg.CORBA.UserException;

public class InvalidRepositoryException extends UserException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3465909222150958356L;

	public InvalidRepositoryException(String reason) {
		super(reason);
	}

}
