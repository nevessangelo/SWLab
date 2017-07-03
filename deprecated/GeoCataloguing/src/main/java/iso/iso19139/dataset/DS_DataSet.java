package iso.iso19139.dataset;

import iso.iso19115.metadata.MD_Metadata;
import iso.iso19115.spatial_representation.MD_Georectified;

import java.io.Serializable;

/**
 * @author Luiz André
 * @version 1.0
 * @since 1.0
 * @alias DS_DataSet*/
public class DS_DataSet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8209286649894277753L;

	/**
	 * @associates <{iso.iso19115.metadata.MD_Metadata}>
	 * @supplierCardinality 1..*
	 * @supplierRole +has
	 * @clientRole +describes
	 * @clientCardinality 0..*
	 * @bidirectional <{iso.iso19115.metadata.MD_Metadata#describes}>
	 * @undirected*/
	public MD_Metadata[] has = null;

	public String getFeature() {
		String result = null;
		result += "<identifier>" + has[0].fileIdentifier + "</identifier>";
		result += "<display-name>" + has[0].metadataStandardName
				+ "</display-name>";
		result += "<bounding-box>";
		result += "<gml:coord>";
		result += "<gml:X>"
				+ ((MD_Georectified) has[0].spatialRepresentationInfo[0]).cornerPoints[0].x
				+ "</gml:X>";
		result += "<gml:Y>"
				+ ((MD_Georectified) has[0].spatialRepresentationInfo[0]).cornerPoints[0].y
				+ "</gml:Y>";
		result += "</gml:coord>";
		result += "<gml:coord>";
		result += "<gml:X>"
				+ ((MD_Georectified) has[0].spatialRepresentationInfo[0]).cornerPoints[2].x
				+ "</gml:X>";
		result += "<gml:Y>"
				+ ((MD_Georectified) has[0].spatialRepresentationInfo[0]).cornerPoints[2].y
				+ "</gml:Y>";
		result += "</gml:coord>";
		result += "</bounding-box>";

		return result;
	}
}
