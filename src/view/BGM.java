package view;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class BGM implements Runnable{
    private String MusicPath;
    private boolean isStop=false;
    private InputStream is;
    private Clip runningBGM;
    private AudioInputStream ais;
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
            is = BGM.class.getClassLoader().getResourceAsStream(this.MusicPath);
            ais = AudioSystem.getAudioInputStream(is);
            runningBGM.open(ais);
            runningBGM.start();
            runningBGM.loop(Clip.LOOP_CONTINUOUSLY);

            while(true){
                if(!isStop)
                {
                    runningBGM.start();
                    Thread.sleep(1000);
                }
                else
                {
                    runningBGM.stop();
                    Thread.sleep(1000);
                }
            }

//            while(isStop)
//            {
//                runningBGM.stop();
//                Thread.sleep(1000);
//
//            }


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

    public void setIs(InputStream is) {
        this.is = is;
    }
    public void setRunningBGM(Clip runningBGM) {
        this.runningBGM = runningBGM;
    }
    public void setAis(AudioInputStream ais) {
        this.ais = ais;
    }

    public String getMusicPath() {
        return MusicPath;
    }

    public boolean isStop() {
        return isStop;
    }

    public InputStream getIs() {
        return is;
    }

    public Clip getRunningBGM() {
        return runningBGM;
    }

    public AudioInputStream getAis() {
        return ais;
    }
}

