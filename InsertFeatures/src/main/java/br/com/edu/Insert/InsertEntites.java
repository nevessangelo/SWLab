/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Insert;

import br.com.edu.connection.SearchEntitesBD;
import br.com.edu.objects.Entites;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.DC;

/**
 *
 * @author angelo
 */
public class InsertEntites {
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException, FileNotFoundException {
        
       
        String filename = "/home/angelo/Área de Trabalho/teste/voids_2016-09-25_15-03-34.nq.gz";
       
        
        new File("/home/angelo/Área de Trabalho/teste/tdb").mkdirs();
        String assemblerFile = "/home/angelo/WebSemantica/apache-jena-fuseki/apache-jena-fuseki-2.4.0/run/configuration/read.ttl";
        Dataset ds2 = TDBFactory.assembleDataset(assemblerFile);
        RDFDataMgr.read(ds2, filename);
        
        Model modelProv = ModelFactory.createDefaultModel();
        modelProv.read("http://www.w3.org/ns/prov");
        modelProv.setNsPrefix("prov", "http://www.w3.org/ns/prov#");

        Resource keyValueProperty = modelProv.getResource("http://www.w3.org/ns/prov#KeyValuePair");
        Property key = modelProv.getProperty("http://www.w3.org/ns/prov#pairKey");
        Property value = modelProv.getProperty("http://www.w3.org/ns/prov#pairValue");
        ArrayList<Entites> entites = new ArrayList<>();
        
        entites = SearchEntitesBD.search();
      
        for(int i = 0; i < entites.size(); i++){
            String name = entites.get(i).getName();
            String convert = String.valueOf(entites.get(i).getFrequen());
            String graph = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/"+name;
            Model model = ds2.getNamedModel(graph);
            Resource teste_resource = model.createResource(graph).addProperty(FOAF.topic, model.createResource(keyValueProperty).addProperty(key, entites.get(i).getEntite()).addProperty(value, convert));
        }
        
        FileOutputStream out = new FileOutputStream("/home/angelo/Área de Trabalho/teste/Entites.nq");
        
        RDFDataMgr.write(out, ds2, Lang.NQUADS);
        
        ds2.close();

        
   
        
        
        

        
        
    }
    
}
