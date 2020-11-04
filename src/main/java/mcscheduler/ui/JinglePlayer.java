package mcscheduler.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Stores media and plays them when called.
 */
public class JinglePlayer {
    private final MediaPlayer mediaPlayer;

    /**
     * Creates a JinglePlayer from a media file.
     * @param filename the path of the media file from resources/
     */
    public JinglePlayer(String filename) {
        Media media = new Media(getClass().getResource(filename).toExternalForm());
        mediaPlayer = new MediaPlayer(media);
    }

    public void play() {
        mediaPlayer.play();
    }
}
