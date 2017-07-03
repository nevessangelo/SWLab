package uff.ic.geocataloguing;

import iso.iso19139.dataset.DS_DataSet;

/**
 * @author Luiz Leme
 * @alias GeoImageInterface*/
public interface GeoImageInterface {
	/**
	 * @param name
	 */
	public abstract void setName(String name);

	/**
	 * @return
	 */
	public abstract String getName();

	/**
	 * @param x
	 */
	public abstract void setLeftUpperCornerPointX(double x);

	/**
	 * @param y
	 */
	public abstract void setLeftUpperCornerPointY(double y);

	/**
	 * @return
	 */
	public abstract double getLeftUpperCornerPointX();

	/**
	 * @return
	 */
	public abstract double getLeftUpperCornerPointY();

	/**
	 * @param x
	 */
	public abstract void setRightLowerCornerPointX(double x);

	/**
	 * @param y
	 */
	public abstract void setRightLowerCornerPointY(double y);

	/**
	 * @return
	 */
	public abstract double getRightLowerCornerPointX();

	/**
	 * @return
	 */
	public abstract double getRightLowerCornerPointY();

	public abstract DS_DataSet[] getDataSets();
}