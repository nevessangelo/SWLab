package uff.ic.geocataloguing.lib.agent;

import uff.ic.geocataloguing.model.CataloguingProcess;

/**
 * @version 1.0
 * @since 1.0
 * @author Luiz Andr�
 * @alias Agent
 * @stereotype abstract*/
public abstract class Agent implements Runnable {

	public Agent(String id) {
		commHandler = new LocalComm();
		this.id = id;
	}

	public final void sendMessage(Message msg) {
		commHandler.sendMessage(msg);
	}

	public final Message readMessage(Message type) throws InterruptedException {
		return commHandler.readMessage(type);
	}

	/**
	 * Inicia o processamento do agente.
	 * 
	 * @version 1.0
	 * @since 1.0
	 */
	public final void start() {
		System.out.println("Agent id:" + getId() + " iniciando opera��o");
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Interrompe o processamento do agente.
	 * 
	 * @version 1.0
	 * @since 1.0
	 */
	public final void stop() {
		thread = null;
		System.gc();
	}

	/**
	 * @version 1.0
	 * @since 1.0
	 * @return A thread que executa o processamento do agente
	 */
	public final Thread getThread() {
		return thread;
	}

	/**
	 * @version 1.0
	 * @since 1.0
	 * @return identifica��o do agente.
	 */
	public final String getId() {
		return this.id;
	}

	/**
	 * @return Returns the process.
	 */
	public CataloguingProcess getProcess() {
		return process;
	}

	/**
	 * @param process
	 *            The cataloguingProcess to set.
	 * @since 1.0
	 */
	public void setProcess(CataloguingProcess process) {
		this.process = process;
	}

	/**
	 * In�cio do processamento da Thread do Agente. Dispara agentAction() que
	 * cont�m o processamento do agente.
	 * 
	 * @version 1.0
	 * @since 1.0
	 * @see AgentImpl.agentAction()
	 */
	public void run() {
		Thread myThread = Thread.currentThread();
		while (thread == myThread) {
			agentBehavior();
		}
	}

	public abstract void agentBehavior();

	/**
	 * @labelDirection forward
	 * @supplierRole +commHandler*/
	private Comm commHandler = null;

	private String id;

	private Thread thread = null;

	/**
	 * @bidirectional
	 */
	private CataloguingProcess process = null;
}