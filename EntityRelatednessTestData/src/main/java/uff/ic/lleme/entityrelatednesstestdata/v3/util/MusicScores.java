package uff.ic.lleme.entityrelatednesstestdata.v3.util;

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

public class MusicScores extends HashMap<String, ArrayList<Score>> {

    public MusicScores() {
        String linha;
        File dir = new File(Config.DATA_ROOT + "/music_scores");
        File[] files = dir.listFiles();
        for (File f : files) {
            //String[] names = f.getName().split("\\.");
            //String name = (String.join("", Arrays.copyOfRange(names, 1, names.length - 1)));
            String name = f.getName().trim().replaceAll(".txt$", "").replaceAll("^\\d*\\.", "");
            try (InputStream in = new FileInputStream(f);) {
                Scanner sc = new Scanner(in);
                int count = 0;
                ArrayList<Score> lista = get(name);
                if (lista == null) {
                    lista = new ArrayList<>();
                    put(name, lista);
                }
                while (sc.hasNext()) {
                    linha = sc.nextLine();
                    linha = linha.replaceAll("  ", " ").replaceAll(" ", "\t").replaceAll("\t\t", "\t");
                    count++;
                    if (count > 1 && linha != null && !linha.equals(""))
                        try {
                            String[] cols = linha.split("\t");
                            cols[0] = cols[0].trim();
                            cols[1] = cols[1].trim();
                            lista.add(new Score(cols[0], null, Double.valueOf(cols[1])));
                        } catch (NumberFormatException e) {
                            System.out.println(e.toString());
                            System.out.println(String.format("Erro: class -> %1s, file -> %1s, line -> %1s.", "MusicEntityMappings", f.getName(), linha));
                            continue;
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
