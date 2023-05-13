package view;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class BGM implements Runnable{
    private String MusicPath;
    private boolean isStop=false;
    private Clip runningBGM;
    public BGM(String MusicPath)
    {
        this.MusicPath=MusicPath;
    }
    public void setBGMPath(String musicPath)
    {
        this.MusicPath=musicPath;
    }
    public void stopBGM()
    {
        isStop=true;
    }
    public void startBGM()
    {
        isStop=false;
    }
    public  void run()
    {
        try{
            runningBGM = AudioSystem.getClip();
            InputStream is = BGM.class.getClassLoader().getResourceAsStream(this.MusicPath);
            AudioInputStream ais = AudioSystem.getAudioInputStream(is);
            runningBGM.open(ais);
            runningBGM.start();
            runningBGM.loop(Clip.LOOP_CONTINUOUSLY);
            while(isStop)
            {
                runningBGM.stop();
                Thread.sleep(1000);

            }


        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

