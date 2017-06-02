package uff.ic.lleme.entityrelatednesstestdata.v3.util;

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
import uff.ic.lleme.entityrelatednesstestdata.v3.Config;

public class MovieEntityMappings extends HashMap<String, ArrayList<Pair>> {

    public MovieEntityMappings() {
        String linha;
        File dir = new File(Config.DATA_ROOT + "/movie_entity_mappings");
        File[] files = dir.listFiles();
        for (File f : files) {
            String name = f.getName().trim().replaceAll(".txt$", "").replaceAll("^\\d*\\.", "");
            try (InputStream in = new FileInputStream(f);) {
                Scanner sc = new Scanner(in);
                int count = 0;
                while (sc.hasNext()) {
                    linha = sc.nextLine();
                    linha = linha.replaceAll("\u00a0", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "\t").replaceAll("\t\t", "\t");
                    count++;
                    if (count > 1 && linha != null && !linha.equals("")) {
                        String[] cols = linha.split("\t");
                        try {
                            cols[0] = cols[0].trim();
                            cols[1] = cols[1].trim();
                            cols[2] = cols[2].trim().replaceFirst("^ttp://", "http://");
                            cols[3] = cols[3].trim().replaceFirst("^ttp://", "http://");
                            if (cols.length != 4)
                                throw new Exception("Invalid columns.");
                        } catch (Exception e) {
                            System.out.println(e.toString());
                            System.out.println(String.format("Error: class -> %1s, file -> %1s, line -> %1s.", "MovieEntityMappings", f.getName(), linha));
                            continue;
                        }
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
