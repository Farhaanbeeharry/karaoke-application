import java.io.FileInputStream;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.stage.*;

public class DialogBox {

    public static void box(String message) {

        Stage messageWindow = new Stage();
        messageWindow.setTitle("Message");
        messageWindow.setResizable(false);
        messageWindow.initModality(Modality.APPLICATION_MODAL);

        GridPane messagePane = new GridPane();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.setVgap(30);

        Label displayMessage = new Label();
        displayMessage.setText(message);
        displayMessage.setAlignment(Pos.CENTER);

        Button btnOK = new Button("OK");
        btnOK.setMinWidth(250);
        btnOK.setFocusTraversable(false);
        btnOK.setOnAction(e -> messageWindow.close());

        messagePane.add(displayMessage, 0, 0);
        messagePane.add(btnOK, 0, 1);

        Scene scene = new Scene(messagePane, 600, 150);
        if (importData.importConfig()[1].equalsIgnoreCase("dark")) {
            scene.getStylesheets().add("file:stylesheet/style.css");
        }

        messageWindow.setScene(scene);
        messageWindow.showAndWait();

    }

}
