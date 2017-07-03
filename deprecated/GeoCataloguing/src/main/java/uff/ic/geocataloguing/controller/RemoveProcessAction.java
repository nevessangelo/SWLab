package uff.ic.geocataloguing.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import uff.ic.geocataloguing.catfwk.view.CurrentWsp;

/**
 * @author Luiz Leme
 * @alias RemoveProcessAction*/
public class RemoveProcessAction extends AbstractAction {

	private static final long serialVersionUID = 5245227434505555313L;
	private CurrentWsp currentWspImpl = null;

	public void actionPerformed(ActionEvent e) {
		currentWspImpl.removeCurrentProcess();
	}

	/**
	 * @return Returns the currentWspImpl.
	 */
	public CurrentWsp getCurrentWspImpl() {
		return currentWspImpl;
	}

	/**
	 * @param currentWspImpl
	 *            The currentWspImpl to set.
	 */
	public void setCurrentWspImpl(CurrentWsp currentWspImpl) {
		this.currentWspImpl = currentWspImpl;
	}

}
