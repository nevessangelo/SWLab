package iso.iso19115.reference_system;

import iso.iso19115.citation.CI_Citation;

/**
 * @stereotype type
 * @alias MD_Identifier*/
public class MD_Identifier {

	/**
	 * @bidirectional {iso.iso19115.citation.CI_Citation>
	 */
	public CI_Citation authority = null;

	public String code = null;
}
