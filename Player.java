import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;
import java.beans.JavaBean;
import java.util.ArrayList;

public class Player {

    static Stage mediaPlayerStage;
    static TableView playlistTable;
    static MediaPlayer mediaPlayer;

    public static void playPlaylist(HashST<String, Song> songs) {

        ArrayList<String> playlist = importData.getPlaylist();
        mediaPlayerStage = new Stage();

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double columnWidth = screenWidth / 4;

        try {
            TableColumn<Song, String> playlistColumn = new TableColumn<>("My Playlist");
            playlistColumn.setCellValueFactory(new PropertyValueFactory<>("songName"));
            playlistColumn.setMinWidth(columnWidth - 65);
            playlistColumn.setResizable(false);
            playlistColumn.setSortable(false);

            playlistTable = new TableView<>();
            playlistTable.setFocusTraversable(false);
            playlistTable.setMaxWidth(columnWidth);
            playlistTable.setEditable(false);
            playlistTable.prefHeightProperty().bind(mediaPlayerStage.heightProperty());
            playlistTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            refreshPlaylist();

            playlistTable.getColumns().addAll(playlistColumn);
        } catch (Exception e) {
            System.out.println(e);
        }

        playlistTable.getSelectionModel().selectFirst();
        Song selectedSong = (Song) playlistTable.getSelectionModel().getSelectedItem();
        String fileName = songs.get(selectedSong.getSongName()).getFileName();

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(20);

        Label songTitle = new Label();
        songTitle.setText(songs.get(selectedSong.getSongName()).getSongName() + " - " + songs.get(selectedSong.getSongName()).getArtistName());
        songTitle.setMinWidth(screenWidth - columnWidth);

        Media media = new Media("file:///home/cst2550/IdeaProjects/KaraokeApplication/src/videos/" + fileName);
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(screenWidth - columnWidth);

        Button addSongBtn = new Button("Add a song to playlist");
        addSongBtn.setFocusTraversable(false);
        addSongBtn.setMinWidth(300);
        addSongBtn.setMinHeight(40);
        addSongBtn.setAlignment(Pos.CENTER);
        addSongBtn.setOnAction(e -> {
            mediaPlayer.stop();
            mediaPlayerStage.close();
            tableView.tableView(songs);
        });

        Button deleteBtn = new Button("Delete from playlist");
        deleteBtn.setFocusTraversable(false);
        deleteBtn.setMinWidth(300);
        deleteBtn.setMinHeight(40);
        deleteBtn.setAlignment(Pos.CENTER);
        deleteBtn.setOnAction(e -> {
            int getSelectionPlaylist = playlistTable.getSelectionModel().getSelectedIndex();
                if (getSelectionPlaylist == 0) {
                    deleteFromPlaylist(getSelectionPlaylist);
                    refreshPlaylist();
                    mediaPlayer.stop();
                    playlistTable.getSelectionModel().selectFirst();
                    Song newSelectedSong = (Song) playlistTable.getSelectionModel().getSelectedItem();
                    String newFileName = songs.get(newSelectedSong.getSongName()).getFileName();
                    Media nextMedia = new Media("file:///home/cst2550/IdeaProjects/KaraokeApplication/src/videos/" + newFileName);
                    mediaPlayer.dispose();
                    mediaPlayer = new MediaPlayer(nextMedia);
                    mediaView.setMediaPlayer(mediaPlayer);
                    mediaPlayer.play();
                    songTitle.setText(songs.get(newSelectedSong.getSongName()).getSongName() + " - " + songs.get(newSelectedSong.getSongName()).getArtistName());
                } else if (getSelectionPlaylist > 0) {
                    deleteFromPlaylist(getSelectionPlaylist);
                    refreshPlaylist();
                    playlistTable.getSelectionModel().selectFirst();

                } else {
                DialogBox.box("No song selected!\nSelect a song to remove from the playlist!");
            }
            if (importData.getPlaylistCount() == 0) {
                mediaPlayer.stop();
                mediaPlayerStage.close();
                DialogBox.box("Nothing in your playlist!\nAdd a song in your playlist to play!");
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setFocusTraversable(false);
        backBtn.setMinWidth(300);
        backBtn.setMinHeight(40);
        backBtn.setAlignment(Pos.CENTER);
        backBtn.setOnAction(e -> {
            mediaPlayer.stop();
            mediaPlayerStage.close();
        });

        Button pauseBtn = new Button("Pause");
        pauseBtn.setFocusTraversable(false);
        pauseBtn.setMinWidth(100);
        pauseBtn.setMinHeight(40);
        pauseBtn.setAlignment(Pos.CENTER);
        pauseBtn.setOnAction(e -> {
            mediaPlayer.pause();
        });

        Button playBtn = new Button("Play");
        playBtn.setFocusTraversable(false);
        playBtn.setMinWidth(100);
        playBtn.setMinHeight(40);
        playBtn.setAlignment(Pos.CENTER);
        playBtn.setOnAction(e -> {
            mediaPlayer.play();
        });

        Button stopBtn = new Button("Stop");
        stopBtn.setFocusTraversable(false);
        stopBtn.setMinWidth(100);
        stopBtn.setMinHeight(40);
        stopBtn.setAlignment(Pos.CENTER);
        stopBtn.setOnAction(e -> {
            mediaPlayer.stop();
        });

        Button nextBtn = new Button("Next");
        nextBtn.setFocusTraversable(false);
        nextBtn.setMinWidth(100);
        nextBtn.setMinHeight(40);
        nextBtn.setAlignment(Pos.CENTER);
        nextBtn.setOnAction(e -> {
            if (importData.getPlaylistCount() != 1) {
                mediaPlayer.stop();
                int getSelectionPlaylist = playlistTable.getSelectionModel().getSelectedIndex();
                deleteFromPlaylist(getSelectionPlaylist);
                refreshPlaylist();
                playlistTable.getSelectionModel().selectFirst();
                Song newSelectedSong = (Song) playlistTable.getSelectionModel().getSelectedItem();
                String newFileName = songs.get(newSelectedSong.getSongName()).getFileName();
                Media nextMedia = new Media("file:///home/cst2550/IdeaProjects/KaraokeApplication/src/videos/" + newFileName);
                mediaPlayer.dispose();
                mediaPlayer = new MediaPlayer(nextMedia);
                mediaView.setMediaPlayer(mediaPlayer);
                mediaPlayer.play();
                songTitle.setText(songs.get(newSelectedSong.getSongName()).getSongName() + " - " + songs.get(newSelectedSong.getSongName()).getArtistName());
            } else if (importData.getPlaylistCount() == 1) {
                DialogBox.box("There is no next media to play!\nAdd more songs to the playlist first!");
            }
        });

        Button muteBtn = new Button("Mute");
        muteBtn.setFocusTraversable(false);
        muteBtn.setMinHeight(40);
        muteBtn.setMinWidth(100);
        muteBtn.setAlignment(Pos.CENTER);
        muteBtn.setOnAction(e -> {
            mediaPlayer.setMute(true);
        });

        Button unmuteBtn = new Button("Unmute");
        unmuteBtn.setFocusTraversable(false);
        unmuteBtn.setMinHeight(40);
        unmuteBtn.setMinWidth(100);
        unmuteBtn.setAlignment(Pos.CENTER);
        unmuteBtn.setOnAction(e -> {
            mediaPlayer.setMute(false);
        });

        VBox playerBox = new VBox();
        playerBox.setAlignment(Pos.CENTER);
        playerBox.setPadding(new Insets(0, 20, 0, 0));
        playerBox.getChildren().add(mediaView);

        HBox mediaBtnBox = new HBox(20);
        mediaBtnBox.setAlignment(Pos.CENTER);
        mediaBtnBox.setPadding(new Insets(25, 0, 0, 0));
        mediaBtnBox.getChildren().addAll(pauseBtn, playBtn, stopBtn, nextBtn, muteBtn, unmuteBtn);

        VBox mediaVBox = new VBox(20);
        mediaVBox.setAlignment(Pos.CENTER);
        mediaVBox.setPadding(new Insets(0, 0, 60, 0));
        mediaVBox.getChildren().addAll(songTitle, playerBox, mediaBtnBox);

        VBox listBox = new VBox();
        listBox.setAlignment(Pos.CENTER);
        listBox.getChildren().add(playlistTable);

        VBox playlistBtnBox = new VBox(20);
        playlistBtnBox.setAlignment(Pos.CENTER);
        playlistBtnBox.setPadding(new Insets(0, 0, 20, 0));
        playlistBtnBox.getChildren().addAll(addSongBtn, deleteBtn, backBtn);

        VBox leftPaneBox = new VBox(20);
        leftPaneBox.setAlignment(Pos.CENTER);
        leftPaneBox.setPadding(new Insets(20, 0, 0, 20));
        leftPaneBox.getChildren().addAll(listBox, playlistBtnBox);

        HBox windowBox = new HBox(20);
        windowBox.setAlignment(Pos.CENTER);
        windowBox.getChildren().addAll(leftPaneBox, mediaVBox);

        gridPane.add(windowBox, 0, 0);

        Scene scene = new Scene(gridPane);

        mediaPlayerStage.setScene(scene);
        mediaPlayerStage.setMaximized(true);
        mediaPlayerStage.setResizable(false);
        mediaPlayerStage.setTitle("Media Player");
        mediaPlayerStage.show();
        mediaPlayerStage.setOnCloseRequest(e -> {
            mediaPlayer.stop();
            e.consume();
            mediaPlayerStage.close();
        });

        mediaPlayer.play();

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            int getSelectionPlaylist = playlistTable.getSelectionModel().getSelectedIndex();
            deleteFromPlaylist(getSelectionPlaylist);
            refreshPlaylist();
            playlistTable.getSelectionModel().selectFirst();
            Song newSelectedSong = (Song) playlistTable.getSelectionModel().getSelectedItem();
            String newFileName = songs.get(newSelectedSong.getSongName()).getFileName();
            Media nextMedia = new Media("file:///home/cst2550/IdeaProjects/KaraokeApplication/src/videos/" + newFileName);
            mediaPlayer.dispose();
            mediaPlayer = new MediaPlayer(nextMedia);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
            songTitle.setText(songs.get(newSelectedSong.getSongName()).getSongName() + " - " + songs.get(newSelectedSong.getSongName()).getArtistName());

        });

    }

    public static void refreshPlaylist() {

        ArrayList<String> playlist = importData.getPlaylist();

        playlistTable.getItems().clear();

        for (int i = 0; i < playlist.size(); i++) {
            playlistTable.getItems().add(new Song(playlist.get(i)));
        }

    }

    public static void deleteFromPlaylist(int indexToDelete) {
        ArrayList<String> playlist = importData.getPlaylist();
        playlist.remove(indexToDelete);
        exportData.updateFile("playlist.txt", playlist);
    }

}
