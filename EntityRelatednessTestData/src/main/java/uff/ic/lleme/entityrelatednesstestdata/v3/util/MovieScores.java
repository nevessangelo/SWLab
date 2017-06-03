package uff.ic.lleme.entityrelatednesstestdata.v3.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import uff.ic.lleme.entityrelatednesstestdata.v3.Config;

public class MovieScores extends HashMap<String, ArrayList<Score>> {

    public MovieScores() {
        String linha, name;
        File dir = new File(Config.DATA_ROOT + "/movie_scores");
        for (File f : dir.listFiles()) {
            name = f.getName().trim().replaceAll(".txt$", "").replaceAll("^\\d*\\.", "");
            try (InputStream in = new FileInputStream(f);) {
                Scanner sc = new Scanner(in);
                int count = 0;
                while (sc.hasNext()) {
                    linha = sc.nextLine();
                    linha = linha.replace('\u00A0', '\0').replace('\u00C2', '\0');
                    linha = linha.replaceAll("  ", " ").replaceAll(" ", "\t").replaceAll("\t\t", "\t");
                    count++;
                    if (count > 1 && linha != null && !linha.equals("")) {
                        String[] cols = linha.split("\t");
                        if (cols.length == 2) {
                            cols[0] = cols[0].trim();
                            cols[1] = cols[1].trim();
                            ArrayList<Score> lista = get(name);
                            if (lista == null) {
                                lista = new ArrayList<>();
                                put(name, lista);
                            }
                            lista.add(new Score(cols[0], null, Double.valueOf(cols[1])));
                        } else
                            System.out.println(String.format("Error: class -> %1s, file -> %1s, line -> %1s.", "MovieScores", f.getName(), linha));
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MovieScores.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MovieScores.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Double[] getScore(String label) {
        Set<Double> scores = new HashSet<>();
        for (List<Score> entities : values())
            for (Score entity : entities)
                if (entity.label.equals(label))
                    scores.add(entity.score);
        return scores.toArray(new Double[0]);
    }
}
