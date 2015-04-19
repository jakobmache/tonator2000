/*
 * 
 */
package application;
	
import javax.sound.sampled.LineUnavailableException;

import containers.StandardModuleContainer;
import engine.SynthesizerEngine;



import engine.ModuleContainer;
import engine.SynthesizerEngine;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main extends Application {
	
	/** The root. */
	private BorderPane root;
	
	/** The scene. */
	private Scene scene;
	
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			
			initLayout();
			createOscillatorInputs();
			initScene();
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Inits the layout.
	 */
	private void initLayout()
	{
		root = new BorderPane();
	}
	
	/**
	 * Inits the scene.
	 */
	private void initScene()
	{
		scene = new Scene(root, 400, 400);
	}
	
	/**
	 * Creates the oscillator inputs.
	 *
	 * @throws Exception the exception
	 */
	private void createOscillatorInputs() throws Exception
	{
		HBox hbox = new HBox();
		TextField frequency = new TextField();
		hbox.getChildren().add(frequency);
		Button applyButton = new Button("Apply");
		hbox.getChildren().add(applyButton);
		root.setTop(hbox);
		
		SynthesizerEngine parent = new SynthesizerEngine();
		ModuleContainer container = new StandardModuleContainer(parent);
		container.getToneModule().processSample(100);
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
