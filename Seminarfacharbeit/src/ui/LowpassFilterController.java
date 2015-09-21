package ui;

import engine.SynthesizerEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import modules.LowpassFilter;

public class LowpassFilterController 
{
	
	@FXML
	private Slider cutoffSlider;
	@FXML
	private Slider resonanceSlider;
	@FXML
	private TextField cutoffInput;
	@FXML
	private TextField resonanceInput;
	
	private LowpassFilter filter;
	
	private SynthesizerEngine parent;
	
	public LowpassFilterController(SynthesizerEngine parent, LowpassFilter filter)
	{
		this.filter = filter;
	}
	
	public void init()
	{
		cutoffSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{filter.setCutoffFrequency(newValue.doubleValue());
		cutoffInput.setText(newValue.toString());});
		
		resonanceSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{filter.setResonance(newValue.doubleValue());
		resonanceInput.setText(newValue.toString());});
		
		cutoffInput.setText(String.valueOf(cutoffSlider.getValue()));
		cutoffSlider.setMax(10000);
	}
	
	public void onCutoffInputAction(ActionEvent event)
	{
		double value = Double.valueOf(cutoffInput.getText());
		cutoffSlider.setValue(value);
		filter.setCutoffFrequency(value);
	}
	
	public void onResonanceInputAction(ActionEvent event)
	{
		double value = Double.valueOf(resonanceInput.getText());
		resonanceSlider.setValue(value);
		filter.setResonance(value);
	}


}
