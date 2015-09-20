package ui;


import modules.Oscillator;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import engine.SynthesizerEngine;

public class OscillatorController{
	
	private SynthesizerEngine engine;
	private FXMLLoader loader;
	
	private CheckBox sineBox;
	private CheckBox sawBox;
	private CheckBox triangleBox;
	private CheckBox squareBox;
	
	public OscillatorController(SynthesizerEngine engine)
	{
		this.engine = engine;
	}

	
	public void onSineBoxAction(ActionEvent event)
	{
		engine.setOscillatorType(Oscillator.TYPE_SINE);
		updateBoxes();
	}
	
	public void onSawBoxAction(ActionEvent event)
	{
		engine.setOscillatorType(Oscillator.TYPE_SAW);
		updateBoxes();
	}
	
	public void onSquareBoxAction(ActionEvent event)
	{
		engine.setOscillatorType(Oscillator.TYPE_SQUARE);
		updateBoxes();
	}
	
	public void onTriangleBoxAction(ActionEvent event)
	{
		//engine.setOscillatorType(Oscillator.TYPE_SAW);
		updateBoxes();
	}
	
	private void updateBoxes()
	{
		if (engine.getOscillatorType() == Oscillator.TYPE_SINE)
		{
			sineBox.setSelected(true);
			sawBox.setSelected(false);
			squareBox.setSelected(false);
			triangleBox.setSelected(false);
		}
		
		else if (engine.getOscillatorType() == Oscillator.TYPE_SAW)
		{
			sawBox.setSelected(true);
			sineBox.setSelected(false);
			squareBox.setSelected(false);
			triangleBox.setSelected(false);
		}
		
		else if (engine.getOscillatorType() == Oscillator.TYPE_SQUARE)
		{
			sawBox.setSelected(false);
			sineBox.setSelected(false);
			squareBox.setSelected(true);
			triangleBox.setSelected(false);
		}
	}
	
	public void setMainPane(FXMLLoader loader)
	{
		this.loader = loader;
		
		sineBox = (CheckBox) loader.getNamespace().get("sineBox");
		sawBox = (CheckBox) loader.getNamespace().get("sawBox");
		triangleBox = (CheckBox) loader.getNamespace().get("triangleBox");
		squareBox = (CheckBox) loader.getNamespace().get("squareBox");	
		
		updateBoxes();
	}

}


