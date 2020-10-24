package seedu.address.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

/**
 * Stores media and plays them when called.
 */
public class JinglePlayer {
    private MediaPlayer mediaPlayer;

    public JinglePlayer(String filename) {
        Media media = new Media(getClass().getResource(filename).toExternalForm());
        mediaPlayer = new MediaPlayer(media);
    }

    public void play() {
        mediaPlayer.play();
    }
}
