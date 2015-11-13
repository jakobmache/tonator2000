package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import modules.listener.EngineListener;
import engine.SynthesizerEngine;

public class StatusBarController implements EngineListener
{
	
	private SynthesizerEngine engine;
	private MainApplication parent;
	
	@FXML
	private Label engineRunningLabel;
	@FXML
	private Label bufferTimeLabel;
	@FXML
	private Label samplingRateLabel;
	@FXML
	private Label currChannelLabel;
	@FXML
	private Label midiDeviceLabel;
	
	public StatusBarController(SynthesizerEngine engine, MainApplication parent)
	{
		this.engine = engine;
		this.parent = parent;
		engine.addListener(this);
	}
	
	public void updateStatusBar()
	{
		bufferTimeLabel.setText("Latenz: " + String.valueOf(engine.getBufferTime()) + "s");
		samplingRateLabel.setText("Samplingrate: " + String.valueOf(engine.getSamplingRate()));
		currChannelLabel.setText("MIDI-Kanal: " + String.valueOf(parent.getCurrChannel()));
		
		if (engine.getConnectedMidiDevice() == null)
			midiDeviceLabel.setText("Kein MIDI-Gerät verbunden!");
		else 
			midiDeviceLabel.setText("MIDI-Gerät: " + engine.getConnectedMidiDevice().getDeviceInfo().getName());
		
		if (engine.isRunning())
			engineRunningLabel.setText("Engine läuft!");
		else 
			engineRunningLabel.setText("Engine gestoppt");
		
	}

	@Override
	public void onValueChanged() 
	{
		updateStatusBar();
	}

}
