package uff.ic.geocataloguing.lib.observer;

/**
 * @author Luiz Leme
 * @alias ObserverImpl*/
public class ObserverImpl implements Observer {

	public void update() {
		// TODO Auto-generated method stub

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

	private SubjectImpl subjectImpl;

	private Object state;

	public SubjectImpl getSubjectImpl() {
		return subjectImpl;
	}

	public void setSubjectImpl(SubjectImpl subjectImpl) {
		this.subjectImpl = subjectImpl;
	}
}
