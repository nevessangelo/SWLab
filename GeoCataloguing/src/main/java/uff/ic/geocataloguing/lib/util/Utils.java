package uff.ic.geocataloguing.lib.util;

import java.io.File;

import javax.swing.ImageIcon;

/* Utils.java is a 1.4 example used by FileChooserDemo2.java. */

public class Utils {
	public final static String jpeg = "jpeg";

	public final static String jpg = "jpg";

	public final static String gif = "gif";

	public final static String tiff = "tiff";

	public final static String tif = "tif";

	public final static String png = "png";

	public final static String xml = "xml";

	/*
	 * Get the extension of a file.
	 */
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Utils.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		}
		System.err.println("Couldn't find file: " + path);
		return null;
	}

	public boolean equals(String[] s1, String[] s2) {
		if (s1 == null || s2 == null)
			return false;
		if (s1.length != s2.length)
			return false;
		if (s1.length > 0)
			for (int i = 0; i < s1.length; i++)
				if (s1[i] == null || s2[i] == null || !s1[i].equals(s2[i]))
					return false;

		return true;
	}

	public boolean equals(String[][] s1, String[][] s2) {
		if (s1 == null || s2 == null)
			return false;
		if ((s1.length != s2.length) || (s1[0].length != s2[0].length))
			return false;
		if (s1.length > 0)
			for (int i = 0; i < s1.length; i++)
				for (int j = 0; j < s1[0].length; j++)
					if (s1[i][j] == null || s2[i][j] == null
							|| !s1[i][j].equals(s2[i][j]))
						return false;

		return true;
	}
}
