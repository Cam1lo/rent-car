package cu.edu.cujae.carRent.visuals.login;

import cu.edu.cujae.carRent.dot.UserDto;
import cu.edu.cujae.carRent.services.ServicesLocator;
import cu.edu.cujae.carRent.utils.Validations;
import cu.edu.cujae.carRent.utils.bdResponses.LoginResponse;
import cu.edu.cujae.carRent.visuals.mainLayout.MainLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class LoginController {
    @FXML
    private Button login_button;
    @FXML
    private TextField user_name;
    @FXML
    private PasswordField user_password;
    @FXML
    private Label error_label;
    @FXML
    private AnchorPane ap;
    @FXML
    private ImageView close_icon;

    private Stage stage;
    private Double xOffset;
    private Double yOffset;

    public void apMouseDragged(MouseEvent event) {
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    public void apMousePressed(MouseEvent event) {
        this.stage = (Stage) ap.getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    public void login(ActionEvent e) throws IOException, SQLException {

        String username = this.user_name.getText();
        String password = this.user_password.getText();

        String errors = Validations.loginValidation(username, password);

        if (errors.equals("")) {
            LoginResponse loginResponse = ServicesLocator.getUserServices().authentication(username, password);

            if (loginResponse.getUser() != null) {
                this.stage = (Stage) ap.getScene().getWindow();
                this.stage.close();
                goToMainLayout(loginResponse.getUser());
            } else if (!loginResponse.getError().equals("")) {
                errors = loginResponse.getError();
                this.error_label.setText(errors);
                this.user_password.clear();
            }
        } else {
            this.error_label.setText(errors);
        }
    }

    public void goToMainLayout(UserDto user) throws IOException {
        Stage mainStage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainLayout/MainLayout.fxml"));
        Parent root = loader.load();
        MainLayout mainLayoutController = (MainLayout) loader.getController();
        mainLayoutController.onInit(user);

        mainStage.setScene(new Scene(root));
        mainStage.show();
        mainStage.setMaximized(true);
        mainStage.toFront();
    }

    public void closeApplication(MouseEvent e) {
        System.exit(0);
    }
}