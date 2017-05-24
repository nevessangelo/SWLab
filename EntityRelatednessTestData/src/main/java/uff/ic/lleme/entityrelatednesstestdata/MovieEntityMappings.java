package uff.ic.lleme.entityrelatednesstestdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovieEntityMappings extends HashMap<String, ArrayList<Pair>> {

    public MovieEntityMappings() {
        File dir = new File(Config.DATA_ROOT + "/movie_entity_mappings");
        File[] files = dir.listFiles();
        for (File f : files) {
            String name = (f.getName().split("\\.")[1]);
            try (InputStream in = new FileInputStream(f);) {
                Scanner sc = new Scanner(in);
                int count = 0;
                while (sc.hasNext()) {
                    String linha = sc.nextLine();
                    linha = linha.replaceAll("  ", " ").replaceAll(" ", "\t").replaceAll("\t\t", "\t");
                    count++;
                    if (count > 1 && linha != null && !linha.equals("")) {
                        String[] cols = linha.split("\t");
                        cols[0] = cols[0].trim();
                        cols[1] = cols[1].trim();
                        cols[2] = cols[2].trim();
                        cols[3] = cols[3].trim();
                        ArrayList<Pair> lista = get(name);
                        if (lista == null) {
                            lista = new ArrayList<>();
                            put(name, lista);
                        }
                        lista.add(new Pair(cols[0], cols[1], cols[2], cols[3]));
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MovieEntityMappings.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MovieEntityMappings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
