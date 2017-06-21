package iso.iso19115.identification;

import iso.iso19115.metadata_extension.EX_Extent;

/**
 * @author Luiz Leme*/
public class MD_DataIdentification extends MD_Identification {

	/**
	 * @link association
	 * @associates <{MD_SpatialRepresentationTypeCode}>*/
	public String spatialRepresentationType = null;
	public MD_Resolution spatialResolution = null;

	public String language = null;

	/**
	 * @link association
	 * @associates <{MD_CharacterSetCode}>*/
	public String characterSet = "utf8";

	/**
	 * @link association
	 * @associates <{MD_TopicCategoryCode}>*/
	public String topicCategory = null;

	public String environmentDescription = null;
	public EX_Extent extent = null;

	public String suplementalInformation = null;
}
