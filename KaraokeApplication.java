import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.io.FileInputStream;
import java.io.IOException;

public class KaraokeApplication extends Application {

    public static void main(String[] args) {

        if (args.length == 0) {
            exportData.writeLocation("data/songList.txt");
        } else if (args.length > 1) {
            System.out.println("Too many arguments!");
            System.exit(0);
        } else {
            exportData.writeLocation(args[0]);
            HashFB<String, Song> songs = importData.importSongList();

        }

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {

        startApplication();

    }

    public static void startApplication() {

        Stage primaryStage = new Stage();

        GridPane mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setVgap(20);

        Label title = new Label("Karaoke Application");
        title.setMinWidth(300);
        title.setAlignment(Pos.CENTER);

        try {
            Image image = new Image(new FileInputStream("images/logo.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);

            Button playPlaylist = new Button("Play my playlist");
            playPlaylist.setAlignment(Pos.CENTER);
            playPlaylist.setMinWidth(300);
            playPlaylist.setMinHeight(40);
            playPlaylist.setFocusTraversable(false);
            playPlaylist.setOnAction(e -> {
                HashFB<String, Song> songs = importData.importSongList();
                if (importData.getPlaylistCount() == 0) {
                    DialogBox.box("There are no songs in your playlist!");
                } else {
                    Player.playPlaylist(songs);
                }
            });

            Button addSongBtn = new Button("Add a song to the library");
            addSongBtn.setAlignment(Pos.CENTER);
            addSongBtn.setMinWidth(300);
            addSongBtn.setMinHeight(40);
            addSongBtn.setFocusTraversable(false);
            addSongBtn.setOnAction(e -> {
                HashFB<String, Song> songs = importData.importSongList();
                AddSong.addToLibrary(songs);
            });

            Button listAllBtn = new Button("All songs & My playlist");
            listAllBtn.setAlignment(Pos.CENTER);
            listAllBtn.setMinWidth(300);
            listAllBtn.setMinHeight(40);
            listAllBtn.setFocusTraversable(false);
            listAllBtn.setOnAction(e -> {
                HashFB<String, Song> songs = importData.importSongList();
                tableView.tableView(songs);
            });

            Button settingsBtn = new Button("Settings");
            settingsBtn.setAlignment(Pos.CENTER);
            settingsBtn.setMinWidth(300);
            settingsBtn.setMinHeight(40);
            settingsBtn.setFocusTraversable(false);
            settingsBtn.setOnAction(e -> {
                Settings.setting();
                primaryStage.close();
            });

            Button exitBtn = new Button("Exit");
            exitBtn.setAlignment(Pos.CENTER);
            exitBtn.setMinHeight(40);
            exitBtn.setMinWidth(300);
            exitBtn.setFocusTraversable(false);
            exitBtn.setOnAction(e -> {
                System.exit(0);
            });

            mainGrid.add(title, 0, 0);
            mainGrid.add(imageView, 0, 2);
            mainGrid.add(playPlaylist, 0, 4);
            mainGrid.add(addSongBtn, 0, 5);
            mainGrid.add(listAllBtn, 0, 6);
            mainGrid.add(settingsBtn, 0, 7);
            mainGrid.add(exitBtn, 0, 9);
            mainGrid.setAlignment(Pos.CENTER);

        } catch (IOException e) {
            System.out.println(e);
        }

        Scene scene = new Scene(mainGrid);
        if (importData.importConfig()[1].equalsIgnoreCase("dark")) {
            scene.getStylesheets().add("file:stylesheet/style.css");
        }
        primaryStage.setTitle("Karaoke Application");
        primaryStage.setScene(scene);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            primaryStage.close();
            System.exit(0);
        });
        primaryStage.show();

    }

}