package iso.iso19115.metadata;

import iso.iso19109.metadata.GF_AtributeType;
import iso.iso19109.metadata.GF_FeatureType;
import iso.iso19109.metadata.GF_PropertyType;
import iso.iso19115.citation.CI_ResponsibleParty;
import iso.iso19115.identification.MD_Identification;
import iso.iso19115.maintenance.MD_ScopeCode;
import iso.iso19115.spatial_representation.MD_SpatialRepresentation;
import iso.iso19139.dataset.DS_Aggregate;
import iso.iso19139.dataset.DS_DataSet;

import java.io.Serializable;
import java.sql.Date;
import java.util.Vector;

/**
 * @author Luiz André
 * @version 1.0
 * @since 1.0
 * @alias MD_Metadata*/
public class MD_Metadata implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5778727586071345988L;

	/**
	 * @link aggregation
	 * @clientCardinality 0..*
	 * @supplierCardinality 0..*
	 * @clientRole +featureTypeMetadata
	 * @supplierRole +featureType*/
	public GF_FeatureType featureType = null;

	/**
	 * @link aggregation
	 * @clientCardinality 0..*
	 * @supplierCardinality 0..*
	 * @clientRole +featureAtributeMetadata
	 * @supplierRole +featureAtribute*/
	public GF_AtributeType atributeType = null;

	/**
	 * @link aggregation
	 * @clientCardinality 0..*
	 * @supplierCardinality 0..*
	 * @directed
	 * @clientRole +propertyTypeMetadata
	 * @supplierQualifier +propertyType*/
	public GF_PropertyType lnkGF_PropertyType = null;

	/**
	 * @link aggregation
	 * @supplierCardinality 1..*
	 * @supplierRole +identificationInfo
	 * @directed*/
	public MD_Identification[] identificationInfo = null;

	public String fileIdentifier = null;

	public String language = null;

	public String characterSet = "utf8";

	public String parentIdentifier = null;

	/**
	 * @link association
	 * @associates <{iso.iso19115.maintenance.MD_ScopeCode}>*/
	public MD_ScopeCode hierarchyLevel = null;

	public String hierarchyLevelName = null;
	public CI_ResponsibleParty contact = null;

	public Date dateStamp = null;

	public String metadataStandardName = null;

	public String metadataStandardVersion = null;

	public String dataSetURI = null;

	/**
	 * @link aggregation
	 * @associates <{iso.iso19115.spatial_representation.MD_SpatialRepresentation}>
	 * @supplierCardinality 0..*
	 * @supplierRole +spatialRepresentationInfo*/
	public MD_SpatialRepresentation[] spatialRepresentationInfo = null;

	/**
	 * @bidirectional
	 */
	public DS_Aggregate series;

	/**
	 * @bidirectional
	 */
	private DS_DataSet describes;

	/***************************************************************************
	 * @link aggregation
	 * @associates <{iso.iso19115.data_quality.DQ_DataQuality}>
	 * @supplierRole +dataQualityInfo
	 * @supplierCardinality 0..*/
	private Vector dataQualityInfo;

	/**
	 * @link aggregation
	 * @associates <{iso.iso19115.distribution.MD_Distribution}>
	 * @supplierRole +distributionInfo
	 * @supplierCardinality 0..1*/
	private Vector distributionInfo;

	/**
	 * @link aggregation
	 * @associates <{iso.iso19115.content.MD_ContentInformation}>
	 * @supplierRole +contentInfo
	 * @supplierCardinality 0..**/
	private Vector contentInfo;

	/**
	 * @link aggregation
	 * @associates <{iso.iso19115.portrayal_catalogue.MD_PortrayalCatalogueReference}>
	 * @supplierRole +portrayalCatalogueInfo
	 * @supplierCardinality 0..**/
	private Vector portrayalCatalogueInfo;

	/**
	 * @link aggregation
	 * @associates <{iso.iso19115.application_schema.MD_ApplicationSchemaInformation}>
	 * @supplierRole +applicationSchemaInfo
	 * @supplierCardinality 0..**/
	private Vector applicationSchemaInfo;

	/**
	 * @link aggregation
	 * @associates <{iso.iso19115.constraint.MD_Conatraints}>
	 * @supplierRole +metadataConatraints
	 * @supplierCardinality 0..**/
	private Vector metadataConatraints;

	/**
	 * @link aggregation
	 * @associates <{iso.iso19115.maintenance.MD_MaintenanceInformation}>
	 * @supplierRole +metadataMaintenance
	 * @supplierCardinality 0..1*/
	private Vector metadataMaintenance;

	/**
	 * @link aggregation
	 * @associates <{iso.iso19115.metadata_extension.MD_MetadaExtensionInformation}>
	 * @supplierRole +metadataExtensionInfo
	 * @supplierCardinality 0..**/
	private Vector metadaExtensionInfo;

	/**
	 * @link aggregation
	 * @associates <{iso.iso19115.reference_system.MD_ReferenceSystem}>
	 * @supplierRole +referenceSystemInfo
	 * @supplierCardinality 0..**/
	private Vector referenceSystemInfo;
}
