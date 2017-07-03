/**
 * 
 */
package uff.ic.geocataloguing.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import uff.ic.geocataloguing.catfwk.view.GeoCatalogAppWindow;

/**
 * @author Luiz Leme*/
public class ExitApplicationAction implements ActionListener {
	private GeoCatalogAppWindow catAppWindow;

	/**
	 * @param window
	 */
	public ExitApplicationAction(GeoCatalogAppWindow window) {
		catAppWindow = window;
	}

	public void actionPerformed(ActionEvent e) {
		catAppWindow.exitForm();
	}
}