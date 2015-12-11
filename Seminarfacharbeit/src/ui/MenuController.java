package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import midi.MidiUtils;

import org.xml.sax.SAXException;

import resources.Strings;
import containers.ContainerPreset;
import engine.ProgramManager;
import engine.SynthesizerEngine;

public class MenuController 
{

	private MainApplication parent;

	private SynthesizerEngine engine;
	private List<Info> deviceInfos;

	private Stage midiPlayerStage;

	private MidiPlayerController midiPlayerController;

	private Stage midiSelectionStage;
	@FXML
	private ChoiceBox<String> availableMidiDevicesBox;

	private Stage sampleRateStage;	
	@FXML
	private TextField samplingRateInput;

	private Stage bufferTimeStage;
	@FXML
	private TextField bufferTimeInput;

	private Stage channelSelectionStage;
	@FXML 
	private ChoiceBox<String> availableChannelsBox;

	@FXML
	private MenuItem changeEngineStateMenuItem;

	public MenuController(SynthesizerEngine engine, MainApplication parent)
	{
		this.engine = engine;
		this.parent = parent;

		initMidiPlayer();
	}

	private void initMidiPlayer()
	{
		midiPlayerStage = new Stage();

		BorderPane layout = null;
		try 
		{
			MidiPlayerController controller = new MidiPlayerController(engine, midiPlayerStage, parent.getPrimaryStage());
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

		midiPlayerStage.show();

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
		try 
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MenuController.class.getResource("fxml/SelectMidiDeviceLayout.fxml"));

			loader.setController(this);
			VBox root = (VBox) loader.load();

			midiSelectionStage = new Stage();
			midiSelectionStage.setTitle("MIDI-Ger " + Strings.ae + "t ausw" + Strings.ae + "hlen");
			midiSelectionStage.setScene(new Scene(root));
			midiSelectionStage.initStyle(StageStyle.UTILITY);

			ObservableList<String> midiDevices = getMidiDevices();
			availableMidiDevicesBox.getItems().addAll(midiDevices);
			availableMidiDevicesBox.getSelectionModel().selectFirst();

			midiSelectionStage.show();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void onSetSampleRateMenuAction(ActionEvent event)
	{
		try 
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MenuController.class.getResource("fxml/SelectSampleRateLayout.fxml"));

			loader.setController(this);
			VBox root = (VBox) loader.load();

			sampleRateStage = new Stage();
			sampleRateStage.setTitle("Samplingrate auswï¿½hlen");
			sampleRateStage.setScene(new Scene(root));

			sampleRateStage.show();


		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void onSetSampleRateAction(ActionEvent event)
	{
		float samplingRate = Float.valueOf(samplingRateInput.getText());
		try 
		{
			engine.setSamplingRate(samplingRate);
		} 
		catch (LineUnavailableException e) 
		{
			Alert alert = UiUtils.generateExceptionDialog(parent.getPrimaryStage(), e, Strings.ERROR_TITLE, Strings.ERROR_HEADERS[Strings.ERROR_AUDIO], 
					Strings.ERROR_EXPLANATIONS[Strings.ERROR_AUDIO]);
			alert.showAndWait();
		}
		sampleRateStage.close();
	}

	public void onSetBufferTimeMenuAction(ActionEvent event)
	{
		try 
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MenuController.class.getResource("fxml/SelectBufferTimeLayout.fxml"));

			loader.setController(this);
			VBox root = (VBox) loader.load();

			bufferTimeStage = new Stage();
			bufferTimeStage.setTitle("Pufferzeit auswï¿½hlen");
			bufferTimeStage.setScene(new Scene(root));

			bufferTimeStage.show();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void onSetBufferTimeAction(ActionEvent event)
	{
		double bufferTime = Double.valueOf(bufferTimeInput.getText());
		try {
			engine.setBufferTime(bufferTime);
		} catch (LineUnavailableException e) 
		{
			e.printStackTrace();
		} 
		bufferTimeStage.close();
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
			engine.stop();
			source.setText("Starten");		
		}

		else 
		{
			engine.run();
			source.setText("Pausieren");
		}
	}

	//MIDI-Handler

	public void onConnectMidiDeviceAction(ActionEvent event)
	{
		int index = availableMidiDevicesBox.getSelectionModel().getSelectedIndex();

		try 
		{
			MidiDevice device = MidiSystem.getMidiDevice(deviceInfos.get(index));
			engine.connectMidiDevice(device);
			device.open();
		} 
		catch (MidiUnavailableException e) 
		{
			e.printStackTrace();
		}

		midiSelectionStage.close();
	}

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

	public void onResetMidiAction(ActionEvent event)
	{
		engine.reset();
	}

	public void onSelectCurrentProgramAction(ActionEvent event)
	{
		try 
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MenuController.class.getResource("fxml/SelectCurrentChannelLayout.fxml"));

			loader.setController(this);
			VBox root = (VBox) loader.load();

			channelSelectionStage = new Stage();
			channelSelectionStage.setTitle("Akuellen MIDI-Kanal auswählen");
			channelSelectionStage.setScene(new Scene(root));
			channelSelectionStage.initStyle(StageStyle.UTILITY);

			for (int i = 0; i < ProgramManager.NUM_PROGRAMS; i++)
				availableChannelsBox.getItems().add(engine.getProgramManager().getInstrumentName(i));

			availableChannelsBox.getSelectionModel().select(parent.getCurrProgram());

			channelSelectionStage.show();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

	public void onConfirmCurrentProgramAction(ActionEvent event)
	{
		parent.setCurrProgram(availableChannelsBox.getSelectionModel().getSelectedIndex());
		channelSelectionStage.close();
		parent.updateStatusBar();
	}

	public void onAssignPresetToChannelAction(ActionEvent event)
	{
		//TODO:das hier
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
		try {
			preset.writeToFile(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			try {
				engine.getProgramManager().setInstrumentPreset(parent.getCurrProgram(), new ContainerPreset(file.getPath()));
				parent.update();
			} 
			catch (ParserConfigurationException e) 
			{
				e.printStackTrace();
			} 
			catch (SAXException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
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
		NumberInputDialog dialog = new NumberInputDialog(parent.getPrimaryStage(), Strings.POLYPHONY_DIALOG_TITLE, Strings.POLYPHONY_DIALOG_HEADER, Strings.POLYPHONY_DIALOG_TEXT);
		String result = dialog.showAndWait().get();
		engine.setMaxPolyphony(Integer.valueOf(result));
	}

	public MidiPlayerController getMidiPlayerController()
	{
		return midiPlayerController;
	}

}
