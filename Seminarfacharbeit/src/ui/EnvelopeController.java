package ui;

import containers.OscillatorContainer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import modules.Envelope;
import engine.ModuleContainer;
import engine.SynthesizerEngine;

public class EnvelopeController 
{

	private SynthesizerEngine parent;

	@FXML private Slider attackSlider;
	@FXML private Slider decaySlider;
	@FXML private Slider sustainSlider;
	@FXML private Slider releaseSlider;

	@FXML private TextField attackInput;
	@FXML private TextField decayInput;
	@FXML private TextField sustainInput;
	@FXML private TextField releaseInput;

	public EnvelopeController(SynthesizerEngine parent)
	{
		this.parent = parent;
	}

	public void init()
	{
		attackSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{
			for (ModuleContainer container:parent.getInputController().getAllModules().values())
			{
				Envelope envelope = ((OscillatorContainer) container).getEnvelope();
				envelope.setAttackTime((float) newValue.doubleValue()); 
				attackInput.setText(newValue.toString());
			}
		});

		decaySlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{
			for (ModuleContainer container:parent.getInputController().getAllModules().values())
			{
				Envelope envelope = ((OscillatorContainer) container).getEnvelope();
				envelope.setDecayTime((float) newValue.doubleValue()); 
				decayInput.setText(newValue.toString());
			}
		});

		sustainSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{
			for (ModuleContainer container:parent.getInputController().getAllModules().values())
			{
				Envelope envelope = ((OscillatorContainer) container).getEnvelope();
				envelope.setSustainLevel((float) newValue.doubleValue()); 
				sustainInput.setText(newValue.toString());
			}
		});

		releaseSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{
			for (ModuleContainer container:parent.getInputController().getAllModules().values())
			{
				Envelope envelope = ((OscillatorContainer) container).getEnvelope();
				envelope.setReleaseTime((float) newValue.doubleValue()); 
				releaseInput.setText(newValue.toString());
			}
		});

	}

	public void onAttackInputAction(ActionEvent event)
	{
		double value = Double.valueOf(attackInput.getText());
		attackSlider.setValue(value);
	}

	public void onDecayInputAction(ActionEvent event)
	{
		double value = Double.valueOf(decayInput.getText());
		decaySlider.setValue(value);
	}

	public void onSustainInputAction(ActionEvent event)
	{
		double value = Double.valueOf(sustainInput.getText());
		sustainSlider.setValue(value);
	}

	public void onReleaseInputAction(ActionEvent event)
	{
		double value = Double.valueOf(releaseInput.getText());
		releaseSlider.setValue(value);
	}





}
