/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Client.RoomHandler.VoiceManagement;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Recorder implements Runnable
{
    private final TargetDataLine targetDataLine;
    private final AudioFormat audioFormat;
    private final AudioInputStream audioInputStream;
    private final File file;

    public Recorder(File file)
    {
        this.file = file;
        try
        {
            audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            this.audioInputStream = new AudioInputStream(targetDataLine);
        }
        catch (LineUnavailableException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Is used to run thread and record voice from microphone
     */
    @Override
    public void run()
    {
        try {
            targetDataLine.open(audioFormat);
            targetDataLine.start();
        }
        catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        Thread thread = new Thread(new Record(audioInputStream, file));
        thread.start();
    }

    /**
     * Is used to stop recorder
     */
    public void stop(){
        try {
            audioInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        targetDataLine.stop();
        targetDataLine.flush();
        targetDataLine.close();
    }
}


