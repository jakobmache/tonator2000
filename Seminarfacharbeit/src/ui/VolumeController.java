package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import engine.SynthesizerEngine;

public class VolumeController extends ModuleController
{
	
	private SynthesizerEngine engine;
	
	@FXML
	private Slider volumeSlider;
	
	public VolumeController(SynthesizerEngine engine)
	{
		super(engine, -1);
		this.engine = engine;
	}
	
	public void init()
	{
		volumeSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{
			engine.getOutputModule().setVolume(newValue.floatValue());
		});

	}

	@Override
	protected void update() 
	{	
		//Fake 
	}

	@Override
	public void loadData() 
	{	
		//Fake
	}

	@Override
	public void setModuleEnabled(boolean value) 
	{
		//TODO Fix
	}

	@Override
	public void setChildNodesEnabled(boolean value) 
	{
		// TODO Auto-generated method stub
	}
	
	

}
