package csvJson;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Principal extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Cliente\\TelaPrincipal.fxml"));
		AnchorPane root = new AnchorPane();
		loader.setRoot(root);
		loader.load();
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
}
