package test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class EditorTest extends Application
{


	@Override
	public void start(Stage primaryStage) throws Exception 
	{

		Scene scene = new Scene(new HBox());
		primaryStage.setScene(scene);
		primaryStage.show();
	}


	public static void main(String[] args)
	{
		launch(args);
	}

}

