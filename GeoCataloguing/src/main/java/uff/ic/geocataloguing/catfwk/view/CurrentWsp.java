package uff.ic.geocataloguing.catfwk.view;

import javax.swing.JOptionPane;

import uff.ic.geocataloguing.appmodel.WspImpl;
import uff.ic.geocataloguing.model.CataloguingProcess;
import uff.ic.geocataloguing.model.InvalidDictionaryException;
import uff.ic.geocataloguing.model.InvalidRepositoryException;
import uff.ic.geocataloguing.CustomFactory;

/**
 * @author Luiz Leme
 * @alias CurrentWsp*/
public class CurrentWsp {

	private static final long serialVersionUID = 7907387128463230861L;

	/**
	 * @bidirectional
	 */
	private GeoCatalogAppWindow geoCatalogAppWindow = null;

	public CurrentWsp(GeoCatalogAppWindow catAppWindow) {
		this.geoCatalogAppWindow = catAppWindow;
	}

	public boolean addProcess() {
		geoCatalogAppWindow.getProcessDialog().setVisible(false);

		String[] datasetSites = new String[1];
		datasetSites[0] = geoCatalogAppWindow.getRepository().getText();
		String[][] thesaurus = new String[1][2];
		thesaurus[0][0] = CustomFactory.ADL_DICTIONARY;
		thesaurus[0][1] = geoCatalogAppWindow.getGazetteer().getText();

		CataloguingProcess process = new CataloguingProcess(geoCatalogAppWindow
				.getProcessName().getText(), datasetSites, thesaurus,
				geoCatalogAppWindow.getCatalog().getText(), geoCatalogAppWindow
						.getFails().getText());
		WspImpl wspImpl = ((WspWindow) geoCatalogAppWindow.getJDesktopPane()
				.getSelectedFrame()).getWspImpl();
		return wspImpl.addProcess(process);
	}

	public void updateProcess() {
		geoCatalogAppWindow.getProcessDialog().setVisible(false);

		String[] datasetSites = new String[1];
		datasetSites[0] = geoCatalogAppWindow.getRepository().getText();
		String[][] thesaurus = new String[1][2];
		thesaurus[0][0] = CustomFactory.ADL_DICTIONARY;
		thesaurus[0][1] = geoCatalogAppWindow.getGazetteer().getText();

		WspWindow wspWindow = ((WspWindow) geoCatalogAppWindow
				.getJDesktopPane().getSelectedFrame());
		CataloguingProcess process = wspWindow.getCurrentProcess();

		if (process != null) {
			process.setName(geoCatalogAppWindow.getProcessName().getText());
			process.setRepositories(datasetSites);
			process.setGazetteers(thesaurus);
			process.setCatalog(geoCatalogAppWindow.getCatalog().getText());
			process.setFails(geoCatalogAppWindow.getFails().getText());
		}
	}

	public boolean removeCurrentProcess() {
		WspWindow wspWindow = ((WspWindow) geoCatalogAppWindow
				.getJDesktopPane().getSelectedFrame());
		return wspWindow.getWspImpl().removeProcess(
				wspWindow.getCurrentProcess());
	}

	public void startProcess() {
		try {
			((WspWindow) geoCatalogAppWindow.getJDesktopPane()
					.getSelectedFrame()).getCurrentProcess().start();
		} catch (InvalidDictionaryException e1) {
			JOptionPane.showMessageDialog(geoCatalogAppWindow, e1.getMessage());
		} catch (InvalidRepositoryException e1) {
			JOptionPane.showMessageDialog(geoCatalogAppWindow, e1.getMessage());
		}
	}

	public void stopProcess() {
		try {
			((WspWindow) geoCatalogAppWindow.getJDesktopPane()
					.getSelectedFrame()).getCurrentProcess().stop();
		} finally { // TODO

		}
	}
}
