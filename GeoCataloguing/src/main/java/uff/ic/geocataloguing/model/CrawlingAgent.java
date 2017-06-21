package uff.ic.geocataloguing.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import uff.ic.geocataloguing.lib.agent.Agent;

/**
 * @version 1.0
 * @since 1.0
 * @author Luiz Andr�
 * @alias CrawlingAgent*/
public class CrawlingAgent extends Agent {
	public CrawlingAgent(String id, Repository[] sites, Catalog tempCatalog,
			Catalog catalog, Catalog fails) {
		super(id);
		this.repositories = sites;
		this.catalog = catalog;
		this.fails = fails;
		this.tempCatalog = tempCatalog;
	}

	public void run() {
		super.run();
	}

	/**
	 * Busca novas imagens, extrai seus conte�dos e os envia ao CataloguingAgent
	 * do qual � colaborador
	 * 
	 * @since 1.0
	 */
	public void agentBehavior() {
		String[] filenames = null;
		DatasetDescription dataset = null;
		ArrayList newDatasets = new ArrayList();

		/*
		 * Para cada local recupera uma lista de seus arquivos
		 */
		try {
			for (int i = 0; i < repositories.length; i++) {
				filenames = repositories[i].listURI();
				if (filenames != null)
					/*
					 * Para cada arquivo recupera seus metadados
					 */
					for (int j = 0; j < filenames.length; j++) {
						try {
							/*
							 * Verifica se � um novo Resource
							 */
							if (!catalog.contains(filenames[j])
									&& !fails.contains(filenames[j])
									&& !tempCatalog.contains(filenames[j])) {
								/*
								 * Recupera metadados: getContent(filenames[j])
								 */
								dataset = repositories[i]
										.getDescription(filenames[j]);
								dataset.setUri(filenames[j]);
								/*
								 * Adiciona Resource � lista de novos
								 */
								newDatasets.add(dataset);
							}
						} catch (NumberFormatException e) {
							System.err.println("Erro na conver��o de tipos "
									+ "na cria��o do information resource "
									+ "do arquivo " + filenames[j]);
						} catch (MalformedURLException e) {
							System.err
									.println("Erro no nome do arquivo "
											+ "do information resource "
											+ filenames[j]);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							getProcess().reportError();
						} catch (InvalidDatasetDescException e) {
							System.err.println("Erro na decodifica��o de "
									+ "meta-informa��es do arquivo "
									+ "do information resource: "
									+ filenames[j]);
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
							getProcess().reportError();
						} catch (SAXException e) {
							e.printStackTrace();
							getProcess().reportError();
						} catch (IOException e) {
							e.printStackTrace();
							getProcess().reportError();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
							getProcess().reportError();
						}
					}
			}
			/*
			 * Cria uma mensagem para cada novo Resource e as envia aos agentes
			 * CataloguingAgent
			 */
			if (!newDatasets.isEmpty()) {
				for (int i = 0; i < newDatasets.size(); i++) {
					if (newDatasets.get(i) != null) {
						tempCatalog
								.put((DatasetDescription) newDatasets.get(i));
						sendMessage(new HarvestResource(newDatasets.get(i)));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			getProcess().reportError();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			getProcess().reportError();
		}

		try {
			System.out.println("Agent id:" + getId()
					+ " iniciando pausa de 30 seg.");
			Thread.sleep(30000l);
			System.out.println("Agent id:" + getId()
					+ " retornando da pausa de 30 seg.");
		} catch (InterruptedException e) {
			System.out.println("Agent id:" + getId()
					+ " reiniciando processamento ap�s receber nova mensagem.");
		}
	}

	/**
	 * @link aggregation
	 * @supplierCardinality 1..*
	 * @label searches
	 * @labelDirection forward*/
	private Repository[] repositories = null;

	/**
	 * @supplierRole +tempCatalog*/
	private Catalog tempCatalog;

	/**
	 * @supplierRole +catalog*/
	private Catalog catalog;

	/**
	 * @supplierRole +fails*/
	private Catalog fails = null;

}
