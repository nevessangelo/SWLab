package uff.ic.geocataloguing.appmodel;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import javax.swing.JOptionPane;

import uff.ic.geocataloguing.catfwk.view.GeoCatalogAppWindow;
import uff.ic.geocataloguing.catfwk.view.WspWindow;

/**
 * @version 1.0
 * @since 1.0
 * @author Luiz Andr�
 * @alias GeoCatalogAppImpl*/
public class GeoCatalogAppImpl implements Application {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4673943822407622157L;

	/**
	 * @version 1.0
	 * @since 1.0
	 * @link aggregation
	 * @associates <{Document}>
	 * @supplierCardinality 0..**/
	private HashMap documents = null;

	/**
	 * @bidirectional
	 */
	public GeoCatalogAppWindow geoCatalogAppWindow = null;

	public GeoCatalogAppImpl() {
		documents = new HashMap();
	}

	public Document createDocument(String filename) {
		Document document = new WspImpl();
		document.setFilename(filename);
		return document;
	}

	public Document newDocument(String filename) throws FileNotFoundException {
		Document document = createDocument(filename);
		document.open();
		documents.put(filename, document);
		return document;
	}

	public Document loadDocument(String filename) throws IOException,
			ClassNotFoundException {
		FileInputStream in = new FileInputStream(filename);
		ObjectInputStream s = new ObjectInputStream(in);
		Document document = (Document) s.readObject();
		s.close();
		return document;
	}

	public Document openDocument(String filename) throws IOException,
			ClassNotFoundException {
		Document document = null;
		document = loadDocument(filename);
		document.setFilename(filename);
		document.open();
		documents.put(filename, document);
		return document;
	}

	public Document getDocument(String filename) {
		return (Document) documents.get(filename);
	}

	public void newDocument() {
		geoCatalogAppWindow.getFilenameDialog().setVisible(false);
		try {
			WspImpl wspImpl = (WspImpl) newDocument(geoCatalogAppWindow
					.getWorkspaceFilename().getText());
			WspWindow wspWindow = new WspWindow(geoCatalogAppWindow
					.getWorkspaceFilename().getText());
			wspWindow.setWspImpl(wspImpl);
			wspWindow.setVisible(true);
			geoCatalogAppWindow.getJDesktopPane().add(wspWindow);
			try {
				wspWindow.setSelected(true);
				wspWindow.setMaximum(true);
			} catch (PropertyVetoException e1) {
				JOptionPane.showMessageDialog(geoCatalogAppWindow, e1
						.getMessage());
			}
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(geoCatalogAppWindow, e1.getMessage());
		} finally { // TODO
		}
	}

	public void openDocument() {
		try {
			geoCatalogAppWindow.getFilenameDialog().setVisible(false);
			WspImpl wsp = (WspImpl) openDocument(geoCatalogAppWindow
					.getWorkspaceFilename().getText());
			WspWindow wspWin = new WspWindow(geoCatalogAppWindow.getWorkspaceFilename()
					.getText());
			wspWin.setWspImpl(wsp);
			wspWin.setVisible(true);
			geoCatalogAppWindow.getJDesktopPane().add(wspWin);
			try {
				wspWin.setSelected(true);
				wspWin.setMaximum(true);
			} catch (PropertyVetoException e1) {
				JOptionPane.showMessageDialog(geoCatalogAppWindow, e1
						.getMessage());
			} finally {
				// TODO
			}
		} catch (FileNotFoundException e2) {
			JOptionPane.showMessageDialog(geoCatalogAppWindow, e2.getMessage());
		} catch (IOException e2) {
			JOptionPane.showMessageDialog(geoCatalogAppWindow, e2.getMessage());
		} catch (ClassNotFoundException e2) {
			JOptionPane.showMessageDialog(geoCatalogAppWindow, e2.getMessage());
		}
	}

	public GeoCatalogAppWindow getGeoCatalogAppWindow() {
		return geoCatalogAppWindow;
	}

	public void setGeoCatalogAppWindow(GeoCatalogAppWindow geoCatalogAppWindow) {
		this.geoCatalogAppWindow = geoCatalogAppWindow;
	}
}
