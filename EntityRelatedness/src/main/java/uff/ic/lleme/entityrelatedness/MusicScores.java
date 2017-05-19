package uff.ic.lleme.entityrelatedness;

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

public class MusicScores extends HashMap<String, ArrayList<Score>> {

    public MusicScores() {
        File dir = new File("./data/EntityRelatednessTestDataset/music_scores");
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
                    if (count > 1 && linha != null && !linha.equals(""))
                        try {
                            String[] cols = linha.split("\t");
                            cols[0] = cols[0].trim();
                            cols[1] = cols[1].trim();
                            ArrayList<Score> lista = get(name);
                            if (lista == null) {
                                lista = new ArrayList<>();
                                put(name, lista);
                            }
                            lista.add(new Score(cols[0], null, Double.valueOf(cols[1])));
                        } catch (NumberFormatException e) {
                            System.out.println(e.toString());
                            System.out.println(String.format("Erro: class -> %1s, arq -> %1s, line -> %1s.", "MusicEntityMappings", f.getName(), linha));
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
}
