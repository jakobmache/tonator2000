package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import containers.ContainerPreset;
import resources.Strings;
import midi.MidiUtils;
import engine.SynthesizerEngine;

public class MenuController 
{

	private MainApplication parent;

	private SynthesizerEngine engine;
	private List<Info> deviceInfos;

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
	private CheckMenuItem monoMenuItem;
	@FXML
	private CheckMenuItem stereoMenuItem;

	@FXML
	private MenuItem changeEngineStateMenuItem;

	public MenuController(SynthesizerEngine engine, MainApplication parent)
	{
		this.engine = engine;
		this.parent = parent;
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
			midiSelectionStage.setTitle("MIDI-Gerï¿½t auswï¿½hlen");
			midiSelectionStage.setScene(new Scene(root));

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

	public void loadData()
	{
		int numChannels = engine.getNumChannels();
		if (numChannels == 1)
		{
			stereoMenuItem.setSelected(false);
			monoMenuItem.setSelected(true);
		}

		else {
			stereoMenuItem.setSelected(true);
			monoMenuItem.setSelected(false);;
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
			e.printStackTrace();
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
		System.out.println("Engine menu action!");
		if (engine.isRunning())
			changeEngineStateMenuItem.setText("Pausieren");
		else 
			changeEngineStateMenuItem.setText("Starten");

	}

	public void onSetNumberChannelsAction(ActionEvent event) throws LineUnavailableException
	{	
		if ((event.getSource() == monoMenuItem) && (monoMenuItem.isSelected()))
		{
			engine.setNumChannels(1);
			stereoMenuItem.setSelected(false);
		}

		else if ((event.getSource() == monoMenuItem) && !(monoMenuItem.isSelected()))
		{
			engine.setNumChannels(1);
			stereoMenuItem.setSelected(false);
			monoMenuItem.setSelected(true);
		}

		else if ((event.getSource() == stereoMenuItem) && (stereoMenuItem.isSelected()))
		{
			engine.setNumChannels(2);
			monoMenuItem.setSelected(false);
		}
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

	public void onSelectMidiFileAction(ActionEvent event)
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("MIDI-Datei auswählen");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("MIDI-Dateien", "*mid"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("MIDI-Dateien", "*midi"));

		File midiFile = fileChooser.showOpenDialog(parent.getPrimaryStage());
		try {
			engine.getMidiPlayer().loadMidiFile(midiFile);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onPlayMidiFileAction(ActionEvent event)
	{
		engine.getMidiPlayer().startPlayingMidiFile();
	}
	
	public void onPauseMidiFileAction(ActionEvent event)
	{
		engine.getMidiPlayer().stopPlayingMidiFile();
	}

	public void onResetMidiAction(ActionEvent event)
	{
		engine.reset();
	}
	
	public void onSelectCurrentChannelAction(ActionEvent event)
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

			for (int i = 0; i < engine.getInputController().getNumMidiChannels(); i++)
				availableChannelsBox.getItems().add(i + " - " + MidiUtils.channelNames.get(i));
			
			availableChannelsBox.getSelectionModel().select(parent.getCurrChannel());

			channelSelectionStage.show();

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public void onConfirmCurrentChannelAction(ActionEvent event)
	{
		parent.setCurrChannel(availableChannelsBox.getSelectionModel().getSelectedIndex());
		channelSelectionStage.close();
		parent.updateStatusBar();
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
			try {
				engine.getInputController().getPreset(parent.getCurrChannel()).writeToFile(file.getPath());
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
				engine.getInputController().setPreset(new ContainerPreset(file.getPath()), parent.getCurrChannel());
				parent.update();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


}
