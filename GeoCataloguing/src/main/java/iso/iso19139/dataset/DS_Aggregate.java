package iso.iso19139.dataset;

import iso.iso19115.metadata.MD_Metadata;

import java.io.Serializable;

/**
 * @author Luiz André
 * @version 1.0
 * @since 1.0
 * @alias DS_Aggregate
 * @stereotype abstract*/
abstract public class DS_Aggregate implements Serializable {
	/**
	 * @link aggregation
	 * @associates <{DS_DataSet}>
	 * @clientRole +partOf
	 * @clientCardinality 0..*
	 * @supplierRole +composedOf
	 * @supplierCardinality 1..*
	 * @directed*/
	public DS_DataSet[] composedOf = null;

	/**
	 * @directed
	 * @clientRole +superset
	 * @clientCardinality 0..*
	 * @supplierRole +subSet
	 * @supplierCardinality 0..*
	 * @associationAsClass MultipleAggregation*/
	public DS_Aggregate[] subSet = null;

	/**
	 * @associates <{MD_Metadata}>
	 * @supplierRole +seriesMetadata
	 * @clientRole +series
	 * @supplierCardinality 1..*
	 * @clientCardinality 0..*
	 * @bidirectional <{iso.iso19115.metadata.MD_Metadata#series}>
	 * @undirected*/
	public MD_Metadata[] seriesMetadata = null;
}
