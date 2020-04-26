import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.io.File;
import java.util.LinkedList;

public class Player {

    static Stage mediaPlayerStage;
    static TableView playlistTable, allSongTable;
    static Media media;
    static MediaView mediaView;
    static MediaPlayer mediaPlayer;
    static Label songTitle;
    static File file;
    static TextField searchField;
    static Slider timeSlider, volumeSlider;
    static boolean muteState = false;
    static double volume = 100;
    static LinkedList<String> playlist = importData.getPlaylist();

    public static void playPlaylist(HashST<String, Song> songs) {

        mediaPlayerStage = new Stage();

        playlist = importData.getPlaylist();

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

        songTitle = new Label();
        songTitle.setText(songs.get(selectedSong.getSongName()).getSongName() + " - " + songs.get(selectedSong.getSongName()).getArtistName());
        songTitle.setMinWidth(screenWidth - columnWidth);

        file = new File("videos/" + fileName);

        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(screenWidth - columnWidth);


        try {
            TableColumn<Song, String> songNameColumn = new TableColumn<>("Songs");
            songNameColumn.setCellValueFactory(new PropertyValueFactory<>("songName"));
            songNameColumn.setMinWidth(columnWidth - 85);
            songNameColumn.setResizable(false);
            songNameColumn.setSortable(false);

            allSongTable = new TableView<>();
            allSongTable.setFocusTraversable(false);
            allSongTable.setMaxWidth(columnWidth);
            allSongTable.setEditable(false);
            allSongTable.prefHeightProperty().bind(mediaPlayerStage.heightProperty());
            allSongTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            getDefaultList(songs);

            allSongTable.getColumns().addAll(songNameColumn);
        } catch (Exception e) {
            System.out.println(e);
        }

        searchField = new TextField();
        searchField.setPromptText("Search for a song ...");
        searchField.setMinHeight(40);
        searchField.setMinWidth((columnWidth - 95) / 3 * 2);
        searchField.setMaxWidth((columnWidth - 95) / 3 * 2);
        searchField.setFocusTraversable(false);
        searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyPressed) {
                if (keyPressed.getCode().equals(KeyCode.ENTER)) {
                    if (!searchField.getText().matches("")) {
                        searchSongName(songs, searchField.getText());
                    } else {
                        getDefaultList(songs);
                    }
                }
            }
        });

        Button searchBtn = new Button("Search");
        searchBtn.setFocusTraversable(false);
        searchBtn.setMinWidth((columnWidth - 95) / 3 * 1);
        searchBtn.setMinHeight(40);
        searchBtn.setAlignment(Pos.CENTER);
        searchBtn.setOnAction(e -> {
            if (searchField.getText().matches("")) {
                getDefaultList(songs);
            } else {
                searchSongName(songs, searchField.getText());
            }
        });


        Button defaultBtn = new Button("Show all songs");
        defaultBtn.setFocusTraversable(false);
        defaultBtn.setMinWidth((columnWidth - 95) / 2);
        defaultBtn.setMinHeight(40);
        defaultBtn.setAlignment(Pos.CENTER);
        defaultBtn.setOnAction(e -> {
            gridPane.requestFocus();
            getDefaultList(songs);
        });

        Button addSongBtn = new Button("Add song to playlist");
        addSongBtn.setFocusTraversable(false);
        addSongBtn.setMinWidth((columnWidth - 95) / 2);
        addSongBtn.setMinHeight(40);
        addSongBtn.setAlignment(Pos.CENTER);
        addSongBtn.setOnAction(e -> {
            gridPane.requestFocus();
            int getSelectionTable = allSongTable.getSelectionModel().getSelectedIndex();
            if (getSelectionTable != -1) {
                Song selectedAddSong = (Song) allSongTable.getSelectionModel().getSelectedItem();
                String selectedSongName = selectedAddSong.getSongName();
                if (checkExistInPlaylist(selectedSongName) == 0) {
                    exportData.addToPlaylist(selectedSongName);
                    refreshPlaylist();
                    playlistTable.getSelectionModel().selectFirst();
                } else {
                    DialogBox.box("Song is already in playlist!\nSelect another song to add to the playlist!!");
                }
            } else {
                DialogBox.box("No song selected!\nSelect a song to add to the playlist!");
            }
        });

        Button deleteBtn = new Button("Delete from playlist");
        deleteBtn.setFocusTraversable(false);
        deleteBtn.setMinWidth(columnWidth - 85);
        deleteBtn.setMinHeight(40);
        deleteBtn.setAlignment(Pos.CENTER);
        deleteBtn.setOnAction(e -> {
            gridPane.requestFocus();
            int getSelectionPlaylist = playlistTable.getSelectionModel().getSelectedIndex();
            if (getSelectionPlaylist == 0) {
                deleteFromPlaylist(getSelectionPlaylist);
                refreshPlaylist();
                mediaPlayer.stop();
                playlistTable.getSelectionModel().selectFirst();
                Song newSelectedSong = (Song) playlistTable.getSelectionModel().getSelectedItem();
                String newFileName = songs.get(newSelectedSong.getSongName()).getFileName();
                file = new File("videos/" + newFileName);
                media = new Media(file.toURI().toString());
                mediaPlayer.dispose();
                mediaPlayer = new MediaPlayer(media);
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
        backBtn.setMinWidth(columnWidth - 85);
        backBtn.setMinHeight(40);
        backBtn.setAlignment(Pos.CENTER);
        backBtn.setOnAction(e -> {
            mediaPlayer.stop();
            mediaPlayerStage.close();
        });

        Button pauseBtn = new Button();
        pauseBtn.setText("Pause");
        pauseBtn.setFocusTraversable(false);
        pauseBtn.setMinWidth(100);
        pauseBtn.setMinHeight(40);
        pauseBtn.setAlignment(Pos.CENTER);
        pauseBtn.setOnAction(e -> {

            MediaPlayer.Status status = mediaPlayer.getStatus();

            if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                pauseBtn.setText("Play");
            } else {
                mediaPlayer.play();
                pauseBtn.setText("Pause");
            }
        });

        Button stopBtn = new Button("Stop");
        stopBtn.setFocusTraversable(false);
        stopBtn.setMinWidth(100);
        stopBtn.setMinHeight(40);
        stopBtn.setAlignment(Pos.CENTER);
        stopBtn.setOnAction(e -> {

            MediaPlayer.Status status = mediaPlayer.getStatus();

            if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                pauseBtn.setText("Play");
            }
            mediaPlayer.stop();
        });

        Button nextBtn = new Button("Next");
        nextBtn.setFocusTraversable(false);
        nextBtn.setMinWidth(100);
        nextBtn.setMinHeight(40);
        nextBtn.setAlignment(Pos.CENTER);
        nextBtn.setOnAction(e -> {
            nextButtonAction(songs, playlist);
        });

        Button muteBtn = new Button("Mute");
        muteBtn.setFocusTraversable(false);
        muteBtn.setMinHeight(40);
        muteBtn.setMinWidth(100);
        muteBtn.setAlignment(Pos.CENTER);
        muteBtn.setOnAction(e -> {

            if (!muteState) {
                mediaPlayer.setMute(true);
                muteBtn.setText("Unmute");
                muteState = !muteState;
            } else {
                mediaPlayer.setMute(false);
                muteBtn.setText("Mute");
                muteState = !muteState;
            }
        });

        Button fullScreenBtn = new Button("Fullscreen (Esc to switch back)");
        fullScreenBtn.setFocusTraversable(false);
        fullScreenBtn.setMinHeight(40);
        fullScreenBtn.setMinWidth(150);
        fullScreenBtn.setAlignment(Pos.CENTER);

        timeSlider = new Slider();
        timeSlider.setFocusTraversable(false);
        timeSlider.setShowTickLabels(true);
        timeSlider.setShowTickMarks(false);
        timeSlider.setMajorTickUnit(60);
        timeSlider.setMinWidth(screenWidth - columnWidth - 75);
        timeSlider.setMaxWidth(screenWidth - columnWidth - 75);

        refreshTimeSlider();

        Text volumeText = new Text();
        volumeText.setText("Volume:");

        volumeSlider = new Slider();
        volumeSlider.setFocusTraversable(false);
        volumeSlider.setShowTickLabels(false);
        volumeSlider.setShowTickMarks(false);
        volumeSlider.setMajorTickUnit(100);
        volumeSlider.setMinWidth(200);
        volumeSlider.setMaxWidth(200);
        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(volume);

        refreshVolumeSlider();

        VBox playerBox = new VBox();
        playerBox.setAlignment(Pos.CENTER);
        playerBox.setPadding(new Insets(0, 20, 0, 0));
        playerBox.getChildren().add(mediaView);

        HBox volumeBox = new HBox(20);
        volumeBox.setAlignment(Pos.CENTER);
        volumeBox.setPadding(new Insets(0, 50, 0, 50));
        volumeBox.getChildren().addAll(volumeText, volumeSlider);

        HBox mediaBtnBox = new HBox(20);
        mediaBtnBox.setAlignment(Pos.CENTER);
        mediaBtnBox.setPadding(new Insets(0, 0, 0, 0));
        mediaBtnBox.getChildren().addAll(pauseBtn, stopBtn, nextBtn, muteBtn, volumeBox, fullScreenBtn);

        HBox sliderBox = new HBox();
        sliderBox.setAlignment(Pos.CENTER);
        sliderBox.setPadding(new Insets(0, 20, 0, 0));
        sliderBox.getChildren().add(timeSlider);

        VBox mediaVBox = new VBox(20);
        mediaVBox.setAlignment(Pos.CENTER);
        mediaVBox.setPadding(new Insets(0, 0, 60, 0));
        mediaVBox.getChildren().addAll(songTitle, playerBox, sliderBox, mediaBtnBox);

        VBox listBox = new VBox();
        listBox.setAlignment(Pos.CENTER);
        listBox.setMaxHeight(400);
        listBox.getChildren().add(playlistTable);

        VBox allSongsBox = new VBox();
        allSongsBox.setAlignment(Pos.CENTER);
        allSongsBox.setMaxHeight(400);
        allSongsBox.getChildren().add(allSongTable);

        HBox searchBox = new HBox(20);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.getChildren().addAll(searchField, searchBtn);

        HBox btnBox = new HBox(20);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(defaultBtn, addSongBtn);

        VBox playlistBtnBox = new VBox(20);
        playlistBtnBox.setAlignment(Pos.CENTER);
        playlistBtnBox.setPadding(new Insets(0, 0, 20, 0));
        playlistBtnBox.getChildren().addAll(searchBox, btnBox, deleteBtn, backBtn);

        VBox leftPaneBox = new VBox(20);
        leftPaneBox.setAlignment(Pos.CENTER);
        leftPaneBox.setPadding(new Insets(20, 0, 0, 20));
        leftPaneBox.getChildren().addAll(listBox, allSongsBox, playlistBtnBox);

        HBox windowBox = new HBox(20);
        windowBox.setAlignment(Pos.CENTER);
        windowBox.getChildren().addAll(leftPaneBox, mediaVBox);

        gridPane.add(windowBox, 0, 0);
        gridPane.requestFocus();

        Scene scene = new Scene(gridPane);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.SPACE)) {
                    MediaPlayer.Status status = mediaPlayer.getStatus();

                    if (status == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.pause();
                        pauseBtn.setText("Play");
                    } else {
                        mediaPlayer.play();
                        pauseBtn.setText("Pause");
                    }
                }
            }
        });

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

        fullScreenBtn.setOnAction(e -> {
            GridPane fullPane = new GridPane();
            fullPane.setAlignment(Pos.CENTER);
            fullPane.add(mediaView, 0, 0);
            mediaPlayerStage.getScene().setRoot(fullPane);
            mediaView.setFitWidth(screenWidth);
            mediaPlayerStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
                        scene.setRoot(gridPane);
                        mediaView.setFitWidth(screenWidth - columnWidth);
                        playerBox.getChildren().add(mediaView);
                        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                            pauseBtn.setText("Pause");
                        } else {
                            pauseBtn.setText("Play");
                        }
                        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent keyEvent) {
                                if (keyEvent.getCode().equals(KeyCode.SPACE)) {
                                    MediaPlayer.Status status = mediaPlayer.getStatus();

                                    if (status == MediaPlayer.Status.PLAYING) {
                                        mediaPlayer.pause();
                                        pauseBtn.setText("Play");
                                    } else {
                                        mediaPlayer.play();
                                        pauseBtn.setText("Pause");
                                    }
                                }
                            }
                        });
                    } else if (keyEvent.getCode().equals(KeyCode.SPACE)) {
                        MediaPlayer.Status status = mediaPlayer.getStatus();

                        if (status == MediaPlayer.Status.PLAYING) {
                            mediaPlayer.pause();
                            pauseBtn.setText("Play");
                        } else {
                            mediaPlayer.play();
                            pauseBtn.setText("Pause");
                        }
                    }

                }
            });

        });

        mediaPlayer.play();

        mediaPlayer.setOnEndOfMedia(() -> {
            nextButtonAction(songs, playlist);
        });

    }

    private static void refreshVolumeSlider() {

        mediaPlayer.setVolume(volume / 100);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (volumeSlider.isPressed()) {
                    mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                    volume = volumeSlider.getValue();
                }
            }
        });

    }

    private static void refreshTimeSlider() {

        mediaPlayer.setOnReady(() -> {
            timeSlider.setMin(0);
            timeSlider.setMax(mediaPlayer.getMedia().getDuration().toSeconds());
            timeSlider.setValue(0);
        });

        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                Duration duration = mediaPlayer.getCurrentTime();

                timeSlider.setValue(duration.toSeconds());
            }
        });

        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (timeSlider.isPressed()) {
                    double val = timeSlider.getValue();
                    mediaPlayer.seek(new Duration(val * 1000));
                }
            }
        });
    }

    private static void nextButtonAction(HashST<String, Song> songs, LinkedList<String> playlist) {
        if (importData.getPlaylistCount() > 1) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            playlistTable.getSelectionModel().selectFirst();
            int selectedUpdateIndex = playlistTable.getSelectionModel().getSelectedIndex();
            deleteFromPlaylist(selectedUpdateIndex);
            refreshPlaylist();
            playlistTable.getSelectionModel().selectFirst();
            Song newSelectedSong = (Song) playlistTable.getSelectionModel().getSelectedItem();
            String newFileName = songs.get(newSelectedSong.getSongName()).getFileName();
            file = new File("videos/" + newFileName);
            media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
            refreshTimeSlider();
            refreshVolumeSlider();
            mediaPlayer.setOnEndOfMedia(() -> {
                nextButtonAction(songs, playlist);
            });
            songTitle.setText(songs.get(newSelectedSong.getSongName()).getSongName() + " - " + songs.get(newSelectedSong.getSongName()).getArtistName());
        } else if (importData.getPlaylistCount() == 1) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayerStage.close();
            deleteFromPlaylist(0);
            DialogBox.box("There is no song in your playlist!");
        }
    }


    public static void refreshPlaylist() {

        playlist = importData.getPlaylist();

        playlistTable.getItems().clear();

        for (int i = 0; i < playlist.size(); i++) {
            playlistTable.getItems().add(new Song(playlist.get(i)));
        }


    }

    public static void deleteFromPlaylist(int indexToDelete) {
        LinkedList<String> playlist = importData.getPlaylist();
        playlist.remove(indexToDelete);
        exportData.updateFile("playlist.txt", playlist);
    }

    public static void searchSongName(HashST<String, Song> songs, String criteria) {

        allSongTable.getItems().clear();

        ObservableList<Song> song = FXCollections.observableArrayList();

        for (String songName : songs.keys()) {

            if (songName.toLowerCase().contains(criteria.toLowerCase())) {
                song.add(songs.get(songName));
            }

        }

        allSongTable.setItems(song);

    }


    public static void getDefaultList(HashST<String, Song> songs) {

        allSongTable.getItems().clear();

        ObservableList<Song> song = FXCollections.observableArrayList();

        for (String songName : songs.keys()) {
            song.add(songs.get(songName));

        }

        allSongTable.setItems(song);


    }

    public static int checkExistInPlaylist(String songName) {

        LinkedList<String> playlist = importData.getPlaylist();

        for (int i = 0; i < playlist.size(); i++) {
            if (playlist.get(i).equalsIgnoreCase(songName)) {
                return 1;
            }
        }

        return 0;
    }


}
