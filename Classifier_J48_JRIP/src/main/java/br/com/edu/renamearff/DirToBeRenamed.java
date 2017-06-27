package br.com.edu.renamearff;

import java.io.File;

public class DirToBeRenamed extends File {

    public DirToBeRenamed(String name) {
        super(name);
    }

    @Override
    public DirToBeRenamed[] listFiles() {
        if (isDirectory()) {
            File[] files = super.listFiles();
            int i = 0;
            DirToBeRenamed[] files2 = new DirToBeRenamed[files.length];
            for (File f : files)
                files2[i++] = new DirToBeRenamed(f.getPath());
            return files2;
        } else
            return new DirToBeRenamed[0];
    }

    public void rename() {
        rename(this);
    }

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
    }

    private void rename(DirToBeRenamed dir) {
        for (DirToBeRenamed f : dir.listFiles())
            if (f.isFile()) {
                String oldName = f.getPath();
                String newName = f.getParent() + "/" + replaceLast(f.getName(), "-", "-id-");
                if (!newName.equals(oldName)) {
                    System.out.println("from: " + oldName + " to: " + newName);
                    f.renameTo(new File(newName));
                }
            } else if (f.isDirectory())
                rename(f);

    }

}
