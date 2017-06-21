package uff.ic.geocataloguing.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Luiz Andr�
 * @version 1.0
 * @since 1.0
 * @alias FrameworkFactory*/
public abstract class FrameworkFactory implements Serializable {

	public final Repository[] createRepositories(String[] directories)
			throws InvalidRepositoryException {
		Repository[] repositories = new Repository[directories.length];
		for (int i = 0; i < directories.length; i++) {
			if (directories[i] == null)
				throw new InvalidRepositoryException(
						"ResourceInput directory==null");
			repositories[i] = new FileSystemDirectory(directories[i]);
		}
		return repositories;
	}

	public final Catalog createFileCatalog(String filename) {
		return new LocalFileCatalog(filename);
	}

	public final Catalog createMemoryCatalog() {
		return new MemoryCatalog();
	}

	public abstract Gazetteer[] createDictionaries(String[][] urls)
			throws InvalidDictionaryException;

	public abstract HashMap createParsers(Builder builder);

	public abstract RepositoryDescBuilder createRepositoryDescBuilder();

	public abstract DictionaryDescBuilder createDictionaryDescBuilder();
}