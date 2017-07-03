package uff.ic.swlab.dataset.ertd.v3.util;

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
import uff.ic.swlab.dataset.ertd.v3.Config;

public class MusicEntityMappings extends HashMap<String, ArrayList<Pair>> {

    public MusicEntityMappings() {
        String linha, name;
        File dir = new File(Config.DATA_ROOT + "/music_entity_mappings");
        for (File f : dir.listFiles()) {
            name = f.getName().trim().replaceAll(".txt$", "").replaceAll("^\\d*\\.", "");
            ArrayList<Pair> lista = get(name);
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
                    linha = linha.replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", "\t").replaceAll("\t\t", "\t");;
                    count++;
                    if (count > 1 && linha != null && !linha.equals("")) {
                        String[] cols = linha.split("\t");
                        if (cols.length == 4) {
                            cols[0] = cols[0].trim();
                            cols[1] = cols[1].trim();
                            cols[2] = cols[2].trim().replaceFirst("^ttp://", "http://");
                            cols[3] = cols[3].trim().replaceFirst("^ttp://", "http://");
                            lista.add(new Pair(cols[0], cols[1], cols[2], cols[3]));
                        } else if (cols.length == 3 && cols[0].equals("Arthur_Fogel")) {
                            cols[0] = cols[0].trim();
                            cols[1] = cols[1].trim();
                            cols[2] = cols[2].trim().replaceFirst("^ttp://", "http://");
                            lista.add(new Pair(cols[0], cols[1], cols[2], null));
                        } else
                            System.out.println(String.format("Error: class -> %1s, file -> %1s, line -> %1s.", "MusicEntityMappings", f.getName(), linha));
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
