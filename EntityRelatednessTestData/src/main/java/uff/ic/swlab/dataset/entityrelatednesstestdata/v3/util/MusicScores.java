package uff.ic.swlab.dataset.entityrelatednesstestdata.v3.util;

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
import uff.ic.swlab.dataset.entityrelatednesstestdata.v3.Config;

public class MusicScores extends HashMap<String, ArrayList<Score>> {

    public MusicScores() {
        String linha, name;
        File dir = new File(Config.DATA_ROOT + "/music_scores");
        for (File f : dir.listFiles()) {
            name = f.getName().trim().replaceAll(".txt$", "").replaceAll("^\\d*\\.", "");
            ArrayList<Score> lista = get(name);
            if (lista == null) {
                lista = new ArrayList<>();
                put(name, lista);
            }
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
                            try {
                                lista.add(new Score(cols[0], null, Double.valueOf(cols[1])));
                            } catch (Exception e) {
                                System.out.println(String.format("Erro: class -> %1s, file -> %1s, line -> %1s.", "MusicScores", f.getName(), linha));
                            }
                        } else
                            System.out.println(String.format("Erro: class -> %1s, file -> %1s, line -> %1s.", "MusicScores", f.getName(), linha));
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MusicScores.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MusicScores.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Double[] getScore(String label) {
        List<Double> scores = new ArrayList<>();
        for (List<Score> entities : values())
            for (Score entity : entities)
                if (entity.label.equals(label))
                    scores.add(entity.score);
        return scores.toArray(new Double[0]);
    }
}
