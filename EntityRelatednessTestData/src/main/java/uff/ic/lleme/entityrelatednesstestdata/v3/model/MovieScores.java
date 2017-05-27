package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import uff.ic.lleme.entityrelatednesstestdata.v3.Config;

public class MovieScores extends HashMap<String, ArrayList<Score>> {

    public MovieScores() {
        File dir = new File(Config.DATA_ROOT + "/movie_scores");
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
                        ArrayList<Score> lista = get(name);
                        if (lista == null) {
                            lista = new ArrayList<>();
                            put(name, lista);
                        }
                        lista.add(new Score(cols[0], null, Double.valueOf(cols[1])));
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MovieScores.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MovieScores.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public double getScore(String label) {
        for (List<Score> entities : values())
            for (Score entity : entities)
                if (entity.label.equals(label))
                    return entity.score;
        return 0;
    }
}
