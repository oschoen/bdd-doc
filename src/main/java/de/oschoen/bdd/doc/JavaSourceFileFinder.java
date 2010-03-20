package de.oschoen.bdd.doc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class JavaSourceFileFinder {

    private final File root;

    public JavaSourceFileFinder(File root) {
        assert root.isDirectory();
        
        this.root = root;
    }

    public Collection<File> findJavaSourceFiles() {
        Collection<File> javaSourceFiles = new ArrayList<File>();
        walk(root, javaSourceFiles);
        return javaSourceFiles;
    }


    private void walk(File directory, Collection<File> javaSourceFiles) {
        assert directory.isDirectory();

        File[] filesInDir = directory.listFiles();

        for (File file : filesInDir) {
            if (file.isDirectory() && !file.getName().startsWith(".")) {
                walk(file, javaSourceFiles);
            } else if (file.getName().endsWith("Test.java")) {
                javaSourceFiles.add(file);
            }
        }
    }

}
