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

public class MusicEntityMappings extends HashMap<String, ArrayList<Pair>> {

    public MusicEntityMappings() {
        File dir = new File("./data/EntityRelatednessTestDataset/music_entity_mappings");
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
                        try {
                            cols[0] = cols[0].trim();
                            cols[1] = cols[1].trim();
                            cols[2] = cols[2].trim();
                            cols[3] = cols[3].trim();
                        } catch (Exception e) {
                            System.out.println(e.toString());
                            System.out.println(String.format("Erro: class -> %1s, arq -> %1s, line -> %1s.", "MusicEntityMappings", f.getName(), linha));
                            continue;
                        }
                        ArrayList<Pair> lista = get(name);
                        if (lista == null) {
                            lista = new ArrayList<>();
                            put(name, lista);
                        }
                        lista.add(new Pair(cols[0], cols[1], cols[2], cols[3]));
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MusicEntityMappings.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MusicEntityMappings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
