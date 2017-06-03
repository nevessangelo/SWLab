package uff.ic.swlab.dataset.entityrelatednesstestdata.v3.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import uff.ic.swlab.dataset.entityrelatednesstestdata.v3.Config;

public class MovieClassMapping extends ArrayList<Pair> {

    public MovieClassMapping() {
        String mvo = "http://www.movieontology.org/2010/01/movieontology.owl#";
        String dbo = "http://dbpedia.org/ontology/";
        File f = new File(Config.DATA_ROOT + "/movie_class_mapping.txt");
        try (InputStream in = new FileInputStream(f);) {
            Scanner sc = new Scanner(in);
            int count = 0;
            String linha;
            while (sc.hasNext()) {
                linha = sc.nextLine();
                linha = linha.replace('\u00A0', '\0').replace('\u00C2', '\0');
                linha = linha.replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "\t").replaceAll("\t\t", "\t");
                count++;
                if (count > 1 && linha != null && !linha.equals("")) {
                    String[] cols = linha.split("\t");
                    if (cols.length == 3) {
                        cols[0] = cols[0].trim().replace("mvo:Director", "dbo:Film_Director").replace("mvo:", mvo).replace("dbo:", dbo);
                        cols[1] = cols[1].trim().replaceFirst("^dpo:", "dbo:").replace("mvo:", mvo).replace("dbo:", dbo);
                        cols[2] = cols[2].trim();
                        add(new Pair(cols[2], null, cols[0], cols[1]));
                    } else
                        System.out.println(String.format("Error: class -> %1s, line -> %1s.", "MovieClassMapping", linha));
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MovieClassMapping.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MovieClassMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
