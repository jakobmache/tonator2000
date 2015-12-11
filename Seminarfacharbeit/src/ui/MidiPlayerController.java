package ui;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import resources.Strings;
import engine.SynthesizerEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class MidiPlayerController 
{
	@FXML
	private MenuItem openMenuItem;
	@FXML 
	private MenuItem closeMenuItem;
	
	@FXML
	private Label midiFileLabel;
	
	@FXML
	private Button stopButton;
	@FXML
	private Button pauseButton;
	@FXML
	private Button playButton;
	
	private SynthesizerEngine engine;
	private Stage stage;
	
	public MidiPlayerController(SynthesizerEngine engine, Stage stage, Stage parent)
	{
		this.engine = engine;
		this.stage = stage;
		stage.initOwner(parent);
	}

	
	public void onOpenAction(ActionEvent event)
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("MIDI-Datei auswählen");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("MIDI-Dateien", "*mid"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("MIDI-Dateien", "*midi"));

		File midiFile = fileChooser.showOpenDialog(stage);
		try {
			engine.getMidiPlayer().loadMidiFile(midiFile);
		} catch (MidiUnavailableException e) 
		{
			e.printStackTrace();
		} catch (InvalidMidiDataException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		updateLabel();
	}
	
	public void updateLabel()
	{
		if (engine.getMidiPlayer().getMidiFile() == null)
			midiFileLabel.setText(Strings.NO_MIDI_FILE_LOADED_LABEL);
		else
			midiFileLabel.setText(engine.getMidiPlayer().getMidiFile().getAbsolutePath());
	}
	
	public void onCloseAction(ActionEvent event)
	{
		stage.close();
	}
	
	public void onStopAction(ActionEvent event)
	{
		engine.getMidiPlayer().stopPlayingMidiFile();
	}
	
	public void onPauseAction(ActionEvent event)
	{
		engine.getMidiPlayer().pausePlayingMidiFile();
	}
	
	public void onPlayAction(ActionEvent event)
	{
		engine.getMidiPlayer().startPlayingMidiFile();
	}

}
