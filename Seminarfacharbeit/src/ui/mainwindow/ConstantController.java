package ui.mainwindow;

import engine.SynthesizerEngine;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class ConstantController extends ModuleController{

	@FXML
	protected Slider slider;
	
	@FXML 
	protected TextField input;
	
	private float minValue;
	private float maxValue;
	private float defaultValue;
	
	
	public ConstantController(SynthesizerEngine parent, int id, float minValue, float maxValue, float defaultValue) 
	{
		super(parent, id);
		
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.defaultValue = defaultValue;
	}

	@Override
	protected void update() 
	{
		float value = (float) slider.getValue();
		parent.getProgramManager().updateInstrumentPresetValue(currProgram, id, value);
		input.setText(Float.toString(Math.round(value * 100f) / 100f));
	}

	@Override
	public void init() 
	{
		slider.setMin(minValue);
		slider.setMax(maxValue);
		slider.setValue(defaultValue);
		
		update();
		
		slider.valueProperty().addListener((observableValue, newValue, oldValue) -> 
		{
			update();
		});
	}

	@Override
	public void loadData() 
	{
		update();
	}

	@Override
	public void setModuleEnabled(boolean value) 
	{
		
	}

	@Override
	public void setChildNodesEnabled(boolean value)
	{
		
	}

}
