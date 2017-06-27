/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.DBPedia;
import br.com.edu.Connection.InsertFeaturesBD;
import br.com.edu.Objects.Entites;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.sql.Connection;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author angelo
 */
public class DBpediaWB {
    public static void ArmazenaEntite(String entite, String nome_dataset, Connection conn) throws ClassNotFoundException, SQLException {
        double frequencia;
        frequencia = InsertFeaturesBD.UpdateFrequencia(nome_dataset, entite, conn);
        if (frequencia >= 1) {
            InsertFeaturesBD.Update(nome_dataset, frequencia, entite, conn);
        } else {
            frequencia = 1;
            Entites entites = new Entites();
            entites.setName_dataset(nome_dataset);
            entites.setFeature(entite);
            entites.setFrequen(1);
            entites.setType("Dump");
            InsertFeaturesBD.InsertEntites(entites, conn);
        }
    }
    
    public static void getEntite(String text, String nome_dataset, Connection conn) {
        try {
            //URLConnectionClientHandler ch = new URLConnectionClientHandler(new br.com.edu.DBPedia.ConnectionFactory());
            //Client client = new Client(ch);
            Client client = new Client();
            WebResource webResource = client
                    .resource("http://model.dbpedia-spotlight.org/en/annotate/?confidence=0.6&text="+ text + "&support=100");
            
            ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            String output = response.getEntity(String.class);

            System.out.println("Output from Server .... \n");
            JSONObject my_obj = new JSONObject(output);
            try {
                JSONArray Resources = my_obj.getJSONArray("Resources");
                for (int i = 0; i < Resources.length(); i++) {
                    JSONObject resource = Resources.getJSONObject(i);
                    if (resource != null) {
                        String entite = resource.getString("@surfaceForm");
                        ArmazenaEntite(entite, nome_dataset, conn);
                    }
                }

            } catch (JSONException e) {
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
       

    }
    
}
