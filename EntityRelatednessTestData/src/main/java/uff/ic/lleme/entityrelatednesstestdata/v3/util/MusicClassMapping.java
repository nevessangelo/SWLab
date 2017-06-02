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

public class MusicClassMapping extends ArrayList<Pair> {

    public MusicClassMapping() {
        String linha;
        String mso = "http://purl.org/ontology/mo/";
        String dbo = "http://dbpedia.org/ontology/";
        try (InputStream in = new FileInputStream(Config.DATA_ROOT + "/music_class_mapping.txt");) {
            Scanner sc = new Scanner(in);
            int count = 0;
            while (sc.hasNext()) {
                linha = sc.nextLine();
                linha = linha.replaceAll("\u00a0", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "\t").replaceAll("\t\t", "\t");
                count++;
                if (count > 1 && linha != null && !linha.equals("")) {
                    String[] cols = linha.split("\t");
                    cols[0] = cols[0].trim().replace("mso:", mso).replace("dbo:", dbo);
                    cols[1] = cols[1].trim().replace("mso:", mso).replace("dbo:", dbo);
                    cols[2] = cols[2].trim();
                    add(new Pair(cols[2], null, cols[0], cols[1]));
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MusicClassMapping.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MusicClassMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
