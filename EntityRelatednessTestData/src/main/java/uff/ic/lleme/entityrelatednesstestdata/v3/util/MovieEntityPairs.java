package uff.ic.lleme.entityrelatednesstestdata.v3.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import uff.ic.lleme.entityrelatednesstestdata.v3.Config;

public class MovieEntityPairs extends ArrayList<Pair> {

    public MovieEntityPairs() {
        String linha;
        try (InputStream in = new FileInputStream(Config.DATA_ROOT + "/movie_entity_pairs.txt");) {
            Scanner sc = new Scanner(in);
            int count = 0;
            while (sc.hasNext()) {
                linha = sc.nextLine();
                count++;
                if (count > 1 && linha != null && !linha.equals("")) {
                    String[] cols = linha.split("\t");
                    if (cols.length == 3)
                        add(new Pair(cols[0], null, cols[1], cols[2]));
                    else
                        System.out.println(String.format("Error: class -> %1s, line -> %1s.", "MovieEntityPairs", linha));
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MovieEntityPairs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MovieEntityPairs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
