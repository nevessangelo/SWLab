package uff.ic.geocataloguing;

import org.xml.sax.Attributes;

/**
 * @author Luiz Leme
 * @alias MetadataElement*/
public class MetadataElement {
	private String qName = null;

	private String localName = null;

	private String value = null;

	private String uri = null;

	private Attributes attributes = null;

	public MetadataElement(String uri, String localName, String qName,
			Attributes attributes) {
		this.uri = uri;
		this.localName = localName;
		this.qName = qName;
		this.attributes = attributes;
	}

	public String getQname() {
		return qName;
	}

	/**
	 * @return Returns the localName.
	 */
	public String getLocalName() {
		return localName;
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return Returns the uri.
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @return Returns the attributes.
	 */
	public Attributes getAttributes() {
		return attributes;
	}

	/**
	 * @param value
	 *            The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
