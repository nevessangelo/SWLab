package uff.ic.geocataloguing.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import uff.ic.geocataloguing.appmodel.GeoCatalogAppImpl;

/**
 * @author Luiz Andr�
 * @version 1.0
 * @since 1.0
 * @alias OpenDocumentAction*/
public class OpenDocumentAction extends AbstractAction {

	private static final long serialVersionUID = 5245227434505555313L;
	private GeoCatalogAppImpl catAppImpl = null;

	public void actionPerformed(ActionEvent e) {
		catAppImpl.openDocument();
	}

	public OpenDocumentAction(GeoCatalogAppImpl catAppImpl) {
		this.catAppImpl = catAppImpl;
	}

	/**
	 * @return Returns the catApp.
	 */
	public GeoCatalogAppImpl getCatApp() {
		return catAppImpl;
	}

	/**
	 * @param catAppImpl
	 *            The catApp to set.
	 */
	public void setCatApp(GeoCatalogAppImpl catAppImpl) {
		this.catAppImpl = catAppImpl;
	}

}
