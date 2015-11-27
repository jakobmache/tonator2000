package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import engine.SynthesizerEngine;

public class VolumeController 
{
	
	private SynthesizerEngine engine;
	
	@FXML
	private Slider volumeSlider;
	
	public VolumeController(SynthesizerEngine engine)
	{
		this.engine = engine;
	}
	
	public void init()
	{
		volumeSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{
			engine.getOutputModule().setVolume(newValue.floatValue());
		});

	}
	
	

}
