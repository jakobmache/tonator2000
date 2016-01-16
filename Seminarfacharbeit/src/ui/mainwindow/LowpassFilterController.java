package ui.mainwindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import engine.Module;
import engine.ModuleContainer;
import engine.SynthesizerEngine;

public class LowpassFilterController extends ModuleController
{
	
	@FXML
	private Slider cutoffSlider;
	@FXML
	private Slider resonanceSlider;
	@FXML
	private TextField cutoffInput;
	@FXML
	private TextField resonanceInput;
	
	private int cutoffId;
	private int resonanceId;

	public LowpassFilterController(SynthesizerEngine parent, int moduleId, int cutoffId, int resonanceId)
	{
		super(parent, moduleId);
		this.cutoffId = cutoffId;
		this.resonanceId = resonanceId;
	}
	
	public void init()
	{
		cutoffSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{
			update();
		});
		
		resonanceSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{
			update();
		});
		
		loadData();
	}
	
	public void loadData()
	{
		float cutoff = parent.getProgramManager().getInstrumentPreset(currProgram).getParam(cutoffId);
		cutoffSlider.setValue(cutoff);
		cutoffInput.setText(Float.toString(Math.round(cutoff * 100f) / 100f));
		
		float resonance = parent.getProgramManager().getInstrumentPreset(currProgram).getParam(resonanceId);
		resonanceSlider.setValue(resonance);
		resonanceInput.setText(Float.toString(Math.round(resonance * 100f) / 100f));
	}
	
	public void onCutoffInputAction(ActionEvent event)
	{
		double value = Double.valueOf(cutoffInput.getText());
		cutoffSlider.setValue(value);
		update();
	}
	
	public void onResonanceInputAction(ActionEvent event)
	{
		double value = Double.valueOf(resonanceInput.getText());
		resonanceSlider.setValue(value);
		update();
	}

	@Override
	protected void update() 
	{
		float cutoff = (float) cutoffSlider.getValue();
		float resonance = (float) resonanceSlider.getValue();
		
		parent.getProgramManager().updateInstrumentPresetValue(currProgram, cutoffId, cutoff);
		parent.getProgramManager().updateInstrumentPresetValue(currProgram, resonanceId, resonance);
		
		cutoffInput.setText(Float.toString(Math.round(cutoff * 100f) / 100f));
		resonanceInput.setText(Float.toString(Math.round(resonance * 100f) / 100f));
	}

	@Override
	public void setModuleEnabled(boolean value) 
	{
		for (ModuleContainer container:parent.getInputController().getAllContainers())
		{
			Module osci = container.findModuleById(id);
			osci.setEnabled(value);
		}
	}

	@Override
	public void setChildNodesEnabled(boolean value) 
	{
		cutoffInput.setDisable(value);
		cutoffSlider.setDisable(value);
		resonanceInput.setDisable(value);
		resonanceSlider.setDisable(value);
	}


}
