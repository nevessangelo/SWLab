/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Insert;

import br.com.edu.connection.SearchEntitesBD;
import br.com.edu.objects.ClassObject;
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
public class InsertClassPartitions {
    
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, SQLException {
        
        String filename = "/home/angelo/Área de Trabalho/teste/void_2016-10-01_11-16-14.nq.gz";
        
        new File("/home/angelo/Área de Trabalho/teste/tdb").mkdirs();
        String assemblerFile = "/home/angelo/WebSemantica/apache-jena-fuseki/apache-jena-fuseki-2.4.0/run/configuration/readVoidFinal.ttl";
        Dataset ds2 = TDBFactory.assembleDataset(assemblerFile);
        RDFDataMgr.read(ds2, filename);
        
        Model modelClass = ModelFactory.createDefaultModel();
        modelClass.read("http://rdfs.org/ns/void");
        modelClass.setNsPrefix("class", "http://rdfs.org/ns/void#");
         
        Property classp = modelClass.getProperty("http://rdfs.org/ns/void#classPartition"); 
        Property classd = modelClass.getProperty("http://rdfs.org/ns/void#class");
        Property frquen = modelClass.getProperty("http://rdfs.org/ns/void#entities");
        
        ArrayList<ClassObject> list_class = SearchEntitesBD.searchClass();
        
        for(int i = 0; i < list_class.size(); i++){
             String frequen = String.valueOf(list_class.get(i).getFrequen());
             String name = list_class.get(i).getDataset();
             String graph = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/"+name;
             Model model = ds2.getNamedModel(graph);
             Resource teste_resource_class = model.createResource(graph).addProperty(classp, model.createResource(classp).addLiteral(classd, list_class.get(i).getName()).addProperty(frquen, frequen));
        }
        
        FileOutputStream out = new FileOutputStream("/home/angelo/Área de Trabalho/teste/TesteClass.nq");
        
        RDFDataMgr.write(out, ds2, Lang.NQUADS);
        
        ds2.close();
    }
    
}
