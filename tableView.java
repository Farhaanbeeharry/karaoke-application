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
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;


public class tableView {

    static Stage tableViewStage;
    static TableView table, playlistTable;
    static TextField searchField;

    public static void tableView(HashST<String, Song> songs) {

        tableViewStage = new Stage();
        tableViewStage.setTitle("All songs & My playlist");
        tableViewStage.setResizable(false);
        tableViewStage.setMaximized(true);

        GridPane tablePane = new GridPane();
        tablePane.setAlignment(Pos.CENTER);
        tablePane.setVgap(30);

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double columnWidth = screenWidth / 4;

        try {
            TableColumn<Song, String> songNameColumn = new TableColumn<>("Song Name");
            songNameColumn.setCellValueFactory(new PropertyValueFactory<>("songName"));
            songNameColumn.setMinWidth(columnWidth - 10);
	    songNameColumn.setSortable(true);
            songNameColumn.setResizable(false);

            TableColumn<Song, String> artistNameColumn = new TableColumn<>("Artist Name");
            artistNameColumn.setCellValueFactory(new PropertyValueFactory<>("artistName"));
            artistNameColumn.setMinWidth(columnWidth - 10);
            artistNameColumn.setResizable(false);
            artistNameColumn.setSortable(false);

            TableColumn<Song, Double> durationColumn = new TableColumn<>("Song Duration (seconds)");
            durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
            durationColumn.setMinWidth(columnWidth - 10);
            durationColumn.setResizable(false);
            durationColumn.setSortable(false);

            table = new TableView<>();
            table.setFocusTraversable(false);
            table.setEditable(false);
            table.prefHeightProperty().bind(tableViewStage.heightProperty());
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            getDefaultList(songs);

            table.getColumns().addAll(songNameColumn, artistNameColumn, durationColumn);
        } catch (Exception e) {
            System.out.println(e);
        }

        searchField = new TextField();
        searchField.setPromptText("Search for a song ...");
        searchField.setMinHeight(40);
        searchField.setMinWidth(300);
        searchField.setMaxWidth(300);
        searchField.setFocusTraversable(false);
        searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyPressed) {
		if (!(searchField.getText() + keyPressed.getText()).matches("")) {
if (keyPressed.getCode().equals(KeyCode.BACK_SPACE)) {
		searchSongName(songs, searchField.getText());
		} else {
                        searchSongName(songs, searchField.getText() + keyPressed.getText());
}
		} else {
		getDefaultList(songs);
		}
            }
        });

        Button searchBtn = new Button("Search");
        searchBtn.setMinWidth(300);
        searchBtn.setMinHeight(40);
        searchBtn.setFocusTraversable(false);
        searchBtn.setAlignment(Pos.CENTER);
        searchBtn.setOnAction(e -> {
            if (searchField.getText().matches("")) {
                DialogBox.box("Nothing to search!\nType something to search!");
            } else {
                searchSongName(songs, searchField.getText());
            }
        });

        Button restoreBtn = new Button("Show all");
        restoreBtn.setMinWidth(300);
        restoreBtn.setMinHeight(40);
        restoreBtn.setFocusTraversable(false);
        restoreBtn.setAlignment(Pos.CENTER);
        restoreBtn.setOnAction(e -> {
            getDefaultList(songs);
        });

        Button addToPlaylistBtn = new Button("Add to playlist");
        addToPlaylistBtn.setMinWidth(300);
        addToPlaylistBtn.setMinHeight(40);
        addToPlaylistBtn.setFocusTraversable(false);
        addToPlaylistBtn.setAlignment(Pos.CENTER);
        addToPlaylistBtn.setOnAction(e -> {
            int getSelectionTable = table.getSelectionModel().getSelectedIndex();
            if (getSelectionTable != -1) {
                Song selectedSong = (Song) table.getSelectionModel().getSelectedItem();
                String selectedSongName = selectedSong.getSongName();
                if (checkExistInPlaylist(selectedSongName) == 0) {
                    exportData.addToPlaylist(selectedSongName);
                    refreshPlaylist();
                } else {
                    DialogBox.box("Song is already in playlist!\nSelect another song to add to the playlist!!");
                }
            } else {
                DialogBox.box("No song selected!\nSelect a song to add to the playlist!");
            }
        });

        Button removeFromPlaylist = new Button("Remove from playlist");
        removeFromPlaylist.setMinWidth(300);
        removeFromPlaylist.setMinHeight(40);
        removeFromPlaylist.setFocusTraversable(false);
        removeFromPlaylist.setAlignment(Pos.CENTER);
        removeFromPlaylist.setOnAction(e -> {
            int getSelectionPlaylist = playlistTable.getSelectionModel().getSelectedIndex();
            if (getSelectionPlaylist != -1) {
                deleteFromPlaylist(getSelectionPlaylist);
                refreshPlaylist();
            } else {
                DialogBox.box("No song selected!\nSelect a song to remove from the playlist!");
            }
        });

        Button emptyPlaylist = new Button("Empty my playlist");
        emptyPlaylist.setMinWidth(300);
        emptyPlaylist.setMinHeight(40);
        emptyPlaylist.setFocusTraversable(false);
        emptyPlaylist.setAlignment(Pos.CENTER);
        emptyPlaylist.setOnAction(e -> {
            if (importData.getPlaylistCount() == 0) {
                DialogBox.box("Playlist is already empty!");
            } else {
                LinkedList<String> playlist = importData.getPlaylist();
                playlist.clear();
                exportData.updateFile("playlist.txt", playlist);
                refreshPlaylist();
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setMinWidth(300);
        backBtn.setMinHeight(40);
        backBtn.setFocusTraversable(false);
        backBtn.setAlignment(Pos.CENTER);
        backBtn.setOnAction(e -> {
            tableViewStage.close();
        });

        VBox tableVBox = new VBox();
        tableVBox.setMinWidth(3 * columnWidth);
        tableVBox.setMaxWidth(screenWidth - columnWidth);
        tableVBox.getChildren().add(table);

        try {
            TableColumn<Song, String> playlistColumn = new TableColumn<>("My Playlist");
            playlistColumn.setCellValueFactory(new PropertyValueFactory<>("songName"));
            playlistColumn.setMinWidth(columnWidth - 85);
            playlistColumn.setResizable(false);
            playlistColumn.setSortable(false);

            playlistTable = new TableView<>();
            playlistTable.setFocusTraversable(false);
            playlistTable.setMaxWidth(columnWidth - 80);
            playlistTable.setEditable(false);
            playlistTable.prefHeightProperty().bind(tableViewStage.heightProperty());
            playlistTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            refreshPlaylist();

            playlistTable.getColumns().addAll(playlistColumn);
        } catch (Exception e) {
            System.out.println(e);
        }

 VBox playlistBox = new VBox();
        playlistBox.setMinWidth(columnWidth);
        playlistBox.setAlignment(Pos.CENTER);
        playlistBox.getChildren().add(playlistTable);

        VBox optionVBox = new VBox(20);
        optionVBox.setMinWidth(columnWidth);
        optionVBox.setAlignment(Pos.CENTER);
        optionVBox.getChildren().addAll(searchField, searchBtn, restoreBtn, addToPlaylistBtn, removeFromPlaylist, emptyPlaylist, backBtn);

        VBox leftPaneBox = new VBox(50);
        leftPaneBox.minWidth(columnWidth);
        leftPaneBox.setPadding(new Insets(20, 0, 20, 0));
        leftPaneBox.setAlignment(Pos.CENTER);
        leftPaneBox.getChildren().addAll(playlistBox, optionVBox);

        HBox tableHBox = new HBox();
        tableHBox.setAlignment(Pos.CENTER_LEFT);
        if (importData.importConfig().equalsIgnoreCase("left")) {
            tableHBox.getChildren().addAll(leftPaneBox, tableVBox);
        } else if (importData.importConfig().equalsIgnoreCase("right")) {
            tableHBox.getChildren().addAll(tableVBox, leftPaneBox);
        }

        tablePane.add(tableHBox, 0, 0);

        Scene scene = new Scene(tablePane);

        tableViewStage.setScene(scene);
        tableViewStage.show();

    }

    public static void searchSongName(HashST<String, Song> songs, String criteria) {

        table.getItems().clear();

        ObservableList<Song> song = FXCollections.observableArrayList();

        for (String songName : songs.keys()) {

            if (songName.toLowerCase().contains(criteria.toLowerCase())) {
                song.add(songs.get(songName));
            }

        }

        table.setItems(song);

    }


    public static void getDefaultList(HashST<String, Song> songs) {

        table.getItems().clear();

        ObservableList<Song> song = FXCollections.observableArrayList();

        for (String songName : songs.keys()) {
            song.add(songs.get(songName));

        }

        table.setItems(song);


    }

    public static void refreshPlaylist() {

        LinkedList<String> playlist = importData.getPlaylist();

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
