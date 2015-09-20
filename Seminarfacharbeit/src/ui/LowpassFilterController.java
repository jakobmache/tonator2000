package ui;

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
	private TextField cutoffInput;
	
	private LowpassFilter filter;
	
	public LowpassFilterController(LowpassFilter filter)
	{
		this.filter = filter;
	}
	
	public void init()
	{
		cutoffSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{filter.setCutoffFrequency(newValue.doubleValue());
		cutoffInput.setText(newValue.toString());});
		
		cutoffInput.setText(String.valueOf(cutoffSlider.getValue()));
	}
	
	public void onCutoffInputAction(ActionEvent event)
	{
		double value = Double.valueOf(cutoffInput.getText());
		cutoffSlider.setValue(value);
		filter.setCutoffFrequency(value);
	}


}
