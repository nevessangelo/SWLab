package uff.ic.geocataloguing.appmodel;

import uff.ic.geocataloguing.model.CataloguingProcess;

/**
 * @alias Workspace*/
public interface Workspace {

	public abstract boolean addProcess(CataloguingProcess process);

	public abstract CataloguingProcess[] getProcessList();

	public abstract boolean removeProcess(CataloguingProcess process);

}