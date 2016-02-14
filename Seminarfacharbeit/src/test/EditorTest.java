package test;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditorTest extends Application
{
	
	private Pane editorPane;

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		VBox root = new VBox();
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(new Menu("Test"));
		editorPane = new Pane();
		
		root.getChildren().add(menuBar);
		root.getChildren().add(editorPane);
		
		editorPane.setPrefSize(1000, 700);
		
		initBindings();
		
		Scene scene = new Scene(root);
		
		editorPane.requestFocus();
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void initBindings()
	{
		editorPane.setOnMouseClicked((event) -> 
		{
			if (event.getButton() == MouseButton.SECONDARY)
			{
				Module moduleInput = new Module();
				Node inputGui = moduleInput.getGui();
				inputGui.setLayoutX(event.getX());
				inputGui.setLayoutY(event.getY());
				
				Module moduleOutput = new Module();
				Node outputGui = moduleOutput.getGui();
				outputGui.setLayoutX(event.getX() + 200);
				outputGui.setLayoutY(event.getY());
				
				Wire line = new Wire();
				line.setInput(moduleInput.getPort());
				line.setOutput(moduleOutput.getPort());
				
				editorPane.getChildren().add(inputGui);
				editorPane.getChildren().add(outputGui);
				editorPane.getChildren().add(line);
			}
		});
	}


	public static void main(String[] args)
	{
		launch(args);
	}

}

