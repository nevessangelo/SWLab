/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Insert;

import br.com.edu.connection.SearchEntitesBD;
import br.com.edu.objects.PropretyPartitionObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;

/**
 *
 * @author angelo
 */
public class InsertPropretyPartitions {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, FileNotFoundException {

        String filename = "/home/angelo/Área de Trabalho/teste/void_2016-10-01_11-16-14.nq.gz";
        
        new File("/home/angelo/Área de Trabalho/teste/tdb").mkdirs();
        String assemblerFile = "/home/angelo/WebSemantica/apache-jena-fuseki/apache-jena-fuseki-2.4.0/run/configuration/readVoidFinal.ttl";
        Dataset ds2 = TDBFactory.assembleDataset(assemblerFile);
        RDFDataMgr.read(ds2, filename);

        Model modelProprety = ModelFactory.createDefaultModel();
        modelProprety.read("http://rdfs.org/ns/void");
        modelProprety.setNsPrefix("class", "http://rdfs.org/ns/void#");

        Property classp = modelProprety.getProperty("http://rdfs.org/ns/void#propertyPartition");
        Property classd = modelProprety.getProperty("http://rdfs.org/ns/void#property");
        Property frquen = modelProprety.getProperty("http://rdfs.org/ns/void#triples");

        ArrayList<PropretyPartitionObject> list_proprety = SearchEntitesBD.searchProprety();

        for (int i = 0; i < list_proprety.size(); i++) {
            String frequen = String.valueOf(list_proprety.get(i).getFrequen());
            String name = list_proprety.get(i).getDataset();
            String graph = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/" + name;
            Model model = ds2.getNamedModel(graph);
            Resource teste_resource_class = model.createResource(graph).addProperty(classp, model.createResource(classp).addLiteral(classd, list_proprety.get(i).getName()).addProperty(frquen, frequen));
        }

        FileOutputStream out = new FileOutputStream("/home/angelo/Área de Trabalho/teste/TesteProprety.nq");

        RDFDataMgr.write(out, ds2, Lang.NQUADS);

        ds2.close();

    }

}
