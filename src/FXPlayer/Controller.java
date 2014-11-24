package FXPlayer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {

    final FileChooser fileChooser = new FileChooser();
    private Stage stage;
    private Media media;
    private MediaPlayer mediaPlayer;

    @FXML
    private Label songTicker;

    @FXML
    void changeSong() {
        try {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                final Media media = new Media(file.toURI().toString());
                final MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnError(new Runnable() {
                    @Override
                    public void run() {
                        songTicker.setText(media.getError().getMessage());
                    }
                });
                songTicker.setText("Now Playing"); // TO DO get song name
                if (this.mediaPlayer != null) {
                    this.mediaPlayer.stop();
                }
                mediaPlayer.play();
                this.mediaPlayer = mediaPlayer;
                this.media = media;
            }
        } catch (RuntimeException re) {
            songTicker.setText("Can't play this file");
        }
    }

    public void playClicked() {
        mediaPlayer.play();
        songTicker.setText("PLAY");
    }

    public void pauseClicked() {
        mediaPlayer.pause();
        songTicker.setText("PAUSE");
    }

    public void stopClicked() {
        mediaPlayer.stop();
        songTicker.setText("STOP");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
