package controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class chatRoomController {
    public AnchorPane chatRoomContex;
    public JFXTextField messageTxt;
    public JFXTextArea textArea;
    public VBox vbox;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;
    String userName;
    private FileChooser fileChooser;
    private File filePath;

    public void initialize() throws IOException {
        userName = SignController.username;

        try {
            socket = new Socket("localhost", 5001);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String msg = reader.readLine();
                        int start = msg.indexOf("<");
                        int end = msg.indexOf(">");
                        String message = msg.substring(start + 1, end);
                        String imagePath = msg.substring(end + 1);


                        VBox vBox = new VBox(10);
                        vBox.setAlignment(Pos.BOTTOM_RIGHT);
                        vbox.setAlignment(Pos.TOP_LEFT);
                        vBox.setAlignment(Pos.CENTER_LEFT);

                        Text text = new Text(message);
                        vBox.getChildren().add(text);

                        if (!imagePath.isEmpty()) {

                            File file = new File(imagePath);
                            Image image = new Image(file.toURI().toString());

                            ImageView imageView = new ImageView(image);

                            imageView.setFitHeight(100);
                            imageView.setFitWidth(150);
                            vBox.getChildren().add(imageView);
                        }


                        Platform.runLater(() -> vbox.getChildren().addAll(vBox));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    public void SendOnAction(javafx.event.ActionEvent actionEvent) {

        String msg = messageTxt.getText();
        if (!msg.isEmpty() && filePath != null) {
            writer.println("<" + userName + ": " + msg + ">" + filePath.getPath());
        } else if (filePath != null && msg.isEmpty()) {
            writer.println("<" + userName + ":   >" + filePath.getPath());
        } else if (!msg.isEmpty() && filePath == null) {
            writer.println("<" + userName + ": " + msg + ">" + "");
        }

        messageTxt.clear();

        if (msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);

        }

    }

    public void takePhtoOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
    }


}
