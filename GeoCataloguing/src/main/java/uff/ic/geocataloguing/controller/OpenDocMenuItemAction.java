/**
 * 
 */
package uff.ic.geocataloguing.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import uff.ic.geocataloguing.catfwk.view.GeoCatalogAppWindow;

/**
 * @author Luiz Leme*/
public class OpenDocMenuItemAction implements ActionListener {
	private GeoCatalogAppWindow catAppWindow;

	/**
	 * @param window
	 */
	public OpenDocMenuItemAction(GeoCatalogAppWindow window) {
		catAppWindow = window;
	}

	public void actionPerformed(ActionEvent e) {
		catAppWindow.getNewOpenButton().setAction(
				catAppWindow.getOpenDocumentAction());
		catAppWindow.getNewOpenButton().setActionCommand("Open");
		catAppWindow.getNewOpenButton().setText("Open");
		catAppWindow.getFilenameDialog().setVisible(true);
	}
}