package ui;

import engine.SynthesizerEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import modules.Constant;
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
	private Constant cutoffConstant;
	
	private SynthesizerEngine parent;
	
	public LowpassFilterController(SynthesizerEngine parent, LowpassFilter filter, Constant cutoffInput)
	{
		this.filter = filter;
		this.cutoffConstant = cutoffInput;
	}
	
	public void init()
	{
		cutoffSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{cutoffConstant.setValue(newValue.shortValue());
		cutoffInput.setText(newValue.toString());});
		
		resonanceSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{filter.setResonance(newValue.doubleValue());
		resonanceInput.setText(newValue.toString());});
		
		cutoffSlider.setValue(filter.getCutoffFrequency());
		resonanceSlider.setValue(filter.getResonance());
		
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
