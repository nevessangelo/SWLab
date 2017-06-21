/* Generated by Together */

package uff.ic.geocataloguing.model;

import java.io.IOException;
import java.util.Collection;

/**
 * @since 1.0
 * @version 1.0
 * @author Luiz Andr�
 * @alias Catalog
 * @stereotype abstract*/
public abstract class Catalog extends DescriptionSource {

	/**
	 * @directed
	 * @label organize desc using
	 * @labelDirection forward*/
	private transient CatalogDescBuilder builder = null;

	public Catalog(String url) {
		super(url);
	}

	public abstract DatasetDescription put(DatasetDescription dataset)
			throws IOException, ClassNotFoundException;

	public abstract DatasetDescription remove(DatasetDescription dataset)
			throws IOException, ClassNotFoundException;

	public abstract DatasetDescription get(Object key) throws IOException,
			ClassNotFoundException;

	public abstract boolean contains(String filename) throws IOException,
			ClassNotFoundException;

	public abstract Collection values() throws IOException,
			ClassNotFoundException;

	public abstract void exportXML() throws IOException, ClassNotFoundException;

	public abstract int size();

	public abstract void connect();

	public abstract void disconnect();

	public abstract DatasetDescription getDesciption(
			DatasetDescription datsetDescription);

	/**
	 * @link aggregationByValue
	 */
	/* #Resource lnkResource; */
}
