
package application;

import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.xml.stream.events.EndElement;

import modules.Oscillator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import containers.StandardModuleContainer;
import engine.ModuleContainer;
import engine.SynthesizerEngine;


public class Main extends Application {

	private BorderPane root;
	private Scene scene;


	@Override
	public void start(Stage primaryStage) throws InterruptedException{
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

	private void initLayout()
	{
		root = new BorderPane();
	}

	private void initScene()
	{
		scene = new Scene(root, 400, 400);
	}

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

		float samplesTotal = 44100;//parent.getSamplingRate();
		double fCyclePosition = 0;  

		Oscillator osci = container.getToneModule();
		osci.setFrequency((float) 500);
		
		long starttime = System.currentTimeMillis();
		
		 String path = "C:\\Users\\Jakob\\Documents\\ausgabe.txt";
	      BufferedWriter bw = new BufferedWriter(new FileWriter(path, false));
	      
		while (samplesTotal>0) {

			double fCycleInc = container.getToneModule().getFrequency() / parent.getSamplingRate();  // Fraction of cycle between samples

			short value = (short)(Short.MAX_VALUE * Math.sin(2*Math.PI * fCyclePosition));

			bw.write(Short.toString(value));
			bw.newLine();
			
			fCyclePosition += fCycleInc;
			if (fCyclePosition > 1)
				fCyclePosition -= 1;

			osci.processSample((float) value);
			samplesTotal -= 1;
			System.out.println("Samples total:" + samplesTotal);
			

		}
		bw.close();
		
		long endtime = System.currentTimeMillis();
		System.out.println(endtime - starttime);

		

	}


public static void main(String[] args) {
	launch(args);
}
}
