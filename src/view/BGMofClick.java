package view;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class BGMofClick {
    public void PlayClickBGM(String BGMofClickPath){
        try{
            File BGMofClick = new File(BGMofClickPath);

            if(BGMofClick.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(BGMofClick);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(0);
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
