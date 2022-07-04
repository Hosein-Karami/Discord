package com.example.discordfx.Lateral;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class FileCopier implements Runnable{

    private final File originalFile;
    private final FileOutputStream fileOutputStream;

    public FileCopier(File originalFile,FileOutputStream fileOutputStream){
        this.originalFile = originalFile;
        this.fileOutputStream = fileOutputStream;
    }

    @Override
    public void run() {
        try {
            byte[] fileBytes = Files.readAllBytes(originalFile.toPath());
            fileOutputStream.write(fileBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
