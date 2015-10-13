package ui;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import engine.SynthesizerEngine;

public class StatusBarController 
{
	
	private SynthesizerEngine engine;
	
	@FXML
	private Label engineRunningLabel;
	@FXML
	private Label bufferTimeLabel;
	@FXML
	private Label samplingRateLabel;
	@FXML
	private Label numChannelsLabel;
	@FXML
	private Label midiDeviceLabel;
	
	public StatusBarController(SynthesizerEngine engine)
	{
		this.engine = engine;
		engine.getOutputModule().getAudioLine().addLineListener(new AudioLineListener());
	}
	
	public void updateStatusBar()
	{
		bufferTimeLabel.setText("Latenz: " + String.valueOf(engine.getBufferTime()) + "s");
		samplingRateLabel.setText("Samplingrate: " + String.valueOf(engine.getSamplingRate()));
		numChannelsLabel.setText("Anzahl Kanäle: " + String.valueOf(engine.getNumChannels()));
		
		if (engine.getConnectedMidiDevice() == null)
			midiDeviceLabel.setText("Kein MIDI-Gerät verbunden!");
		else 
			midiDeviceLabel.setText("MIDI-Gerät: " + engine.getConnectedMidiDevice().getDeviceInfo().getName());
		
		if (engine.isRunning())
			engineRunningLabel.setText("Engine läuft!");
		else 
			engineRunningLabel.setText("Engine gestoppt");
		
	}
	
	private class AudioLineListener implements LineListener
	{

		@Override
		public void update(LineEvent event) 
		{
			if (event.getType() == LineEvent.Type.STOP)
				System.out.println("Line stopped!");
			else 
				System.out.println(event.getType());
			
		}
		
	}

}
