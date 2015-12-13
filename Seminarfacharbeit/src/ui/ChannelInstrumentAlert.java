package ui;

import java.io.IOException;

import com.sun.org.apache.bcel.internal.generic.NEW;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import resources.Strings;
import engine.InputController;
import engine.ProgramManager;
import engine.SynthesizerEngine;

public class ChannelInstrumentAlert extends Alert
{
	private ChoiceBox<Integer> channelBox = null;
	private ChoiceBox<String> programBox = null;
	private SynthesizerEngine engine;
	
	private int[] choices = new int[InputController.NUM_CHANNELS];
	
	private ButtonType reset;
	
	public ChannelInstrumentAlert(AlertType alertType, SynthesizerEngine engine) 
	{
		super(alertType);
		setTitle(Strings.CHANNEL_INSTRUMENT_DIALOG_TITLE);
		setHeaderText(Strings.CHANNEL_INSTRUMENT_DIALOG_HEADER);
		
		this.engine = engine;
		
		initLayout();
		loadData();

		Button resetButton = (Button) getDialogPane().lookupButton(reset);
		resetButton.addEventFilter(ActionEvent.ACTION, (event) ->
		{
			for (Integer channel:channelBox.getItems())
			{
				engine.getInputController().setChannelProgram(channel - 1, InputController.DEFAULT_PROGRAM);
			}
			
			event.consume();
		});
	}
	
	@SuppressWarnings("unchecked")
	private void initLayout()
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ChannelInstrumentAlert.class.getResource("fxml/ChannelInstrumentLayout.fxml"));
		
		Node pane = null;
		try 
		{
			pane = loader.load();
			getDialogPane().contentProperty().set(pane);
			channelBox = (ChoiceBox<Integer>) ((HBox) pane).lookup("#channelBox");
			programBox = (ChoiceBox<String>) ((HBox) pane).lookup("#programBox");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		channelBox.valueProperty().addListener((observable) ->
		{
			programBox.setValue(engine.getProgramManager().getInstrumentName(
					engine.getInputController().getChannelProgram(channelBox.getValue() - 1)));
		});

		
		reset = new ButtonType(Strings.BUTTON_TYPE_RESET_ALL);
		getButtonTypes().add(0, reset);
	}
	
	public void loadData()
	{
		ProgramManager manager = engine.getProgramManager();

		for (int i = 0; i < ProgramManager.NUM_PROGRAMS; i++)
		{
			programBox.getItems().add(manager.getInstrumentName(i));
		}
		
		for (int i = 0; i < InputController.NUM_CHANNELS; i++)
		{
			channelBox.getItems().add(i + 1);
		}
		
		channelBox.getSelectionModel().selectFirst();
	}
	
	public ChoiceBox<Integer> getChannelBox()
	{
		return channelBox;
	}
	
	public ChoiceBox<String> getProgramBox()
	{
		return programBox;
	}
}
