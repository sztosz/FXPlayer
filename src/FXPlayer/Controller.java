package FXPlayer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {

    final FileChooser fileChooser = new FileChooser();
    private Stage stage;

    @FXML
    private Label songTicker;

    @FXML
    void changeSong(){
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            songTicker.setText(file.toURI().toString());
        }
    }

    public void playClicked() {
        songTicker.setText("PLAY");
    }

    public void pauseClicked() {
        songTicker.setText("PAUSE");
    }

    public void stopClicked() {
        songTicker.setText("STOP");
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }
}
