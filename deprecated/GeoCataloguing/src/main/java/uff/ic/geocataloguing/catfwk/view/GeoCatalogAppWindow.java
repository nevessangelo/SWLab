package uff.ic.geocataloguing.catfwk.view;

import java.awt.Component;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import uff.ic.geocataloguing.appmodel.GeoCatalogAppImpl;
import uff.ic.geocataloguing.controller.AddProcMenuItemAction;
import uff.ic.geocataloguing.controller.AddProcessAction;
import uff.ic.geocataloguing.controller.ExitApplicationAction;
import uff.ic.geocataloguing.controller.NewDocMenuItemAction;
import uff.ic.geocataloguing.controller.NewDocumentAction;
import uff.ic.geocataloguing.controller.OpenDocMenuItemAction;
import uff.ic.geocataloguing.controller.OpenDocumentAction;
import uff.ic.geocataloguing.controller.RemoveProcessAction;
import uff.ic.geocataloguing.controller.SaveDocumentAction;
import uff.ic.geocataloguing.controller.StartProcessAction;
import uff.ic.geocataloguing.controller.StopProcessAction;
import uff.ic.geocataloguing.controller.UpdProcMenuItemAction;
import uff.ic.geocataloguing.controller.UpdateProcessAction;

/**
 * @author Luiz Andr�
 * @version 1.0
 * @since 1.0
 * @alias GeoCatalogAppWindow*/
public class GeoCatalogAppWindow extends JFrame {

	private static final long serialVersionUID = -2033827828678144697L;

	private JMenuBar jJMenuBar = null;

	private JMenu fileMenu = null;

	private JMenu editMenu = null;

	private JMenu processMenu = null;

	private JMenu helpMenu = null;

	private JMenuItem exitMenuItem = null;

	private JMenuItem aboutMenuItem = null;

	private JMenuItem cutMenuItem = null;

	private JMenuItem copyMenuItem = null;

	private JMenuItem pasteMenuItem = null;

	private JMenuItem saveMenuItem = null;

	private JMenuItem newMenuItem = null;

	private JMenuItem openMenuItem = null;

	private JMenuItem addMenuItem = null;

	private JMenuItem updateMenuItem = null;

	private JMenuItem startMenuItem = null;

	private JMenuItem stopMenuItem = null;

	private JMenuItem removeMenuItem = null;

	private JDialog jFilenameDialog = null; // @jve:decl-index=0:visual-constraint="739,4"

	private JTextField workspaceFilename = null;

	private JDialog processDialog = null; // @jve:decl-index=0:visual-constraint="746,385"

	private JButton newOpenButton = null;

	private JTextField processName = null;

	private JTextField datasetSite = null;

	private JTextField thesaurus = null;

	private JTextField catalog = null;

	private JTextField fails = null;

	private JButton addUpdateButton = null;

	private JButton cancelButton = null;

	private JPanel jPanel = null;
	static GeoCatalogAppWindow catWindow = null; // @jve:decl-index=0:visual-constraint="742,84"

	private JPanel jContentPane = null;

	private JDesktopPane jDesktopPane = null;

	private JPanel jPanel1 = null;

	private JLabel jLabel = null;

	private JLabel jLabel1 = null;

	private JLabel jLabel2 = null;

	private JLabel jLabel3 = null;

	private JLabel jLabel4 = null;

	private JPanel jPanel2 = null;
	private AddProcessAction addProcessAction = null; // @jve:decl-index=0:visual-constraint=""

	private UpdateProcessAction updateProcessAction = null; // @jve:decl-index=0:visual-constraint=""

	private RemoveProcessAction removeProcessAction = null;
	private OpenDocumentAction openDocumentAction = null; // @jve:decl-index=0:visual-constraint="-26,85"

	private NewDocumentAction newDocumentAction = null; // @jve:decl-index=0:visual-constraint="-28,23"

	/**
	 * @associates <{br.pucrio.inf.catfwk.appmodel.GeoCatalogAppImpl}>
	 * @bidirectional <{br.pucrio.inf.catfwk.appmodel.GeoCatalogAppImpl#geoCatalogAppWindow}>*/
	private GeoCatalogAppImpl geoCatalogAppImpl = null;

	/**
	 * @associates <{br.pucrio.inf.catfwk.view.CurrentWsp}>
	 * @bidirectional <{br.pucrio.inf.catfwk.view.CurrentWsp#geoCatalogAppWindow}>*/
	private CurrentWsp currentWsp = null;

	public GeoCatalogAppWindow() throws HeadlessException {
		catWindow = this;
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				evt.getNewState();
				exitForm();
			}

			public void windowClosed(java.awt.event.WindowEvent evt) {
				evt.getNewState();
				System.gc();
			}
		});
		initialize();
	}

	public void exitForm() {
		Component[] comp = getJDesktopPane().getComponents();
		for (int i = 0; i < comp.length; i++) {
			try {
				(((WspWindow) comp[i])).saveWspWindow();
				(((WspWindow) comp[i])).closeWspWindow();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(catWindow, e1.getMessage());
			} finally {
				// TODO
			}
		}
		System.exit(0);
	}

	/**
	 * This method initializes newMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getNewMenuItem() {
		if (newMenuItem == null) {
			newMenuItem = new JMenuItem();
			newMenuItem.setText("New");
			newMenuItem.addActionListener(new NewDocMenuItemAction(this));
		}
		return newMenuItem;
	}

	/**
	 * This method initializes jDialog
	 * 
	 * @return javax.swing.JDialog
	 */
	public JDialog getFilenameDialog() {
		if (jFilenameDialog == null) {
			jFilenameDialog = new JDialog();
			jFilenameDialog.setTitle("Filename");
			jFilenameDialog.setModal(true);
			jFilenameDialog.setResizable(false);
			jFilenameDialog
					.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			jFilenameDialog.setLocation(new java.awt.Point(100, 100));
			jFilenameDialog.setSize(new java.awt.Dimension(390, 66));
			jFilenameDialog.setContentPane(getJPanel());
		}
		return jFilenameDialog;
	}

	/**
	 * This method initializes filename
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridy = 0;
			gridBagConstraints21.insets = new java.awt.Insets(0, 0, 0, 10);
			gridBagConstraints21.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.ipadx = 1;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.insets = new java.awt.Insets(10, 10, 10, 10);
			gridBagConstraints11.gridy = 0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getWorkspaceFilename(), gridBagConstraints11);
			jPanel.add(getNewOpenButton(), gridBagConstraints21);
		}
		return jPanel;
	}

	/**
	 * This method initializes OK
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getNewOpenButton() {
		if (newOpenButton == null) {
			newOpenButton = new JButton();
			newOpenButton.setAction(new NewDocumentAction(
					getGeoCatalogAppImpl()));
			newOpenButton.setText("New");
			newOpenButton.setActionCommand("New");
		}
		return newOpenButton;
	}

	/**
	 * This method initializes newDocument
	 * 
	 * @return br.pucrio.inf.catfwk.control.NewDocument
	 */
	public NewDocumentAction getNewDocumentAction() {
		if (newDocumentAction == null) {
			newDocumentAction = new NewDocumentAction(getGeoCatalogAppImpl());
			newDocumentAction.setCatApp(getGeoCatalogAppImpl());
		}
		return newDocumentAction;
	}

	/**
	 * This method initializes application
	 * 
	 * @return br.pucrio.inf.catfwk.view.Main
	 */
	GeoCatalogAppWindow getCatWindow() {
		if (catWindow == null) {
			catWindow = new GeoCatalogAppWindow();
			catWindow.setSize(new java.awt.Dimension(357, 281));
		}
		return catWindow;
	}

	/**
	 * This method initializes openDocument
	 * 
	 * @return br.pucrio.inf.catfwk.control.OpenDocument
	 */
	public OpenDocumentAction getOpenDocumentAction() {
		if (openDocumentAction == null) {
			openDocumentAction = new OpenDocumentAction(getGeoCatalogAppImpl());
			openDocumentAction.setCatApp(getGeoCatalogAppImpl());
		}
		return openDocumentAction;
	}

	/**
	 * This method initializes openMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getOpenMenuItem() {
		if (openMenuItem == null) {
			openMenuItem = new JMenuItem();
			openMenuItem.setText("Open");
			openMenuItem.addActionListener(new OpenDocMenuItemAction(this));
		}
		return openMenuItem;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.ipadx = 1;
			gridBagConstraints2.ipady = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJDesktopPane(), gridBagConstraints2);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jDesktopPane
	 * 
	 * @return javax.swing.JDesktopPane
	 */
	public JDesktopPane getJDesktopPane() {
		if (jDesktopPane == null) {
			jDesktopPane = new JDesktopPane();
			jDesktopPane.setCursor(new java.awt.Cursor(
					java.awt.Cursor.DEFAULT_CURSOR));
		}
		return jDesktopPane;
	}

	/**
	 * This method initializes processDialog
	 * 
	 * @return javax.swing.JDialog
	 */
	public JDialog getProcessDialog() {
		if (processDialog == null) {
			processDialog = new JDialog();
			processDialog.setTitle("Process configuration");
			processDialog.setResizable(false);
			processDialog.setModal(true);
			processDialog
					.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			processDialog.setLocation(new java.awt.Point(100, 100));
			processDialog.setSize(new java.awt.Dimension(500, 200));
			processDialog.setContentPane(getJPanel1());
		}
		return processDialog;
	}

	/**
	 * This method initializes jContentPane1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.ipadx = 300;
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.gridy = 5;
			gridBagConstraints12.insets = new java.awt.Insets(0, 0, 0, 10);
			gridBagConstraints12.weightx = 1.0;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.gridwidth = 2;
			gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints31.gridy = 6;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.ipadx = 300;
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.insets = new java.awt.Insets(10, 0, 0, 10);
			gridBagConstraints6.weightx = 1.0;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.ipadx = 300;
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.gridy = 4;
			gridBagConstraints10.insets = new java.awt.Insets(0, 0, 0, 10);
			gridBagConstraints10.weightx = 1.0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.ipadx = 300;
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 3;
			gridBagConstraints9.insets = new java.awt.Insets(0, 0, 0, 10);
			gridBagConstraints9.weightx = 1.0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.ipadx = 300;
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.insets = new java.awt.Insets(0, 0, 0, 10);
			gridBagConstraints8.weightx = 1.0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.ipadx = 300;
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.insets = new java.awt.Insets(0, 0, 0, 10);
			gridBagConstraints7.weightx = 1.0;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridwidth = 0;
			gridBagConstraints15.gridy = 5;
			gridBagConstraints15.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints15.insets = new java.awt.Insets(0, 10, 0, 0);
			gridBagConstraints15.gridx = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridwidth = 0;
			gridBagConstraints5.gridy = 4;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.insets = new java.awt.Insets(0, 10, 0, 0);
			gridBagConstraints5.gridx = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.insets = new java.awt.Insets(0, 10, 0, 0);
			gridBagConstraints4.gridy = 3;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.insets = new java.awt.Insets(0, 10, 0, 0);
			gridBagConstraints3.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 0);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.ipadx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
			gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints.gridx = 0;
			jLabel5 = new JLabel();
			jLabel5.setText("Feature Type");
			jLabel4 = new JLabel();
			jLabel4.setText("Fails catalog filename: ");
			jLabel3 = new JLabel();
			jLabel3.setText("Catalog filename:");
			jLabel2 = new JLabel();
			jLabel2.setText("ADL URL: ");
			jLabel1 = new JLabel();
			jLabel1.setText("Images directory: ");
			jLabel = new JLabel();
			jLabel.setText("Name: ");
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(jLabel, gridBagConstraints);
			jPanel1.add(getProcessName(), gridBagConstraints6);
			jPanel1.add(jLabel1, gridBagConstraints1);
			jPanel1.add(getRepository(), gridBagConstraints7);
			jPanel1.add(jLabel2, gridBagConstraints3);
			jPanel1.add(getGazetteer(), gridBagConstraints8);
			jPanel1.add(jLabel3, gridBagConstraints4);
			jPanel1.add(getCatalog(), gridBagConstraints9);
			jPanel1.add(jLabel4, gridBagConstraints5);
			jPanel1.add(getFails(), gridBagConstraints10);
			jPanel1.add(jLabel5, gridBagConstraints15);
			jPanel1.add(getJTextField(), gridBagConstraints12);
			jPanel1.add(getJPanel2(), gridBagConstraints31);
		}
		return jPanel1;
	}

	/**
	 * This method initializes directory
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getRepository() {
		if (datasetSite == null) {
			datasetSite = new JTextField();
		}
		return datasetSite;
	}

	/**
	 * This method initializes url
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getGazetteer() {
		if (thesaurus == null) {
			thesaurus = new JTextField();
		}
		return thesaurus;
	}

	/**
	 * This method initializes catalog
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getCatalog() {
		if (catalog == null) {
			catalog = new JTextField();
		}
		return catalog;
	}

	/**
	 * This method initializes fails
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getFails() {
		if (fails == null) {
			fails = new JTextField();
		}
		return fails;
	}

	/**
	 * This method initializes processName
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getProcessName() {
		if (processName == null) {
			processName = new JTextField();
		}
		return processName;
	}

	/**
	 * This method initializes ok
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getAddUpdateButton() {
		if (addUpdateButton == null) {
			addUpdateButton = new JButton();
			addUpdateButton.setAction(getAddProcessAction());
			addUpdateButton.setText("Add");
		}
		return addUpdateButton;
	}

	/**
	 * This method initializes cancel
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getProcessDialog().setVisible(false);
				}
			});
		}
		return cancelButton;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setBorder(null);
			jPanel2.add(getAddUpdateButton(), null);
			jPanel2.add(getCancelButton(), null);
		}
		return jPanel2;
	}

	/**
	 * This method initializes addMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAddMenuItem() {
		if (addMenuItem == null) {
			addMenuItem = new JMenuItem();
			addMenuItem.setText("Add process");
			addMenuItem.addActionListener(new AddProcMenuItemAction(this));
		}
		return addMenuItem;
	}

	/**
	 * This method initializes addProcessAction
	 * 
	 * @return br.pucrio.inf.catfwk.control.AddProcessAction
	 */
	public AddProcessAction getAddProcessAction() {
		if (addProcessAction == null) {
			addProcessAction = new AddProcessAction();
			addProcessAction.setCurrentWspImpl(getCurrentWsp());
		}
		return addProcessAction;
	}

	/**
	 * This method initializes updateProcessAction
	 * 
	 * @return br.pucrio.inf.catfwk.controller.UpdateProcessAction
	 */
	public UpdateProcessAction getUpdateProcessAction() {
		if (updateProcessAction == null) {
			updateProcessAction = new UpdateProcessAction();
			updateProcessAction.setCurrentWspImpl(getCurrentWsp());
		}
		return updateProcessAction;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getUpdateMenuItem() {
		if (updateMenuItem == null) {
			updateMenuItem = new JMenuItem();
			updateMenuItem.setText("Update process");
			updateMenuItem.addActionListener(new UpdProcMenuItemAction(this));
		}
		return updateMenuItem;
	}

	private StartProcessAction startProcessAction = null;

	public StartProcessAction getStartProcessAction() {
		if (startProcessAction == null) {
			startProcessAction = new StartProcessAction(getCurrentWsp());
		}
		return startProcessAction;
	}

	/**
	 * This method initializes startMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getStartMenuItem() {
		if (startMenuItem == null) {
			startMenuItem = new JMenuItem();
			startMenuItem.setText("Start process");
			startMenuItem.addActionListener(getStartProcessAction());
		}
		return startMenuItem;
	}

	/**
	 * This method initializes processMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getProcessMenu() {
		if (processMenu == null) {
			processMenu = new JMenu();
			processMenu.setText("Process");
			processMenu.add(getStartMenuItem());
			processMenu.add(getStopMenuItem());
		}
		return processMenu;
	}

	private StopProcessAction stopProcessAction = null;

	public StopProcessAction getStopProcessAction() {
		if (stopProcessAction == null) {
			stopProcessAction = new StopProcessAction(getCurrentWsp());
		}
		return stopProcessAction;
	}

	/**
	 * This method initializes stopMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getStopMenuItem() {
		if (stopMenuItem == null) {
			stopMenuItem = new JMenuItem();
			stopMenuItem.setText("Stop process");
			stopMenuItem.addActionListener(getStopProcessAction());
		}
		return stopMenuItem;
	}

	/**
	 * This method initializes removeMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getRemoveMenuItem() {
		if (removeMenuItem == null) {
			removeMenuItem = new JMenuItem();
			removeMenuItem.setAction(getRemoveProcessAction());
			removeMenuItem.setText("Remove process");
		}
		return removeMenuItem;
	}

	/**
	 * This method initializes removeProcessAction
	 * 
	 * @return br.pucrio.inf.catfwk.controller.RemoveProcessAction
	 */
	private RemoveProcessAction getRemoveProcessAction() {
		if (removeProcessAction == null) {
			removeProcessAction = new RemoveProcessAction();
			removeProcessAction.setCurrentWspImpl(getCurrentWsp());
		}
		return removeProcessAction;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
		}
		return jTextField;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			PrintStream ps = new PrintStream(
					new FileOutputStream("geocat.log"), true);
			System.setErr(ps);
			System.setOut(ps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catWindow = new GeoCatalogAppWindow();
		catWindow.setVisible(true);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setBounds(new java.awt.Rectangle(100, 100, 640, 480));
		this.setJMenuBar(getJJMenuBar());
		this.setTitle("GeoCatalog");
	}

	/**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getFileMenu());
			jJMenuBar.add(getEditMenu());
			jJMenuBar.add(getProcessMenu());
			jJMenuBar.add(getHelpMenu());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getNewMenuItem());
			fileMenu.add(getOpenMenuItem());
			fileMenu.add(getSaveMenuItem());
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new JMenu();
			editMenu.setText("Edit");
			editMenu.add(getAddMenuItem());
			editMenu.add(getUpdateMenuItem());
			editMenu.add(getRemoveMenuItem());
			editMenu.add(getCutMenuItem());
			editMenu.add(getCopyMenuItem());
			editMenu.add(getPasteMenuItem());
		}
		return editMenu;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new JMenuItem();
			exitMenuItem.setText("Exit");
			exitMenuItem.addActionListener(new ExitApplicationAction(this));
		}
		return exitMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About");
			aboutMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new JDialog(GeoCatalogAppWindow.this, "About", true)
							.setVisible(true);
				}
			});
		}
		return aboutMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCutMenuItem() {
		if (cutMenuItem == null) {
			cutMenuItem = new JMenuItem();
			cutMenuItem.setText("Cut");
			cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
					Event.CTRL_MASK, true));
		}
		return cutMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCopyMenuItem() {
		if (copyMenuItem == null) {
			copyMenuItem = new JMenuItem();
			copyMenuItem.setText("Copy");
			copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
					Event.CTRL_MASK, true));
		}
		return copyMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getPasteMenuItem() {
		if (pasteMenuItem == null) {
			pasteMenuItem = new JMenuItem();
			pasteMenuItem.setText("Paste");
			pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
					Event.CTRL_MASK, true));
		}
		return pasteMenuItem;
	}

	private SaveDocumentAction saveDocumentAction = null;

	private JLabel jLabel5 = null;

	private JTextField jTextField = null;

	public SaveDocumentAction getSaveDocumentAction() {
		if (saveDocumentAction == null) {
			saveDocumentAction = new SaveDocumentAction(this);
		}
		return saveDocumentAction;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSaveMenuItem() {
		if (saveMenuItem == null) {
			saveMenuItem = new JMenuItem();
			saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					Event.CTRL_MASK, true));
			saveMenuItem.setText("Save");
			saveMenuItem.addActionListener(getSaveDocumentAction());
		}
		return saveMenuItem;
	}

	/**
	 * @return Returns the currentWsp.
	 */
	public CurrentWsp getCurrentWsp() {
		if (currentWsp == null)
			currentWsp = new CurrentWsp(this);
		return currentWsp;
	}

	/**
	 * @return Returns the catApp.
	 */
	public GeoCatalogAppImpl getGeoCatalogAppImpl() {
		if (geoCatalogAppImpl == null)
			geoCatalogAppImpl = new GeoCatalogAppImpl();
		geoCatalogAppImpl.setGeoCatalogAppWindow(this);
		return geoCatalogAppImpl;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getWorkspaceFilename() {
		if (workspaceFilename == null) {
			workspaceFilename = new JTextField();
		}
		return workspaceFilename;
	}
} // @jve:decl-index=0:visual-constraint="81,4"
