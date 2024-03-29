/**
 * @author Hosein Karami
 * @since 7/11/22
 * @version 1.0
 */

package com.example.discordfx.Client.RoomHandler.VoiceManagement;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;

public class Record implements Runnable
{
    private final File file;
    private final AudioInputStream audioInputStream;


    public Record(AudioInputStream audioInputStream, File file)
    {
        this.file = file;
        this.audioInputStream = audioInputStream;
    }

    /**
     * Is used to run thread and record voice
     */
    @Override
    public void run()
    {
        try
        {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, file);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}