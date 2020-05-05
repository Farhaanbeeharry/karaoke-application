import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;

public class AddSong {

    static boolean proceed = false;
    static Text duration;
    static Stage addStage;
    static TextField songNameField, artistNameField;
    static FileChooser fileChooser;
    static File selectedFile;
    static Media media;
    static MediaView mediaView;
    static Button addBtn;
    static MediaPlayer mediaPlayer;
    static VBox fullBox;

    public static void addToLibrary(HashST<String, Song> songs) {

        GridPane mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setVgap(20);

        Label title = new Label("Add new song to library");
        title.setMinWidth(300);
        title.setAlignment(Pos.CENTER);

        Text songNameText = new Text();
        songNameText.minWidth(300);
        songNameText.setText("Song name :");

        songNameField = new TextField();
        songNameField.setPromptText("Song name here ...");
        songNameField.setFocusTraversable(false);
        songNameField.setMinHeight(40);
        songNameField.setMinWidth(300);

        Text artistNameText = new Text();
        artistNameText.minWidth(300);
        artistNameText.setText("Artist name :");

        artistNameField = new TextField();
        artistNameField.setPromptText("Artist name here ...");
        artistNameField.setFocusTraversable(false);
        artistNameField.setMinHeight(40);
        artistNameField.setMinWidth(300);

        fileChooser = new FileChooser();

        Text pathName = new Text();
        pathName.minWidth(620);
        pathName.setText("No file chosen yet!");

        Text durationText = new Text();
        durationText.setText("Duration (in seconds) : ");

        duration = new Text();
        duration.setText("No file chosen yet!");


        Button chooseFileBtn = new Button("Select video file ...");
        chooseFileBtn.setMinHeight(40);
        chooseFileBtn.setMinWidth(400);
        chooseFileBtn.setAlignment(Pos.CENTER);
        chooseFileBtn.setFocusTraversable(false);
        chooseFileBtn.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(addStage);
            if (selectedFile != null) {
                pathName.setText(selectedFile.getPath());
                chooseFileBtn.setText(selectedFile.getName());
                int valid = checkFormat(selectedFile.getName());
                if (valid == 1) {
                    proceed = true;
                    getDuration(selectedFile.getPath());
                } else if (valid == 2) {
                    duration.setText("Invalid format! Choose another file!");
                } else if (valid == 3) {
                    duration.setText("File already exists!");
                }
            }
        });


        Button cancelBtn = new Button("Cancel");
        cancelBtn.setAlignment(Pos.CENTER);
        cancelBtn.setMinHeight(40);
        cancelBtn.setMinWidth(400);
        cancelBtn.setFocusTraversable(false);
        cancelBtn.setOnAction(e -> {
            addStage.close();
        });


        addBtn = new Button("Add");
        addBtn.setAlignment(Pos.CENTER);
        addBtn.setMinHeight(40);
        addBtn.setMinWidth(400);
        addBtn.setFocusTraversable(false);
        addBtn.setOnAction(e -> {
            if (!songNameField.getText().matches("") && !artistNameField.getText().matches("") && proceed) {
                double videoDuration = Double.parseDouble(duration.getText());
                Song newSong = new Song(songNameField.getText(), artistNameField.getText(), videoDuration, selectedFile.getName());
                fullBox.getChildren().remove(cancelBtn);
                addBtn.setText("Please while ... video file uploading !");
                copyFile(selectedFile.getPath(), selectedFile.getName());
                exportData.writeSong(songs, newSong);
                addStage.close();
                DialogBox.box("Song successfully added to library !");

            } else if (!proceed) {
                DialogBox.box("Please check file upload !");
            } else {
                DialogBox.box("Please fill all fields !");
            }
        });

        HBox songNameBox = new HBox(20);
        songNameBox.setAlignment(Pos.CENTER);
        songNameBox.getChildren().addAll(songNameText, songNameField);

        HBox artistNameBox = new HBox(20);
        artistNameBox.setAlignment(Pos.CENTER);
        artistNameBox.getChildren().addAll(artistNameText, artistNameField);

        HBox durationBox = new HBox(20);
        durationBox.setAlignment(Pos.CENTER);
        durationBox.getChildren().addAll(durationText, duration);

        fullBox = new VBox(20);
        fullBox.setAlignment(Pos.CENTER);
        fullBox.getChildren().addAll(songNameBox, artistNameBox, chooseFileBtn, pathName, durationBox, addBtn, cancelBtn);

        mainGrid.add(fullBox, 0, 0);


        Scene scene = new Scene(mainGrid);
if (importData.importConfig()[1].equalsIgnoreCase("dark")) {
            scene.getStylesheets().add("file:stylesheet/style.css");
        }


        addStage = new Stage();
        addStage.setTitle("Add new song to library");
        addStage.setScene(scene);
        addStage.setResizable(false);
        addStage.setMaximized(true);
        addStage.setOnCloseRequest(e -> {
            e.consume();
            addStage.close();
        });
        addStage.show();

    }

    public static void getDuration(String filePath) {

        selectedFile = new File(filePath);
        media = new Media(selectedFile.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);
        mediaPlayer.play();
        mediaPlayer.setMute(true);

        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                duration.setText(Double.toString(mediaPlayer.getMedia().getDuration().toSeconds()));
                mediaPlayer.stop();
            }
        });

    }

    public static int checkFormat(String fileName) {

        String[] fileNameBits = fileName.split("\\.");

        int amountBits = fileNameBits.length;

        if (!fileNameBits[amountBits - 1].equalsIgnoreCase("mp4")) {
            return 2;
        }

        if (fileExists(fileName) == true) {
            return 3;
        }

        return 1;
    }

    public static void copyFile(String sourcePath, String sourceName) {

        File originalFile = new File(sourcePath);
        File newFile = new File("videos/" + sourceName);

        try {
            Files.copy(originalFile.toPath(), newFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static boolean fileExists(String fileName) {
        File tempFile = new File("videos/" + fileName);
        boolean exists = tempFile.exists();
        return exists;
    }

}
