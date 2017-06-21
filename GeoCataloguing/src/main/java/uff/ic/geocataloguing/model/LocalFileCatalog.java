/* Generated by Together */

package uff.ic.geocataloguing.model;

import iso.iso19139.dataset.DS_DataSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import uff.ic.geocataloguing.OOISO19115GeoImage;

/**
 * @author Luiz Andr�
 * @version 1.0
 * @since 1.0*/
public class LocalFileCatalog extends Catalog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4386466832338445378L;

	/**
	 * @link aggregation
	 * @associates <{br.pucrio.inf.catfwk.model.DatasetDescription}>
	 * @supplierCardinality 0..**/
	private HashMap descriptions = null;

	public String filename = null;

	/**
	 * 
	 */
	public LocalFileCatalog(String filename) {
		super("");
		this.filename = filename;
	}

	public synchronized DatasetDescription put(
			DatasetDescription datasetDescription) throws IOException,
			ClassNotFoundException {
		load();
		DatasetDescription _datasetDescription = (DatasetDescription) descriptions
				.put(datasetDescription.getUri(), datasetDescription);
		save();
		return _datasetDescription;
	}

	public synchronized DatasetDescription remove(
			DatasetDescription datasetDescription) throws IOException,
			ClassNotFoundException {
		load();
		DatasetDescription _datasetDescription = (DatasetDescription) descriptions
				.remove(datasetDescription.getUri());
		save();
		return _datasetDescription;
	}

	public synchronized DatasetDescription get(Object key) throws IOException,
			ClassNotFoundException {
		load();
		return (DatasetDescription) descriptions.get(key);
	}

	public synchronized boolean contains(String filename) throws IOException,
			ClassNotFoundException {
		load();
		return descriptions.containsKey(filename);
	}

	public int size() {
		return descriptions.size();
	}

	/**
	 * @return Returns the filename.
	 */
	public String getFilename() {
		return filename;
	}

	public synchronized void save() throws IOException {
		ObjectOutputStream s = null;
		FileOutputStream out = null;
		out = new FileOutputStream(getFilename());
		s = new ObjectOutputStream(out);
		s.writeObject(descriptions);
		s.flush();
		s.close();
	}

	public synchronized void exportXML() throws IOException,
			ClassNotFoundException {
		FileOutputStream file = new FileOutputStream(getFilename().substring(0,
				getFilename().lastIndexOf('.'))
				+ ".xml");
		Writer out = new OutputStreamWriter(file, "UTF8");
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.flush();
		OOISO19115GeoImage[] imageDescription = getImages();
		out.write("<resources>");
		for (int i = 0; i < imageDescription.length; i++) {
			out.write("<resource xmlns:gml=\"http://www.opengis.net/gml\">");
			out.write(imageDescription[i].getDataset());
			out.write("<features>");
			DS_DataSet[] features = imageDescription[i].getDataSets();
			for (int j = 1; j < features.length; j++) {
				out.write("<feature>");
				out.write(features[j].getFeature());
				out.write("</feature>");
			}
			out.write("</features>");
			out.write("</resource>");
		}
		out.write("</resources>");
		out.close();

	}

	private OOISO19115GeoImage[] getImages() throws IOException,
			ClassNotFoundException {
		return (OOISO19115GeoImage[]) values().toArray(
				new OOISO19115GeoImage[0]);
	}

	public synchronized void load() throws IOException, ClassNotFoundException {
		if (descriptions == null) {
			File file = new File(getFilename());
			if (file.exists()) {
				FileInputStream in = new FileInputStream(getFilename());
				ObjectInputStream s = new ObjectInputStream(in);
				descriptions = (HashMap) s.readObject();
				s.close();
			} else {
				descriptions = new HashMap();
				ObjectOutputStream s = null;
				FileOutputStream out = null;
				out = new FileOutputStream(getFilename());
				s = new ObjectOutputStream(out);
				s.writeObject(descriptions);
				s.flush();
				s.close();
			}
		}
	}

	public synchronized void connect() {
		// TODO Auto-generated method stub

	}

	public synchronized void disconnect() {
		// TODO Auto-generated method stub

	}

	public synchronized Collection values() throws IOException,
			ClassNotFoundException {
		load();
		return descriptions.values();
	}

	public DatasetDescription getDesciption(DatasetDescription datsetDescription) {
		// TODO Auto-generated method stub
		return null;
	}

	public Builder getBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	public DatasetDescription getDescription(Object obj) throws IOException,
			InvalidDatasetDescException, ParserConfigurationException,
			SAXException {
		return null;
	}
}
