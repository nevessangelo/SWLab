package uff.ic.lleme.entityrelatednesstestdata;

import uff.ic.lleme.entityrelatednesstestdata.util.MyConfig;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MusicPropertyRelevanceScore extends HashMap<String, Double> {

    public MusicPropertyRelevanceScore() {
        try (InputStream in = new FileInputStream(MyConfig.DATA_ROOT + "/music_property_relevance_scores/properties.txt");) {
            Scanner sc = new Scanner(in);
            int count = 0;
            while (sc.hasNext()) {
                String linha = sc.nextLine();
                linha = linha.replaceAll("  ", " ").replaceAll(" ", "\t").replaceAll("\t\t", "\t");
                count++;
                if (count > 1 && linha != null && !linha.equals("")) {
                    String[] cols = linha.split("\t");
                    cols[0] = cols[0].trim();
                    put(cols[0], Double.valueOf(cols[1]));
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MusicPropertyRelevanceScore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MusicPropertyRelevanceScore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
