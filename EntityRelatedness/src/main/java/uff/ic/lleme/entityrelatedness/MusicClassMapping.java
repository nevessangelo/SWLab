package uff.ic.lleme.entityrelatedness;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MusicClassMapping extends ArrayList<Pair> {

    public MusicClassMapping() {
        String mso = "http://purl.org/ontology/mo/";
        String dbo = "http://dbpedia.org/ontology/";
        try (InputStream in = new FileInputStream("./data/EntityRelatednessTestDataset/music_class_mapping.txt");) {
            Scanner sc = new Scanner(in);
            int count = 0;
            while (sc.hasNext()) {
                String linha = sc.nextLine();
                linha = linha.replaceAll("  ", " ").replaceAll(" ", "\t").replaceAll("\t\t", "\t");
                count++;
                if (count > 1 && linha != null && !linha.equals("")) {
                    String[] cols = linha.split("\t");
                    add(new Pair(null, null, cols[0].trim().replace("mso:", mso).replace("dbo:", dbo), cols[1].trim().replace("mso:", mso).replace("dbo:", dbo)));
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MusicClassMapping.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MusicClassMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
