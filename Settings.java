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
        layoutText.setText("Playlist pane position :");

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
        if (importData.importConfig()[0].equalsIgnoreCase("left")) {
            layoutGroup.selectToggle(leftPlaylistButton);
        } else if (importData.importConfig()[0].equalsIgnoreCase("Right")) {
            layoutGroup.selectToggle(rightPlaylistButton);
        }
        layoutGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
            }
        });

        Text uiModeText = new Text();
        uiModeText.setText("UI Mode :");

        ToggleButton lightBtn = new ToggleButton("Light");
        lightBtn.setMinWidth(100);
        lightBtn.setMinHeight(30);
        lightBtn.setFocusTraversable(false);

        ToggleButton darkBtn = new ToggleButton("Dark");
        darkBtn.setMinWidth(100);
        darkBtn.setMinHeight(30);
        darkBtn.setFocusTraversable(false);

        ToggleGroup uiModeGroup = new ToggleGroup();
        lightBtn.setToggleGroup(uiModeGroup);
        darkBtn.setToggleGroup(uiModeGroup);
        if (importData.importConfig()[1].equalsIgnoreCase("light")) {
            uiModeGroup.selectToggle(lightBtn);
        } else if (importData.importConfig()[1].equalsIgnoreCase("dark")) {
            uiModeGroup.selectToggle(darkBtn);
        }
        uiModeGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
            }
        });

        Button saveBtn = new Button("Save");
        saveBtn.setMinHeight(40);
        saveBtn.setMinWidth(300);
        saveBtn.setFocusTraversable(false);
        saveBtn.setOnAction(e -> {
            String[] data = new String[2];
            if (leftPlaylistButton.isSelected()) {
                data[0] = "left";
            } else if (rightPlaylistButton.isSelected()) {
                data[0] = "right";
            }
            if (lightBtn.isSelected()) {
                data[1] = "light";
            } else if (darkBtn.isSelected()) {
                data[1] = "dark";
            }
            exportData.exportConfig(data);
            KaraokeApplication.startApplication();
            settingsStage.close();
            DialogBox.box("Settings saved !");
        });

        Button restoreDefaultBtn = new Button("Restore default and save");
        restoreDefaultBtn.setMinHeight(40);
        restoreDefaultBtn.setMinWidth(300);
        restoreDefaultBtn.setFocusTraversable(false);
        restoreDefaultBtn.setOnAction(e -> {
            String[] data = new String[2];
            data[0] = "left";
            data[1] = "light";
            exportData.exportConfig(data);
            KaraokeApplication.startApplication();
            settingsStage.close();
            DialogBox.box("Default settings restored !");
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setMinHeight(40);
        cancelBtn.setMinWidth(300);
        cancelBtn.setFocusTraversable(false);
        cancelBtn.setOnAction(e -> {
            KaraokeApplication.startApplication();
            settingsStage.close();
        });

        HBox layoutTextBox = new HBox(30);
        layoutTextBox.setAlignment(Pos.CENTER_LEFT);
        layoutTextBox.setMinWidth(200);
        layoutTextBox.getChildren().add(layoutText);

        HBox buttonLayoutBox = new HBox(30);
        buttonLayoutBox.setAlignment(Pos.CENTER);
        buttonLayoutBox.getChildren().addAll(leftPlaylistButton, rightPlaylistButton);

        HBox layoutBox = new HBox(30);
        layoutBox.setAlignment(Pos.CENTER);
        layoutBox.getChildren().addAll(layoutTextBox, buttonLayoutBox);

        HBox uiModeTextBox = new HBox(30);
        uiModeTextBox.setAlignment(Pos.CENTER_LEFT);
        uiModeTextBox.setMinWidth(200);
        uiModeTextBox.getChildren().add(uiModeText);

        HBox uiButtonBox = new HBox(30);
        uiButtonBox.setAlignment(Pos.CENTER);
        uiButtonBox.getChildren().addAll(lightBtn, darkBtn);

        HBox uiModeBox = new HBox(30);
        uiModeBox.setAlignment(Pos.CENTER);
        uiModeBox.setPadding(new Insets(0, 0, 50, 0));
        uiModeBox.getChildren().addAll(uiModeTextBox, uiButtonBox);

        VBox settingsBox = new VBox(30);
        settingsBox.setAlignment(Pos.CENTER);
        settingsBox.getChildren().addAll(layoutBox, uiModeBox, saveBtn, restoreDefaultBtn, cancelBtn);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(settingsBox, 0, 0);

        Scene scene = new Scene(gridPane);
        if (importData.importConfig()[1].equalsIgnoreCase("dark")) {
            scene.getStylesheets().add("file:stylesheet/style.css");
        }
        settingsStage.setScene(scene);
        settingsStage.setTitle("Settings");
        settingsStage.setMaximized(true);
        settingsStage.setResizable(false);
        settingsStage.show();

    }
}

