package uff.ic.geocataloguing.lib.observer;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Luiz Andr�
 * @version 1.0
 * @since 1.0
 * @alias SubjectImpl*/
public abstract class SubjectImpl implements Subject {

	/**
	 * @link aggregation
	 * @associates <{Observer}>
	 * @supplierCardinality 0..**/
	private ArrayList registry = new ArrayList();

	private Object state;

	/**
	 * Registra observador.
	 * 
	 * @version 1.0
	 * @since 1.0
	 * @param o
	 *            observador
	 */
	public void attach(Observer o) {
		registry.add(o);
		// notifyObservers();
	}

	/**
	 * Elimina observador da lista de observadores
	 * 
	 * @version 1.0
	 * @since 1.0
	 * @param o
	 *            observador
	 */
	public void deattach(Observer o) {
		registry.remove(o);
		// notifyObservers();
	}

	/**
	 * Notifica observadores sobre a mudan�a de estado.
	 * 
	 * @version 1.0
	 * @since 1.0
	 */
	public void notifyObservers() {
		Iterator iterator = registry.iterator();
		while (iterator.hasNext()) {
			((Observer) iterator.next()).update();
		}
	}

	/**
	 * Finaliza��o do objeto. Descarta as refer�ncias para os observadores e
	 * elimina objetos n�o utilizados.
	 * 
	 * @version 1.0
	 * @since 1.0
	 */
	public void finalize() throws Throwable {
		registry = null;
		System.gc();
		System.out.println("Coletando lixo .....");
		super.finalize();
	}

	/**
	 * @return Returns the state.
	 */
	public Object getState() {
		return state;
	}

	/**
	 * @param state
	 *            The state to set.
	 */
	public void setState(Object state) {
		this.state = state;
	}
}