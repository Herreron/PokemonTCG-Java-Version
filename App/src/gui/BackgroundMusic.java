package gui;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class BackgroundMusic {
    private static final String INTRO_TRACK = "/media/audio/intros/Music_Intro_1.wav";
    private static final String BATTLE_TRACK = "/media/audio/battleSongs/SoundtrackCombatGen1.wav";

    private static Clip currentClip;
    private static String currentTrack;

    private BackgroundMusic() {
    }

    public static synchronized void playIntro() {
        playLoop(INTRO_TRACK);
    }

    public static synchronized void playBattle() {
        playLoop(BATTLE_TRACK);
    }

    public static synchronized void stop() {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
            currentClip = null;
            currentTrack = null;
        }
    }

    private static void playLoop(String trackPath) {
        if (trackPath.equals(currentTrack) && currentClip != null && currentClip.isRunning()) {
            return;
        }

        stop();
        URL audioResource = BackgroundMusic.class.getResource(trackPath);
        if (audioResource == null) {
            System.err.println("Background music resource not found: " + trackPath);
            return;
        }

        Clip clip = null;
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioResource)) {
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            currentClip = clip;
            currentTrack = trackPath;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | IllegalArgumentException exception) {
            if (clip != null) {
                clip.close();
            }
            System.err.println("Unable to play background music " + trackPath + ": " + exception.getMessage());
        }
    }
}
