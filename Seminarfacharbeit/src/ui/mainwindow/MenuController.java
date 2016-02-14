package ui.mainwindow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import midi.MidiUtils;
import resources.Strings;
import ui.editor.SynthesizerEditor;
import ui.utils.NumberInputDialog;
import containers.ContainerPreset;
import engine.ProgramManager;
import engine.SynthesizerEngine;

public class MenuController 
{
	private MainApplication parent;

	private SynthesizerEngine engine;
	private List<Info> deviceInfos;

	private Stage midiPlayerStage;
	private Stage midiLoggerStage;

	private MidiPlayerController midiPlayerController;

	@FXML
	private MenuItem changeEngineStateMenuItem;

	public MenuController(SynthesizerEngine engine, MainApplication parent)
	{
		this.engine = engine;
		this.parent = parent;

		initMidiPlayer();
		initMidiLogger();
	}

	private void initMidiPlayer()
	{
		midiPlayerStage = new Stage();
		midiPlayerStage.initOwner(parent.getPrimaryStage());

		BorderPane layout = null;
		try 
		{
			MidiPlayerController controller = new MidiPlayerController(engine, midiPlayerStage, parent.getPrimaryStage(), parent);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("fxml/MidiPlayerLayout.fxml"));

			loader.setController(controller);
			layout = (BorderPane) loader.load();

			midiPlayerController = controller;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		Scene scene = new Scene(layout);
		midiPlayerStage.setScene(scene);

		URL iconUrl = getClass().getClassLoader().getResource("resources/icon.png");
		midiPlayerStage.getIcons().add(new Image(iconUrl.toString()));

		midiPlayerStage.setTitle(Strings.MIDI_PLAYER_TITLE);

		midiPlayerStage.setOnHidden((event) ->
		{
			midiPlayerStage.close();
			engine.getMidiPlayer().stopPlayingMidiFile();
			event.consume();
		});
	}
	
	private void initMidiLogger()
	{
		midiLoggerStage = new Stage();
		midiLoggerStage.initOwner(parent.getPrimaryStage());
		
		Scene scene = new Scene(new MidiLoggerUI(engine.getMidiLogger()));
		midiLoggerStage.setScene(scene);
		
		midiLoggerStage.setTitle(Strings.MIDI_LOGGER_TITLE);
		URL iconUrl = getClass().getClassLoader().getResource("resources/icon.png");
		midiLoggerStage.getIcons().add(new Image(iconUrl.toString()));
	}

	private ObservableList<String> getMidiDevices()
	{
		deviceInfos = MidiUtils.getAvailableInputDevices();
		ObservableList<String> devicesString = FXCollections.observableArrayList();

		for (Info info:deviceInfos)
		{
			devicesString.add(info.getName() + " - " + info.getDescription());
		}
		return devicesString;
	}

	public void onSelectMidiDeviceAction(ActionEvent event)
	{
		List<String> devices = getMidiDevices();

		if (!devices.isEmpty())
		{
			ChoiceDialog<String> dialog = new ChoiceDialog<String>(devices.get(0), devices);
			dialog.initOwner(parent.getPrimaryStage());
			dialog.setTitle(Strings.MIDI_DEVICE_DIALOG_TITLE);
			dialog.setHeaderText(Strings.MIDI_DEVICE_DIALOG_HEADER);
			dialog.setContentText(Strings.MIDI_DEVICE_DIALOG_TEXT);
			String result = dialog.showAndWait().get();
			int index = devices.indexOf(result);

			try 
			{
				MidiDevice device = MidiSystem.getMidiDevice(deviceInfos.get(index));
				engine.connectMidiDevice(device);
				device.open();
			} 
			catch (MidiUnavailableException e) 
			{
				Alert alert = UiUtils.generateExceptionDialog(parent.getPrimaryStage(), e, Strings.ERROR_TITLE, 
						Strings.ERROR_HEADERS[Strings.ERROR_CONNECT_MIDI_DEVICE], Strings.ERROR_EXPLANATIONS[Strings.ERROR_CONNECT_MIDI_DEVICE]);
				alert.showAndWait();
			}
		}
		else
		{
			Alert alert = UiUtils.generateAlert(parent.getPrimaryStage(), AlertType.ERROR, Strings.ERROR_TITLE, 
					Strings.ERROR_HEADERS[Strings.ERROR_NO_MIDI_DEVICE_AVAILABLE], Strings.ERROR_EXPLANATIONS[Strings.ERROR_NO_MIDI_DEVICE_AVAILABLE]);
			alert.showAndWait();
		}
	}

	public void onSetSampleRateMenuAction(ActionEvent event)
	{
		NumberInputDialog dialog = new NumberInputDialog(parent.getPrimaryStage(), Strings.SAMPLERATE_DIALOG_TITLE, 
				Strings.SAMPLERATE_DIALOG_HEADER, Strings.SAMPLINGRATE_DIALOG_TEXT, 1, 100000);
		String result = dialog.showAndWait().get();
		try 
		{
			engine.setSamplingRate(Float.valueOf(result));
		}
		catch (LineUnavailableException e) 
		{
			Alert alert = UiUtils.generateExceptionDialog(parent.getPrimaryStage(), e, Strings.ERROR_TITLE, Strings.ERROR_HEADERS[Strings.ERROR_AUDIO], 
					Strings.ERROR_EXPLANATIONS[Strings.ERROR_AUDIO]);
			alert.showAndWait();
		}
	}


	public void onSetBufferTimeMenuAction(ActionEvent event)
	{
		NumberInputDialog dialog = new NumberInputDialog(parent.getPrimaryStage(), Strings.BUFFERTIME_DIALOG_TITLE, 
				Strings.BUFFERTIME_DIALOG_HEADER, Strings.BUFFERTIME_DIALOG_TEXT, 1, (int) (SynthesizerEngine.MAX_BUFFERTIME * 1000));
		String result = dialog.showAndWait().get();
		try 
		{
			engine.setBufferTime(Double.valueOf(result) / 1000);
		}
		catch (LineUnavailableException e) 
		{
			Alert alert = UiUtils.generateExceptionDialog(parent.getPrimaryStage(), e, Strings.ERROR_TITLE, Strings.ERROR_HEADERS[Strings.ERROR_AUDIO], 
					Strings.ERROR_EXPLANATIONS[Strings.ERROR_AUDIO]);
			alert.showAndWait();
		}
	}


	public void onEngineMenuAction()
	{
		if (engine.isRunning())
			changeEngineStateMenuItem.setText("Pausieren");
		else 
			changeEngineStateMenuItem.setText("Starten");

	}

	public void onChangeEngineStateAction(ActionEvent event)
	{
		MenuItem source = (MenuItem) event.getSource();
		if (engine.isRunning())
		{
			engine.stopAudio();
			source.setText("Starten");		
		}

		else 
		{
			engine.run();
			source.setText("Pausieren");
		}
	}

	//MIDI-Handler

	public void onOpenMidiPlayer(ActionEvent event)
	{
		if (!midiPlayerStage.isShowing())
		{
			midiPlayerStage.show();
		}
		else
		{
			midiPlayerStage.requestFocus();
		}
	}
	
	public void onNoiseHelp(ActionEvent event)
	{
		Alert alert = UiUtils.generateAlert(parent.getPrimaryStage(), AlertType.WARNING, Strings.NOISE_HELP_DIALOG_TITLE, Strings.NOISE_HELP_DIALOG_HEADER, Strings.NOISE_HELP_DIALOG_TEXT);
		Image image = new Image(getClass().getClassLoader().getResource("resources/icon2.JPG").toString(), 50, 50, false, false);
		ImageView view = new ImageView(image);
		alert.setGraphic(view);
		alert.showAndWait();
	}

	public void onResetMidiAction(ActionEvent event)
	{
		engine.reset();
	}

	public void onSelectCurrentProgramAction(ActionEvent event)
	{
		ChoiceDialog<String> dialog = new ChoiceDialog<String>();
		dialog.initOwner(parent.getPrimaryStage());
		dialog.setTitle(Strings.CURR_INSTRUMENT_DIALOG_TITLE);
		dialog.setHeaderText(Strings.CURR_INSTRUMENT_DIALOG_HEADER);
		dialog.setContentText(Strings.CURR_INSTRUMENT_DIALOG_TEXT);

		for (int i = 0; i < ProgramManager.NUM_PROGRAMS; i++)
			dialog.getItems().add(engine.getProgramManager().getInstrumentName(i));

		dialog.setSelectedItem(dialog.getItems().get(parent.getCurrProgram()));
		
		String result = dialog.showAndWait().get();
		parent.showProgram(dialog.getItems().indexOf(result));
		parent.updateStatusBar();
	}

	public void onAssignPresetToChannelAction(ActionEvent event)
	{
		ChannelInstrumentAlert alert = new ChannelInstrumentAlert(AlertType.CONFIRMATION, engine);
		alert.initOwner(parent.getPrimaryStage());
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			int[] choices = alert.getChoices();
			for (int i = 0; i < choices.length; i++)
			{
				engine.getInputController().setChannelProgram(i, choices[i]);
			}
		}
	}

	public void onSavePreset(ActionEvent event)
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Preset speichern");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("XML-Dateien", "*.xml"));
		fileChooser.setInitialFileName(Strings.SAVE_PRESET_FILE_NAME);

		File file = fileChooser.showSaveDialog(parent.getPrimaryStage());

		if (file != null)
		{
			savePreset(engine.getProgramManager().getInstrumentPreset(parent.getCurrProgram()), file.getPath());
		}
	}

	public void onSaveAllMenuPresets(ActionEvent event)
	{
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Ordner ausw" + Strings.ae + "hlen");
		File selectedDirectory = chooser.showDialog(parent.getPrimaryStage());

		if (selectedDirectory != null)
		{
			for (int program = 0; program < ProgramManager.NUM_PROGRAMS; program++)
			{
				String path = selectedDirectory.getPath() + "\\" + Integer.toString(program) + ".xml";
				savePreset(engine.getProgramManager().getInstrumentPreset(program), path);
			}
		}
	}

	public void onLoadAllPresets(ActionEvent event)
	{
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Ordner ausw" + Strings.ae + "hlen");
		File selectedDirectory = chooser.showDialog(parent.getPrimaryStage());

		if (selectedDirectory != null)
		{
			ProgramManager manager = engine.getProgramManager();
			for (int program = 0; program < ProgramManager.NUM_PROGRAMS; program++)
			{
				String path = selectedDirectory.getPath() + "\\" + Integer.toString(program) + ".xml";
				try 
				{
					manager.setInstrumentPreset(program, new ContainerPreset(path));
				} 
				catch (Exception ex)
				{
					Alert alert = UiUtils.generateExceptionDialog(parent.getPrimaryStage(), ex, Strings.ERROR_TITLE, Strings.ERROR_HEADERS[Strings.ERROR_LOAD_ALL_PRESETS],
							Strings.ERROR_EXPLANATIONS[Strings.ERROR_LOAD_ALL_PRESETS]);
					alert.showAndWait();
					break;
				}
			}
		}
	}

	private void savePreset(ContainerPreset preset, String path)
	{
		try 
		{
			preset.writeToFile(path);
		} 
		catch (FileNotFoundException e) 
		{
			Alert alert = UiUtils.generateExceptionDialog(parent.getPrimaryStage(), e, Strings.ERROR_TITLE, 
					Strings.ERROR_HEADERS[Strings.ERROR_FILE_NOT_FOUND], Strings.ERROR_EXPLANATIONS[Strings.ERROR_FILE_NOT_FOUND]);
			alert.showAndWait();
		} 
		catch (ParserConfigurationException e) 
		{
			Alert alert = UiUtils.generateExceptionDialog(parent.getPrimaryStage(), e, Strings.ERROR_TITLE, 
					Strings.ERROR_HEADERS[Strings.ERROR_WRITING_FILE], Strings.ERROR_EXPLANATIONS[Strings.ERROR_WRITING_FILE]);
			alert.showAndWait();
		} 
		catch (TransformerException e) 
		{
			Alert alert = UiUtils.generateExceptionDialog(parent.getPrimaryStage(), e, Strings.ERROR_TITLE, 
					Strings.ERROR_HEADERS[Strings.ERROR_WRITING_FILE], Strings.ERROR_EXPLANATIONS[Strings.ERROR_WRITING_FILE]);
			alert.showAndWait();
		}
	}

	public void onLoadPreset(ActionEvent event)
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Preset laden");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("XML-Dateien", "*.xml"));

		File file = fileChooser.showOpenDialog(parent.getPrimaryStage());

		if (file != null)
		{
			try 
			{
				engine.getProgramManager().setInstrumentPreset(parent.getCurrProgram(), new ContainerPreset(file.getPath()));
				parent.updateModules();
			} 
			catch (NullPointerException e)
			{
				Alert alert = UiUtils.generateAlert(parent.getPrimaryStage(), AlertType.WARNING, Strings.WARNING_TITLE, 
						Strings.ERROR_HEADERS[Strings.ERROR_TOO_LESS_DATA], Strings.ERROR_EXPLANATIONS[Strings.ERROR_TOO_LESS_DATA]);
				alert.showAndWait();
			}
			catch (Exception e) 
			{
				Alert alert = UiUtils.generateExceptionDialog(parent.getPrimaryStage(), e, Strings.ERROR_TITLE, 
						Strings.ERROR_HEADERS[Strings.ERROR_READING_FILE], Strings.ERROR_EXPLANATIONS[Strings.ERROR_READING_FILE]);
				alert.showAndWait();
			}
		}
	}

	public void onAbout(ActionEvent event)
	{
		Alert dialog = UiUtils.generateAlert(parent.getPrimaryStage(), AlertType.INFORMATION, Strings.ABOUT_DIALOG_TITLE, Strings.ABOUT_DIALOG_HEADER, Strings.ABOUT_DIALOG_TEXT);
		dialog.showAndWait();
	}

	public void onAboutLibraries(ActionEvent event)
	{
		Alert dialog = UiUtils.generateAlert(parent.getPrimaryStage(), AlertType.INFORMATION, Strings.LIBRARIES_DIALOG_TITLE, Strings.LIBRARIES_DIALOG_HEADER, Strings.LIBRARIES_DIALOG_TEXT);
		WebView webView = new WebView();
		WebEngine engine = webView.getEngine();

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(MenuController.class.getClassLoader().getResourceAsStream("resources/libraries.html")));
			StringBuilder builder = new StringBuilder();
			String line = reader.readLine();
			while(line != null)
			{
				builder.append(line);
				line = reader.readLine();
			}
			reader.close();

			engine.loadContent(builder.toString());
			dialog.getDialogPane().contentProperty().set(webView);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		dialog.showAndWait();
	}

	public void onSetMaxPolyphony(ActionEvent event)
	{
		NumberInputDialog dialog = new NumberInputDialog(parent.getPrimaryStage(), Strings.POLYPHONY_DIALOG_TITLE, Strings.POLYPHONY_DIALOG_HEADER, Strings.POLYPHONY_DIALOG_TEXT, 1, SynthesizerEngine.MAX_POLYPHONY);
		String result = dialog.showAndWait().get();
		engine.setMaxPolyphony(Integer.valueOf(result));
	}
	
	public void onShowMidiLogger(ActionEvent event)
	{
		if (!midiLoggerStage.isShowing())
		{
			midiLoggerStage.show();
		}
		else
		{
			midiLoggerStage.requestFocus();
		}
	}
	
	//Editor actions
	
	public void onOpenEditor()
	{
		SynthesizerEditor editor = parent.getEditor();
		if (!editor.isShowing())
			editor.show();
	}
	
	public void onResetLayout()
	{
		parent.showStandardConfiguration();
	}

	public MidiPlayerController getMidiPlayerController()
	{
		return midiPlayerController;
	}
	
	public Stage getMidiPlayerStage()
	{
		return midiPlayerStage;
	}

}



