/**
 * 
 */
package uff.ic.geocataloguing.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import uff.ic.geocataloguing.catfwk.view.CurrentWsp;

/**
 * @author Luiz Leme
 * @alias StopProcessAction*/
public class StopProcessAction extends AbstractAction {

	private static final long serialVersionUID = -3819607145416590937L;
	private CurrentWsp currentWsp;

	/**
	 * @param currentWsp
	 */
	public StopProcessAction(CurrentWsp currentWsp) {
		this.currentWsp = currentWsp;
	}

	public void actionPerformed(ActionEvent e) {
		currentWsp.stopProcess();
	}
}