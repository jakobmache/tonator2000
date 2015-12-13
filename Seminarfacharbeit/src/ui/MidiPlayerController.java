package ui;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiUnavailableException;

import resources.Strings;
import engine.SynthesizerEngine;

public class MidiPlayerController 
{
	public static final int META_TEXT = 1;
	public static final int META_COPYRIGHT = 2;
	public static final int META_NAME = 3;
	public static final int META_INSTRUMENT = 4;

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

	@FXML
	private TextArea infoArea;

	private SynthesizerEngine engine;
	private Stage stage;
	private MainApplication parent;

	public MidiPlayerController(SynthesizerEngine engine, Stage stage, Stage parent, MainApplication app)
	{
		this.engine = engine;
		this.stage = stage;
		this.parent = app;
		stage.initOwner(parent);
	}


	public void onOpenAction(ActionEvent event)
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("MIDI-Datei auswählen");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("MIDI-Dateien", "*mid"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("MIDI-Dateien", "*midi"));

		File midiFile = fileChooser.showOpenDialog(stage);
		try 
		{
			engine.getMidiPlayer().loadMidiFile(midiFile);
			loadTextAreaData();
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

	public void loadTextAreaData()
	{
		LoadFileTask task = new LoadFileTask(this, engine);
		parent.getStatusBar().textProperty().bind(task.messageProperty());
		parent.getStatusBar().progressProperty().bind(task.progressProperty());
		
		SynthiStatusBar statusBar = parent.getStatusBar();
		task.setOnSucceeded((handler) ->
		{
			statusBar.textProperty().unbind();   
			statusBar.progressProperty().unbind();
			block(false);
		});

		task.setOnCancelled((handler) ->
		{            
			statusBar.textProperty().unbind();   
			statusBar.progressProperty().unbind();
			block(false);
		});

		task.setOnFailed((handler) ->
		{
			statusBar.textProperty().unbind();   
			statusBar.progressProperty().unbind();
			block(false);
		});
		
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
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

	public void block(boolean value)
	{
		playButton.setDisable(value);
		pauseButton.setDisable(value);
		stopButton.setDisable(value);
	}

	public TextArea getTextArea()
	{
		return infoArea;
	}

}

class LoadFileTask extends Task<Void>
{

	private MidiPlayerController controller;
	private TextArea infoArea;
	private SynthesizerEngine engine;


	public LoadFileTask(MidiPlayerController controller, SynthesizerEngine engine)
	{
		this.infoArea = controller.getTextArea();
		this.engine = engine;
		this.controller = controller;
	}

	@Override
	protected Void call() throws Exception 
	{
		System.out.println("call is called!");
		Platform.runLater(() -> {
			controller.block(true);
			infoArea.clear();
		});



		updateMessage("Lade MIDI-Datei...");

		String sep = System.getProperty("line.separator");

		List<MetaMessage> messages = engine.getMidiPlayer().getMetaMessages();

		writeToTextField(Strings.MIDI_PLAYER_INFO);
		for (MetaMessage message:messages)
		{
			if (message.getType() == MidiPlayerController.META_TEXT)
				writeToTextField("\t" + new String(message.getMessage(), StandardCharsets.UTF_8) + sep);	
		}
		writeToTextField(Strings.MIDI_PLAYER_COPYRIGHT);
		for (MetaMessage message:messages)
		{
			if (message.getType() == MidiPlayerController.META_COPYRIGHT)
				writeToTextField("\t" + new String(message.getMessage(), StandardCharsets.UTF_8) + sep);
		}

		writeToTextField(Strings.MIDI_PLAYER_INSTRUMENTS);	
		List<Integer> instruments = engine.getMidiPlayer().getAllPrograms();	
		for (int program:instruments)
		{
			writeToTextField("\t" + engine.getProgramManager().getInstrumentName(program) + sep);
		}
		
		updateMessage("");
		updateProgress(0, 0);
		done();
		return null;
	}

	private void writeToTextField(String text)
	{
		Platform.runLater(() -> 
		{
			infoArea.appendText(text);
		});
	}
}

