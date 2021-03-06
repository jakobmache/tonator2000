package ui.mainwindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import modules.Oscillator;
import engine.Module;
import engine.ModuleContainer;
import engine.SynthesizerEngine;

public class WaveformController extends ModuleController
{
	@FXML
	private CheckBox sineBox;
	@FXML
	private CheckBox sawBox;
	@FXML
	private CheckBox triangleBox;
	@FXML
	private CheckBox squareBox;
	@FXML
	private CheckBox whiteNoiseBox;
	
	private int currWaveform;
	
	private int waveformId;
	
	public WaveformController(SynthesizerEngine engine, int osciId, int waveformId)
	{
		super(engine, osciId);
		this.waveformId = waveformId;
		currWaveform = (int) parent.getProgramManager().getInstrumentPreset(currProgram).getParam(waveformId);
	}

	public void onSineBoxAction(ActionEvent event)
	{
		currWaveform = Oscillator.TYPE_SINE;
		update();
	}
	
	public void onSawBoxAction(ActionEvent event)
	{
		currWaveform = Oscillator.TYPE_SAW;
		update();
	}
	
	public void onSquareBoxAction(ActionEvent event)
	{
		currWaveform = Oscillator.TYPE_SQUARE;
		update();
	}
	
	public void onTriangleBoxAction(ActionEvent event)
	{
		currWaveform = Oscillator.TYPE_TRI;
		update();
	}
	
	public void onWhiteNoiseBoxAction(ActionEvent event)
	{
		currWaveform = Oscillator.TYPE_WHITE_NOISE;
		update();
	}
	
	@Override
	protected void update()
	{		
		if (currWaveform == Oscillator.TYPE_SINE)
		{
			sineBox.setSelected(true);
			sawBox.setSelected(false);
			squareBox.setSelected(false);
			triangleBox.setSelected(false);
			whiteNoiseBox.setSelected(false);
		}
		
		else if (currWaveform == Oscillator.TYPE_SAW)
		{
			sawBox.setSelected(true);
			sineBox.setSelected(false);
			squareBox.setSelected(false);
			triangleBox.setSelected(false);
			whiteNoiseBox.setSelected(false);
		}
		
		else if (currWaveform == Oscillator.TYPE_SQUARE)
		{
			sawBox.setSelected(false);
			sineBox.setSelected(false);
			squareBox.setSelected(true);
			triangleBox.setSelected(false);
			whiteNoiseBox.setSelected(false);
		}
		
		else if (currWaveform == Oscillator.TYPE_TRI)
		{
			sawBox.setSelected(false);
			sineBox.setSelected(false);
			squareBox.setSelected(false);
			triangleBox.setSelected(true);
			whiteNoiseBox.setSelected(false);
		}
		
		else if (currWaveform == Oscillator.TYPE_WHITE_NOISE)
		{
			sineBox.setSelected(false);
			sawBox.setSelected(false);
			squareBox.setSelected(false);
			triangleBox.setSelected(false);
			whiteNoiseBox.setSelected(true);
		}
		
		parent.getProgramManager().updateInstrumentPresetValue(currProgram, waveformId, currWaveform);
	}
	
	public void init()
	{
		update();
	}

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
		triangleBox.setDisable(value);
		sineBox.setDisable(value);
		squareBox.setDisable(value);
		sawBox.setDisable(value);
	}

	@Override
	public void loadData() 
	{
		currWaveform = (int) parent.getProgramManager().getInstrumentPreset(currProgram).getParam(waveformId);
		update();
	}



}


