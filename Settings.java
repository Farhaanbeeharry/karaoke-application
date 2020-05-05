import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Settings {

    public static void setting() {

        Stage settingsStage = new Stage();

        Text layoutText = new Text();
        layoutText.setText("Playlist Pane :");

        ToggleButton leftPlaylistButton = new ToggleButton("Left");
        leftPlaylistButton.setMinWidth(100);
        leftPlaylistButton.setMinHeight(30);
        leftPlaylistButton.setFocusTraversable(false);

        ToggleButton rightPlaylistButton = new ToggleButton("Right");
        rightPlaylistButton.setMinWidth(100);
        rightPlaylistButton.setMinHeight(30);
        rightPlaylistButton.setFocusTraversable(false);

        ToggleGroup layoutGroup = new ToggleGroup();
        leftPlaylistButton.setToggleGroup(layoutGroup);
        rightPlaylistButton.setToggleGroup(layoutGroup);
        if (importData.importConfig().equalsIgnoreCase("left")) {
            layoutGroup.selectToggle(leftPlaylistButton);
        } else if (importData.importConfig().equalsIgnoreCase("Right")) {
            layoutGroup.selectToggle(rightPlaylistButton);
        }
        layoutGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
            }
        });

        Button saveBtn = new Button("Save");
        saveBtn.setMinHeight(40);
        saveBtn.setMinWidth(300);
        saveBtn.setFocusTraversable(false);
        saveBtn.setOnAction(e -> {
            String data = "";
            if (leftPlaylistButton.isSelected()) {
                data = "left";
            } else if (rightPlaylistButton.isSelected()) {
                data = "right";
            }
            exportData.exportConfig(data);
            settingsStage.close();
            DialogBox.box("Settings saved !");
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setMinHeight(40);
        cancelBtn.setMinWidth(300);
        cancelBtn.setFocusTraversable(false);
        cancelBtn.setOnAction(e -> {
            settingsStage.close();
        });

        HBox layoutTextBox = new HBox(30);
        layoutTextBox.setAlignment(Pos.CENTER_LEFT);
        layoutTextBox.getChildren().add(layoutText);

        HBox buttonLayoutBox = new HBox(30);
        buttonLayoutBox.setAlignment(Pos.CENTER);
        buttonLayoutBox.getChildren().addAll(leftPlaylistButton, rightPlaylistButton);

        HBox layoutBox = new HBox(30);
        layoutBox.setAlignment(Pos.CENTER);
        layoutBox.setPadding(new Insets(0, 0, 50, 0));
        layoutBox.getChildren().addAll(layoutTextBox, buttonLayoutBox);

        VBox settingsBox = new VBox(30);
        settingsBox.setAlignment(Pos.CENTER);
        settingsBox.getChildren().addAll(layoutBox, saveBtn, cancelBtn);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(settingsBox, 0, 0);

        Scene scene = new Scene(gridPane);

        settingsStage.setScene(scene);
        settingsStage.setTitle("Settings");
        settingsStage.setMaximized(true);
        settingsStage.setResizable(false);
        settingsStage.show();

    }
}

