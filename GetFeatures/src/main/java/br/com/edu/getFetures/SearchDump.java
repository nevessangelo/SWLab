/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.getFetures;

import br.com.edu.Utils.Download;
import br.com.edu.objects.Resource;
import com.github.junrar.exception.RarException;
import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.model.CkanDataset;
import eu.trentorise.opendata.jackan.model.CkanResource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.compress.archivers.ArchiveException;


/**
 *
 * @author angelo
 */
public class SearchDump {

    public static List<String> Search(CkanClient cc, List datasets) throws IOException, FileNotFoundException, RarException, ArchiveException, Exception {
        List<String> Datasets_return = new ArrayList<>();
        ArrayList<Resource> Datasets_dump = new ArrayList();
        ArrayList<String> Datasets_difdump = new ArrayList();
        ArrayList<String> names_links = new ArrayList<>();
        List<CkanResource> resources = null;

        names_links.add("dump");
        names_links.add("tgz");
        names_links.add("gz");
        names_links.add("rar");
        names_links.add("zip");
        names_links.add("tar.gz");

        for (int i = 0; i < datasets.size(); i++) {
            boolean verifica_name = false;
            boolean verifica_url = false;
            boolean verifica_descricao = false;
            int flag = 0;
            
            CkanDataset d = cc.getDataset((String) datasets.get(i));
            resources = d.getResources();
            for (int j = 0; j < resources.size(); j++) {
                int exception_name = 0;
                int exception_description = 0;
                String name_resource = resources.get(j).getName();
                String name_description = resources.get(j).getDescription();
                String url = resources.get(j).getUrl();

                if ((name_resource != null && url != null) || (name_description != null) && (url != null)) {
                    if (name_resource != null && !name_resource.isEmpty()) {
                        String[] Vname_resource = name_resource.split(" ");

                        for (int k = 0; k < Vname_resource.length; k++) {
                            if (Vname_resource[k].toLowerCase().equals("page") || Vname_resource[k].toLowerCase().equals("example") || Vname_resource[k].toLowerCase().equals("individual") || Vname_resource[k].toLowerCase().equals("html") || Vname_resource[k].toLowerCase().equals("(html)") || Vname_resource[k].toLowerCase().equals("sparql") || Vname_resource[k].toLowerCase().equals("example")) {
                                exception_name++;
                            }
                        }

                        if (exception_name == 0) {

                            for (int k = 0; k < Vname_resource.length; k++) {
                                if (Vname_resource[k].toLowerCase().equals("dump") || Vname_resource[k].toLowerCase().equals("download") || Vname_resource[k].toLowerCase().equals("export") || Vname_resource[k].toLowerCase().equals("dataset") || Vname_resource[k].toLowerCase().equals("datasets")) {
                                    verifica_name = true;
                                }
                            }

                        }

                    } 
                    
                    if (name_description != null && !name_description.isEmpty()) {

                        String[] Vname_description = name_description.split(" ");

                        for (int k = 0; k < Vname_description.length; k++) {
                            if (Vname_description[k].toLowerCase().equals("page") || Vname_description[k].toLowerCase().equals("example") || Vname_description[k].toLowerCase().equals("individual") || Vname_description[k].toLowerCase().equals("html") || Vname_description[k].toLowerCase().equals("(html)") || Vname_description[k].toLowerCase().equals("sparql") || Vname_description[k].toLowerCase().equals("example")) {
                                exception_description++;
                            }
                        }

                        if (exception_description == 0) {

                            for (int k = 0; k < Vname_description.length; k++) {
                                if ((Vname_description[k].toLowerCase().equals("dump")  || Vname_description[k].toLowerCase().equals("download") || Vname_description[k].toLowerCase().equals("export") || Vname_description[k].toLowerCase().equals("dataset") || Vname_description[k].toLowerCase().equals("datasets"))) {
                                    verifica_descricao = true;
                                }

                            }

                        }

                    }

                    if (verifica_descricao || verifica_name) {
                        for (int k = 0; k < names_links.size(); k++) {
                            int verifica = url.toLowerCase().indexOf(names_links.get(k));
                            if (verifica > 0) {
                                verifica_url = true;
                            }
                        }

                    }

                    if ((verifica_name && verifica_url) || (verifica_descricao && verifica_url)) {
                        Resource obj_resource = new Resource();
                        obj_resource.setName((String) datasets.get(i));
                        obj_resource.setUrl(url);
                        Datasets_dump.add(obj_resource);
                        flag++;
                        break;
                    }

                }

            }

            if (flag == 0) {
                Datasets_difdump.add((String) datasets.get(i));
            }

        }

        int cont1 = 0, cont2 = 0;
        for (int i = 0; i < Datasets_dump.size(); i++) {
            System.out.println("Tem Dump:" + Datasets_dump.get(i).getName());
            System.out.println("URL: " + Datasets_dump.get(i).getUrl());
            cont1++;
        }

        for (int i = 0; i < Datasets_difdump.size(); i++) {
            System.out.println("Não tem Dump:" + Datasets_difdump.get(i));
            cont2++;
        }

        System.out.println("Numeros de dumps encontrados: " + cont1);
        System.out.println("Numeros de dumps NÃO encontrados: " + cont2);
        
        
       return Datasets_return = Download.DownloadDump(Datasets_dump, Datasets_difdump);
       
    }

}
