package uff.ic.geocataloguing.model;

import uff.ic.geocataloguing.lib.agent.Message;

/**
 * @author Luiz Andr�
 * @version 1.0
 * @since 1.0
 * @alias NewDataset*/
public class HarvestResource extends Message {

	private static final long serialVersionUID = 1134004306170375742L;

	public HarvestResource(Object body) {
		super(body);
	}
}
