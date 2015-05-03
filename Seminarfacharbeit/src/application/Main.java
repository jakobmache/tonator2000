
package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {

	private VBox root;
	private Scene scene;
	
	private CheckBox sineCheckbox;
	private CheckBox sawCheckbox;


	@Override
	public void start(Stage primaryStage) throws InterruptedException{
		try {

			initLayout();
			initScene();

			primaryStage.setScene(scene);
			primaryStage.show();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}

	}

	private void initLayout()
	{
		root = new VBox();
		sineCheckbox = new CheckBox("Sinus");
		sawCheckbox = new CheckBox("Sägezahn");
		root.getChildren().add(sineCheckbox);
		root.getChildren().add(sawCheckbox);
	}

	private void initScene()
	{
		scene = new Scene(root, 400, 400);
	}


public static void main(String[] args) {
	launch(args);
}
}
