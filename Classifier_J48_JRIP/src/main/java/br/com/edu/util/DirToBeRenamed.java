package br.com.edu.util;

import java.io.File;

public class DirToBeRenamed extends File {

    public DirToBeRenamed(String name) {
        super(name);
    }

    @Override
    public DirToBeRenamed[] listFiles() {
        DirToBeRenamed[] files2 = null;
        if (isDirectory()) {
            File[] files = super.listFiles();
            int i = 0;
            files2 = new DirToBeRenamed[files.length];
            for (File f : files)
                files2[i++] = new DirToBeRenamed(f.getPath());
        }
        return files2;
    }

    public void rename() {
        rename(this);
    }

    private void rename(DirToBeRenamed dir) {
        if (dir.isDirectory())
            for (DirToBeRenamed f : dir.listFiles())
                if (f.isFile()) {
                    String newName = f.getParent() + "/" + f.getName().replaceAll(":", "-");
                    f.renameTo(new File(newName));
                } else if (f.isDirectory())
                    rename(f);

    }

}
