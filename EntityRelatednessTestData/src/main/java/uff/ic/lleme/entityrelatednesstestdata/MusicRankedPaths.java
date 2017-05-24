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

public class MusicRankedPaths extends HashMap<String, ArrayList<Score>> {

    public MusicRankedPaths() {
        File dir = new File(MyConfig.DATA_ROOT + "/music_ranked_paths");
        File[] files = dir.listFiles();
        for (File f : files) {
            String name = (f.getName().split("\\.")[1]);
            try (InputStream in = new FileInputStream(f);) {
                Scanner sc = new Scanner(in);
                int count = 0;
                while (sc.hasNext()) {
                    String linha = sc.nextLine();
                    count++;
                    if (count > 1 && linha != null && !linha.equals("")) {
                        String[] cols = linha.split("\t");
                        cols[0] = cols[0].trim();
                        cols[1] = cols[1].trim().replace("\"", "");
                        cols[2] = cols[2].trim();
                        ArrayList<Score> lista = get(name);
                        if (lista == null) {
                            lista = new ArrayList<>();
                            put(name, lista);
                        }
                        lista.add(new Score(cols[0], cols[1], Double.valueOf(cols[2])));
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MusicRankedPaths.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MusicRankedPaths.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
