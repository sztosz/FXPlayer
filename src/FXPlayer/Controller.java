package FXPlayer;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.text.SimpleDateFormat;

public class Controller {

    final FileChooser fileChooser = new FileChooser();
    private Stage stage;
    private Media media;
    private MediaPlayer mediaPlayer;
    private Duration endTime;
    private Duration currentTime;

    @FXML private Label songTicker;
    @FXML private Label artistTicker;
    @FXML private Label albumTicker;
    @FXML private Label statusBar;
    @FXML private Label songEndTime;
    @FXML private Label songCurrentTime;
    @FXML private Slider volumeSlider;
    @FXML private Slider songSeeker;


    public void changeSong() {
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
                mediaPlayer.setOnReady(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayer.play();
                        mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
                        endTime = mediaPlayer.getTotalDuration();
                        songEndTime.setText(new SimpleDateFormat("mm:ss").format(endTime.toMillis())); // Possible Memory leak
                    }
                });
                updateStatusBar("Now Playing");
                if (this.mediaPlayer != null) {
                    this.mediaPlayer.stop();
                }
                this.mediaPlayer = mediaPlayer;
                this.media = media;

                changeMetadataOnLabels();
                songSeeker.valueChangingProperty().addListener(new songSeekerChangeListener());
                mediaPlayer.currentTimeProperty().addListener(new CurrentTimeListener());
            }
        } catch (RuntimeException re) {
            updateStatusBar("Can't play this file");
        }
    }

    public class CurrentTimeListener implements InvalidationListener {
        @Override
        public void invalidated(Observable observable) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    currentTime = mediaPlayer.getCurrentTime();
                    songCurrentTime.setText(new SimpleDateFormat("mm:ss").format(currentTime.toMillis()));
                    updateSongSeeker();
                }
            });

        }
    }

    public class songSeekerChangeListener implements ChangeListener<Boolean> {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldVal, Boolean newVal) {
            if (oldVal && !newVal) {
                mediaPlayer.seek(endTime.multiply(songSeeker.getValue()));
            }
        }


    }

    public void updateSongSeeker() {
        if (currentTime == null || endTime == null) {
            songSeeker.setValue(0);
        } else {
            songSeeker.setValue(currentTime.toMillis() / endTime.toMillis());
        }
    }

    public void changeMetadataOnLabels() {
        media.getMetadata().addListener(new MapChangeListener<String, Object>() {
            @Override
            public void onChanged(Change<? extends String, ?> change) {
                if (change.wasAdded()) {
                    if (change.getKey().equals("title")) {
                        songTicker.setText(change.getValueAdded().toString());
                    }
                    if (change.getKey().equals("artist")) {
                        artistTicker.setText(change.getValueAdded().toString());
                    }
                    if (change.getKey().equals("album")) {
                        albumTicker.setText(change.getValueAdded().toString());
                    }
                }
            }
        });
    }


    private void updateStatusBar(String text) {
        statusBar.setText(text);
    }


    public void playClicked() {
        if (this.mediaPlayer != null) {
            mediaPlayer.play();
            updateStatusBar("Playback Started"); // TODO: Check if was paused and say it was resumed.
        }
    }

    public void pauseClicked() {
        if (this.mediaPlayer != null) {
            mediaPlayer.pause();
            updateStatusBar("Paused");
        }
    }

    public void stopClicked() {
        if (this.mediaPlayer != null) {
            mediaPlayer.stop();
            updateStatusBar("Stopped");
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
