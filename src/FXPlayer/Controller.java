package FXPlayer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    private Label songTicker;

    @FXML
    void changeSong(){
        songTicker.setText("FUCK");
    }
}
