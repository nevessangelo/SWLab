/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.Insert;

import br.com.edu.connection.SearchEntitesBD;
import br.com.edu.objects.ClassObject;
import br.com.edu.objects.Entites;
import br.com.edu.objects.PropretyPartitionObject;
import br.com.edu.objects.Types;
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
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.DC_11;

/**
 *
 * @author angelo
 */
public class InsertFeatures {

    public static void InsertClassPartition(Dataset ds2) throws FileNotFoundException, ClassNotFoundException, SQLException {
        Model modelClass = ModelFactory.createDefaultModel();
        modelClass.read("http://rdfs.org/ns/void");
        modelClass.setNsPrefix("class", "http://rdfs.org/ns/void#");

        Property classp = modelClass.getProperty("http://rdfs.org/ns/void#classPartition");
        Property classd = modelClass.getProperty("http://rdfs.org/ns/void#class");
        Property frquen = modelClass.getProperty("http://rdfs.org/ns/void#entities");

        ArrayList<ClassObject> list_class = SearchEntitesBD.searchClass();

        for (int i = 0; i < list_class.size(); i++) {
            String frequen = String.valueOf(list_class.get(i).getFrequen());
            String name = list_class.get(i).getDataset();
            String graph = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/" + name;
            Model model = ds2.getNamedModel(graph);
            Resource teste_resource_class = model.createResource(graph).addProperty(classp, model.createResource(classp).addLiteral(classd, list_class.get(i).getName()).addProperty(frquen, frequen));
        }

        FileOutputStream out = new FileOutputStream("/home/angelo/Área de Trabalho/teste/TesteClass.nq");

        RDFDataMgr.write(out, ds2, Lang.NQUADS);

    }

 public static void InsertPropretyPartition(Dataset ds2) throws ClassNotFoundException, SQLException, FileNotFoundException{
        
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

   

        
    }
    public static void InsertEntites(Dataset ds2) throws FileNotFoundException, ClassNotFoundException, SQLException {

        Model modelProv = ModelFactory.createDefaultModel();
        modelProv.read("http://www.w3.org/ns/prov");
        modelProv.setNsPrefix("prov", "http://www.w3.org/ns/prov#");

        Resource keyValueProperty = modelProv.getResource("http://www.w3.org/ns/prov#KeyValuePair");
        Property key = modelProv.getProperty("http://www.w3.org/ns/prov#pairKey");
        Property value = modelProv.getProperty("http://www.w3.org/ns/prov#pairValue");
        ArrayList<Entites> entites = new ArrayList<>();

        entites = SearchEntitesBD.search();

        for (int i = 0; i < entites.size(); i++) {
            String name = entites.get(i).getName();
            String convert = String.valueOf(entites.get(i).getFrequen());
            String graph = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/" + name;
            Model model = ds2.getNamedModel(graph);
            Resource teste_resource = model.createResource(graph).addProperty(FOAF.topic, model.createResource(keyValueProperty).addProperty(key, entites.get(i).getEntite()).addProperty(value, convert));
        }

        FileOutputStream out = new FileOutputStream("/home/angelo/Área de Trabalho/teste/Entites.nq");

        RDFDataMgr.write(out, ds2, Lang.NQUADS);

        ds2.close();

    }

    public static void InsertTypes(Dataset ds2) throws FileNotFoundException, ClassNotFoundException, SQLException {

        Model modelProv = ModelFactory.createDefaultModel();
        modelProv.read("http://www.w3.org/ns/prov");
        modelProv.setNsPrefix("prov", "http://www.w3.org/ns/prov#");

        Resource keyValueProperty = modelProv.getResource("http://www.w3.org/ns/prov#KeyValuePair");
        Property key = modelProv.getProperty("http://www.w3.org/ns/prov#pairKey");
        Property value = modelProv.getProperty("http://www.w3.org/ns/prov#pairValue");
        ArrayList<Types> types = new ArrayList<>();

        types = SearchEntitesBD.searchtypes();

        for (int i = 0; i < types.size(); i++) {
            String name = types.get(i).getName();
            String convert = String.valueOf(types.get(i).getFrequen());
            String graph = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/" + name;
            Model model = ds2.getNamedModel(graph);
            Resource teste_resource = model.createResource(graph).addProperty(DC.subject, model.createResource(keyValueProperty).addProperty(key, types.get(i).getEntite()).addProperty(value, convert));
        }

        FileOutputStream out = new FileOutputStream("/home/angelo/Área de Trabalho/teste/Types.nq");

        RDFDataMgr.write(out, ds2, Lang.NQUADS);

        ds2.close();

    }

}
