package view;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class BGM implements Runnable{
    public  void run()
    {
        try{
            Clip bgm = AudioSystem.getClip();
            InputStream is = BGM.class.getClassLoader().getResourceAsStream("qakmp-mq85b.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(is);
            bgm.open(ais);
            bgm.start();
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
            while(1==1)
            {

            }

        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

