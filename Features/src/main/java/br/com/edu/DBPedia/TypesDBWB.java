/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.DBPedia;

import java.sql.Connection;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author angelo
 */
public class TypesDBWB {

    public static void getType(String nome_dataset, String entite, Connection conn) {
        try {
         
            Client client = new Client();

            WebResource webResource = client
                    .resource("http://model.dbpedia-spotlight.org/en/annotate/?confidence=0.8&text=" + entite + "&support=100");

            ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            String output = response.getEntity(String.class);

            JSONObject my_obj = new JSONObject(output);
            try {
                JSONArray Resources = my_obj.getJSONArray("Resources");
                for (int i = 0; i < Resources.length(); i++) {
                    JSONObject resource = Resources.getJSONObject(i);
                    if (resource != null) {
                        String types = resource.getString("@types");
                        if (types != null || !types.equals("")) {
                            String[] types_dbpedia = types.split(",");
                            for (String type : types_dbpedia) {
                                int retorno = 0;
                                if (type.contains("DBpedia")) {
                                    SparqlDBPedia.ArmazenaType(nome_dataset, type, entite, conn);
                                }
                            }

                        }

                    }
                }

            } catch (JSONException e) {
                System.out.println("Não foi encontrado Tipos");
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
