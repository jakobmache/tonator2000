package ui;

import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import modules.Ids;
import modules.InputController;
import engine.SynthesizerEngine;

public class EnvelopeController extends ModuleController
{
	@FXML private Slider attackSlider;
	@FXML private Slider decaySlider;
	@FXML private Slider sustainSlider;
	@FXML private Slider releaseSlider;

	@FXML private TextField attackInput;
	@FXML private TextField decayInput;
	@FXML private TextField sustainInput;
	@FXML private TextField releaseInput;

	private Slider sliderToUpdate;

	private InputController controller;

	private Map<Slider, TextField> inputs = new HashMap<Slider, TextField>();
	private Map<Slider, Integer> ids = new HashMap<Slider, Integer>();

	public EnvelopeController(SynthesizerEngine parent)
	{
		super(parent);
		controller = parent.getInputController();
	}

	public void init()
	{
		inputs.put(attackSlider, attackInput);
		inputs.put(decaySlider, decayInput);
		inputs.put(releaseSlider, releaseInput);
		inputs.put(sustainSlider, sustainInput);

		ids.put(attackSlider, Ids.ID_CONSTANT_ATTACK_1);
		ids.put(decaySlider, Ids.ID_CONSTANT_DECAY_1);
		ids.put(releaseSlider, Ids.ID_CONSTANT_RELEASE_1);
		ids.put(sustainSlider, Ids.ID_CONSTANT_SUSTAIN_1);
		
		attackSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{
			sliderToUpdate = attackSlider;
			update();
		});

		decaySlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{
			sliderToUpdate = decaySlider;
			update();
		});

		sustainSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{
			sliderToUpdate = sustainSlider;
			update();
		});

		releaseSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{
			sliderToUpdate = releaseSlider;
			update();
		});

		for (Slider slider:inputs.keySet())
		{
			TextField input = inputs.get(slider);
			float value = controller.getPreset().getParam(ids.get(slider));
			input.setText(Float.toString(Math.round(value * 100f) / 100f));
			slider.setValue(value);
		}
	}

	@Override
	protected void update() 
	{
		TextField input = inputs.get(sliderToUpdate);

		float value = (float) sliderToUpdate.getValue();
		input.setText(Float.toString(Math.round(value * 100f) / 100f));
		
		int id = ids.get(sliderToUpdate);
		controller.updatePresetValue(id, value);
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
