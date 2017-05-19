package uff.ic.lleme.entityrelatedness;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovieClassMapping extends ArrayList<Pair> {

    public MovieClassMapping() {
        String mvo = "http://www.movieontology.org/2010/01/movieontology.owl#";
        String dbo = "http://dbpedia.org/ontology/";
        try (InputStream in = new FileInputStream("./data/EntityRelatednessTestDataset/movie_class_mapping.txt");) {
            Scanner sc = new Scanner(in);
            int count = 0;
            while (sc.hasNext()) {
                String linha = sc.nextLine();
                linha = linha.replaceAll("  ", " ").replaceAll(" ", "\t").replaceAll("\t\t", "\t");
                count++;
                if (count > 1 && linha != null && !linha.equals("")) {
                    String[] cols = linha.split("\t");
                    cols[0] = cols[0].trim().replace("mvo:Director", "dbo:Film_Director").replace("mvo:", mvo).replace("dbo:", dbo);
                    cols[1] = cols[1].trim().replace("mvo:", mvo).replace("dbo:", dbo);
                    add(new Pair(null, null, cols[0], cols[1]));
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MovieClassMapping.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MovieClassMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
