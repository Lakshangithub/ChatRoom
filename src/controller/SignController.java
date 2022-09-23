package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import util.UiNavigateUtil;


import java.io.IOException;

public class SignController {

    public AnchorPane signContex;
    public JFXTextField txtUserName;
    public static String username;

    public void initialize() throws IOException {

    }

    public void cancelOnAction(ActionEvent actionEvent) {
        UiNavigateUtil.closeForm(signContex);
    }

    public void SignOnAction(ActionEvent actionEvent) throws IOException {
        if (!txtUserName.getText().isEmpty()) {
            username = txtUserName.getText();
            UiNavigateUtil.newUi(signContex, "ChatRoom");
        }

    }
}
