package uff.ic.geocataloguing.catfwk.view;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import uff.ic.geocataloguing.appmodel.WspImpl;
import uff.ic.geocataloguing.model.CataloguingProcess;
import uff.ic.geocataloguing.lib.observer.Observer;

/**
 * @author Luiz Andr�
 * @version 1.0
 * @since 1.0
 * @alias WspWindow*/
public class WspWindow extends JInternalFrame implements Observer,
		InternalFrameListener, TreeSelectionListener {

	private static final long serialVersionUID = 3464602272968429219L;

	private JPanel jContentPane = null;

	private JSplitPane jSplitPane = null;

	private JTree processTree = null;

	private DefaultMutableTreeNode treeNodes = null;

	private JScrollPane jScrollPane = null;
	private WspImpl wspImpl = null;

	/**
	 * @supplierRole +current*/
	private CataloguingProcess currentProcess = null;

	private JScrollPane jScrollPane1 = null;

	private JList fails = null;

	private DefaultListModel failsList = null;

	public WspWindow(String title) {
		super(title);
		this.addInternalFrameListener(this);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setResizable(true);
		this
				.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setBounds(new java.awt.Rectangle(0, 0, 630, 430));
		this.setMaximizable(true);
		this.setIconifiable(true);
		this.setClosable(true);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerSize(5);
			jSplitPane.setDividerLocation(200);
			jSplitPane.setLeftComponent(getJScrollPane1());
			jSplitPane.setRightComponent(getJScrollPane());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes processes
	 * 
	 * @return javax.swing.JTree
	 */
	private JTree getProcessTree() {
		if (processTree == null) {
			processTree = new JTree(getTreeNodes());
			processTree.setShowsRootHandles(true);
			processTree.addTreeSelectionListener(this);
			processTree.getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);
		}
		return processTree;
	}

	public String getFilename() {
		return getWspImpl().getFilename();
	}

	public void setFilename(String filename) {
		getWspImpl().setFilename(filename);
	}

	public void closeWspWindow() throws IOException {
		getWspImpl().close();
	}

	public void saveWspWindow() throws IOException {
		getWspImpl().save();
	}

	public void internalFrameActivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		// TODO Auto-generated method stub
	}

	public void internalFrameClosing(InternalFrameEvent e) {
		try {
			CataloguingProcess[] proc = getWspImpl().getProcessList();
			for (int i = 0; i < proc.length; i++) {
				proc[i].stop();
			}
			saveWspWindow();
			closeWspWindow();
		} catch (IOException e1) { // TODO
		}
	}

	public void internalFrameDeactivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub

	}

	public void internalFrameDeiconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub

	}

	public void internalFrameIconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub

	}

	public void internalFrameOpened(InternalFrameEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * This method initializes treeRoot
	 * 
	 * @return javax.swing.tree.DefaultMutableTreeNode
	 */
	private DefaultMutableTreeNode getTreeNodes() {
		DefaultMutableTreeNode node = null;
		if (treeNodes == null) {
			treeNodes = new DefaultMutableTreeNode("Cataloguing process");
			CataloguingProcess[] proc = getWspImpl().getProcessList();
			for (int i = 0; i < proc.length; i++) {
				node = new DefaultMutableTreeNode(proc[i]);
				treeNodes.add(node);
			}
		}
		return treeNodes;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getFails());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes workspaceImpl
	 * 
	 * @return inf.puc_rio.catframework.WorkspaceImpl
	 */
	public WspImpl getWspImpl() {
		if (wspImpl == null) {
			wspImpl = new WspImpl();
		}
		return wspImpl;
	}

	/**
	 * @param wspImpl
	 *            The wspImpl to set.
	 */
	public void setWspImpl(WspImpl workspaceImpl) {
		if (workspaceImpl != null)
			workspaceImpl.deattach(this);
		this.wspImpl = workspaceImpl;
		this.wspImpl.attach(this);
		this.update();
	}

	public CataloguingProcess[] getProcessList() {
		return getWspImpl().getProcessList();
	}

	public void update() {
		updateProcessTree();
		updateFailsList();
	}

	public void updateProcessTree() {
		processTree = null;
		treeNodes = null;
		getJScrollPane1().setViewportView(getProcessTree());
	}

	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getProcessTree()
				.getLastSelectedPathComponent();
		if (node == null)
			return;
		Object nodeInfo = node.getUserObject();
		if (node.isLeaf() && (nodeInfo instanceof CataloguingProcess)) {
			if (currentProcess != null) {
				currentProcess.deattach(this);
			}
			currentProcess = (CataloguingProcess) nodeInfo;
			currentProcess.attach(this);
			updateFailsList();
		}
	}

	private void updateFailsList() {
		failsList = null;
		fails = null;
		jScrollPane.setViewportView(getFails());
	}

	/**
	 * @return Returns the currentProcess.
	 */
	public CataloguingProcess getCurrentProcess() {
		return this.currentProcess;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getProcessTree());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes fails
	 * 
	 * @return javax.swing.JList
	 */
	private JList getFails() {
		if (fails == null) {
			fails = new JList();
			fails.setModel(getFailsList());
		}
		return fails;
	}

	/**
	 * This method initializes failsList
	 * 
	 * @return javax.swing.DefaultListModel
	 */
	private DefaultListModel getFailsList() {
		if (failsList == null) {
			failsList = new DefaultListModel();
			if (currentProcess != null) {
				Collection fails;
				try {
					fails = currentProcess.listFails();
					Iterator iter = fails.iterator();
					while (iter.hasNext()) {
						failsList.addElement(iter.next());
					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, e.getMessage());
				} catch (ClassNotFoundException e) {
					JOptionPane.showMessageDialog(this, e.getMessage());
				}
			}
		}
		return failsList;
	}
}
