package uff.ic.lleme.entityrelatednesstestdata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MusicEntityPairs extends ArrayList<Pair> {

    public MusicEntityPairs() {
        try (InputStream in = new FileInputStream(MyConfig.DATA_ROOT + "/music_entity_pairs.txt");) {
            Scanner sc = new Scanner(in);
            int count = 0;
            while (sc.hasNext()) {
                String linha = sc.nextLine();
                count++;
                if (count > 1 && linha != null && !linha.equals("")) {
                    String[] cols = linha.split("\t");
                    add(new Pair(cols[0], null, cols[1], cols[2]));
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MusicEntityPairs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MusicEntityPairs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
