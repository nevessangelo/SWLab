/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.DBPedia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.Objects.Types_;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

/**
 *
 * @author angelo
 */
public class SparqlDBPedia {
    
    public static void ArmazenaThing(String nome_dataset, String entite, Connection conn) throws ClassNotFoundException, SQLException {
        String type_ = "Thing/0";
        int verifica_type_entite = InsertFeaturesBD.VerificaTypeEntite(nome_dataset, type_, entite, conn);
        if (verifica_type_entite == 0) {
            double frequencia_types = InsertFeaturesBD.VerificaUpdateTypes(nome_dataset, type_, conn);
            if (frequencia_types == 0) {
                double frequencia = InsertFeaturesBD.UpdateFrequencia(nome_dataset, entite, conn);
                Types_ obj_type = new Types_();
                obj_type.setName_dataset(nome_dataset);
                obj_type.setFeature(type_);
                obj_type.setFrequen(frequencia);
                obj_type.setType("Dump");
                InsertFeaturesBD.InsertTypes(obj_type, conn);
                InsertFeaturesBD.InsertTypeVerifica(nome_dataset, entite, type_, conn);
            } else {
                double frequen_total = 0;
                double frequencia_types_update = InsertFeaturesBD.GetFrequenType(nome_dataset, entite, conn);
                frequen_total = frequencia_types_update + frequencia_types;
                InsertFeaturesBD.UpdateTypes(nome_dataset, frequen_total, type_, conn);
                InsertFeaturesBD.InsertTypeVerifica(nome_dataset, entite, type_, conn);
            }

        }

    }
    
    
    public static String Raiz(String type) {
        String superclass = null;
        String queryString = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "prefix DBpedia: <http://dbpedia.org/ontology/>"
                + "\n"
                + "SELECT DISTINCT ?superclass WHERE {\n"
                + " " + type + " rdfs:subClassOf ?superclass.\n"
                + "\n"
                + "}";
        //System.out.println(queryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", queryString);
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {
                QuerySolution soln = rs.nextSolution();
                superclass = String.valueOf(soln.get("superclass"));
                if (!superclass.contains("DUL.owl#SocialPerson")) {
                    if (superclass.contains("Thing")) {
                        return "Thing";
                    } else if (superclass.contains("http://dbpedia.org/ontology/")) {
                        String[] vetor = superclass.split("/");
                        int tamanho = vetor.length - 1;
                        String type_retorno = "DBpedia:" + vetor[tamanho];
                        return type_retorno;

                    }

                }

            }

        } finally {
            qexec.close();
        }
        return superclass;

    }
    
     public static int VerificaSubClass(String type) {
        String queryString = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "prefix DBpedia: <http://dbpedia.org/ontology/>\n"
                + "\n"
                + "SELECT DISTINCT ?subclass WHERE {\n"
                + "    ?subclass rdfs:subClassOf  " + type + " \n"
                + "}";
        //System.out.println(queryString);
        //Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", queryString);
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {
                QuerySolution soln = rs.nextSolution();
                String subclass = String.valueOf(soln.get("subclass"));
                if ((subclass.contains("http://dbpedia.org/ontology/")) && (!subclass.contains("www.ontologydesignpatterns.org/ont/dul/DUL.owl#SocialPerson"))) {
                    return 1;
                }
            }
        } finally {
            qexec.close();

        }
        return 0;

    }

     public static void ArmazenaType(String nome_dataset, String classse, String entite, Connection conn) throws ClassNotFoundException, SQLException {
        String entite_decode = java.net.URLDecoder.decode(entite);
        ArmazenaThing(nome_dataset, entite_decode, conn);
        System.out.println("Type da entidade: " + entite_decode);
        String verifica_nivel = classse;
        int nivel = 0;
        while ((!verifica_nivel.equals("Thing")) && (!verifica_nivel.equals("http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#SocialPerson"))) {
            nivel = nivel + 1;
            verifica_nivel = Raiz(verifica_nivel);

        }
        String classse_armazena = classse + "/" + nivel;
        int verifica_type_entite = InsertFeaturesBD.VerificaTypeEntite(nome_dataset, classse_armazena, entite_decode, conn);
        if (verifica_type_entite == 0) {
            double frequencia_types = InsertFeaturesBD.VerificaUpdateTypes(nome_dataset, classse_armazena, conn);
            if (frequencia_types == 0) {
                double frequencia = InsertFeaturesBD.UpdateFrequencia(nome_dataset, entite_decode, conn);
                Types_ obj_type = new Types_();
                obj_type.setName_dataset(nome_dataset);
                obj_type.setFeature(classse_armazena);
                obj_type.setFrequen(frequencia);
                obj_type.setType("Dump");
                InsertFeaturesBD.InsertTypes(obj_type, conn);
                InsertFeaturesBD.InsertTypeVerifica(nome_dataset, entite_decode, classse_armazena, conn);
            } else {
                double frequen_total = 0;
                double frequencia_types_update = InsertFeaturesBD.GetFrequenType(nome_dataset, entite_decode, conn);
                frequen_total = frequencia_types_update + frequencia_types;
                InsertFeaturesBD.UpdateTypes(nome_dataset, frequen_total, classse_armazena, conn);
                InsertFeaturesBD.InsertTypeVerifica(nome_dataset, entite_decode, classse_armazena, conn);
            }

            GetSuperClass(nome_dataset, classse, entite_decode, conn);
            //GetSubClass(nome_dataset, classse, entite_decode, conn);

        } else {
            //System.out.println("Type repetido do dataset: " + nome_dataset);
        }

    }
     public static void GetSuperClass(String nome_dataset, String classse, String entite, Connection conn) throws ClassNotFoundException, SQLException {
        String queryString = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "prefix DBpedia: <http://dbpedia.org/ontology/>"
                + "\n"
                + "SELECT DISTINCT ?superclass WHERE {\n"
                + " " + classse + " rdfs:subClassOf ?superclass.\n"
                + "\n"
                + "}";
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", queryString);
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {
                QuerySolution soln = rs.nextSolution();
                String superclass = String.valueOf(soln.get("superclass"));
                if (!superclass.contains("DUL.owl#SocialPerson")) {
                    if (superclass.contains("http://dbpedia.org/ontology/")) {
                        String[] vetor = superclass.split("/");
                        int tamanho = vetor.length - 1;
                        String type = "DBpedia:" + vetor[tamanho];
                        String verifica_nivel = type;
                        int nivel = 0;
                        while (!verifica_nivel.equals("Thing")) {
                            nivel = nivel + 1;
                            verifica_nivel = Raiz(verifica_nivel);
                        }
                        String type_armazena = type + "/" + nivel;
                        System.out.println(type_armazena);
                        int verifica_type_entite = InsertFeaturesBD.VerificaTypeEntite(nome_dataset, type_armazena, entite, conn);
                        if (verifica_type_entite == 0) {
                            double frequencia_types = InsertFeaturesBD.VerificaUpdateTypes(nome_dataset, type_armazena, conn);
                            if (frequencia_types == 0) {
                                double frequencia = InsertFeaturesBD.UpdateFrequencia(nome_dataset, entite, conn);
                                Types_ obj_type = new Types_();
                                obj_type.setName_dataset(nome_dataset);
                                obj_type.setFeature(type_armazena);
                                obj_type.setFrequen(frequencia);
                                obj_type.setType("Dump");
                                InsertFeaturesBD.InsertTypes(obj_type, conn);
                                InsertFeaturesBD.InsertTypeVerifica(nome_dataset, entite, type_armazena, conn);
                            } else {
                                double frequen_total = 0;
                                double frequencia_types_update = InsertFeaturesBD.GetFrequenType(nome_dataset, entite, conn);
                                frequen_total = frequencia_types_update + frequencia_types;
                                InsertFeaturesBD.UpdateTypes(nome_dataset, frequen_total, type_armazena, conn);
                                InsertFeaturesBD.InsertTypeVerifica(nome_dataset, entite, type_armazena, conn);
                            }

                        }
                    }                  
                }
                

            }
        } finally {
            qexec.close();
        }
        
       

    }

      public static void GetSubClass(String nome_dataset, String classse, String entite, Connection conn) throws ClassNotFoundException, SQLException {
        String type = null;
        String queryString = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "prefix DBpedia: <http://dbpedia.org/ontology/>\n"
                + "\n"
                + "SELECT DISTINCT ?subclass WHERE {\n"
                + "    ?subclass rdfs:subClassOf " + classse + " \n"
                + "}";
        //Query query = QueryFactory.create(queryString);
        //System.out.println(queryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", queryString);
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {
                QuerySolution soln = rs.nextSolution();
                String subclass = String.valueOf(soln.get("subclass"));
                if ((subclass.contains("http://dbpedia.org/ontology/")) && (!subclass.contains("www.ontologydesignpatterns.org/ont/dul/DUL.owl#SocialPerson"))) {
                    String[] vetor = subclass.split("/");
                    int tamanho = vetor.length - 1;
                    type = "DBpedia:" + vetor[tamanho];
                    String verifica_nivel = type;
                    int nivel = 0;
                    while (!verifica_nivel.equals("Thing")) {
                        nivel = nivel + 1;
                        verifica_nivel = Raiz(verifica_nivel);
                    }
                    String type_armazena = type + "/" + nivel;
                    System.out.println(type_armazena);
                    int verifica_type_entite = InsertFeaturesBD.VerificaTypeEntite(nome_dataset, type_armazena, entite, conn);
                    if (verifica_type_entite == 0) {
                        double frequencia_types = InsertFeaturesBD.VerificaUpdateTypes(nome_dataset, type_armazena, conn);
                        if (frequencia_types == 0) {
                            double frequencia = InsertFeaturesBD.UpdateFrequencia(nome_dataset, entite, conn);
                            Types_ obj_type = new Types_();
                            obj_type.setName_dataset(nome_dataset);
                            obj_type.setFeature(type_armazena);
                            obj_type.setFrequen(frequencia);
                            obj_type.setType("Dump");
                            InsertFeaturesBD.InsertTypes(obj_type, conn);
                            InsertFeaturesBD.InsertTypeVerifica(nome_dataset, entite, type_armazena, conn);
                        } else {
                            double frequen_total = 0;
                            double frequencia_types_update = InsertFeaturesBD.GetFrequenType(nome_dataset, entite, conn);
                            frequen_total = frequencia_types_update + frequencia_types;
                            InsertFeaturesBD.UpdateTypes(nome_dataset, frequen_total, type_armazena, conn);
                            InsertFeaturesBD.InsertTypeVerifica(nome_dataset, entite, type_armazena, conn);
                        }

                    }

                    //System.out.println(type + "/" + nivel);
                    int verifica = VerificaSubClass(type);
                    if (verifica == 1) {
                        GetSubClass(nome_dataset, type, entite, conn);
                    }
                }

            }
        } finally {
            qexec.close();
        }

    }

    
}
