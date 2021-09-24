package Sound;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    private Clip clip;
    private final File soundFile;
    public Sound(File file) {
        soundFile = file;
        soundCharge();
    }

    private void soundCharge() {
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(soundFile));
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void loop(){
        soundCharge();
        clip.loop(clip.LOOP_CONTINUOUSLY);
    }

    public void play() {
        soundCharge();
        clip.setFramePosition(0);
        clip.start();
    }

    public void stop(){
        clip.stop();
    }

    public Boolean isPlaying(){
        return clip.isRunning();
    }

}
