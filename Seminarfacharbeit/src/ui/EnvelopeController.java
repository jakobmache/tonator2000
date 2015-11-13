package ui;

import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import modules.InputController;
import engine.Module;
import engine.ModuleContainer;
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
	
	private int attackId;
	private int decayId;
	private int sustainId;
	private int releaseId;

	private Slider sliderToUpdate;

	private InputController controller;

	private Map<Slider, TextField> inputs = new HashMap<Slider, TextField>();
	private Map<Slider, Integer> ids = new HashMap<Slider, Integer>();

	public EnvelopeController(SynthesizerEngine parent, int attackId, int decayId, int sustainId, int releaseId, int moduleId)
	{
		super(parent, moduleId);
		controller = parent.getInputController();
		
		this.attackId = attackId;
		this.sustainId = sustainId;
		this.releaseId = releaseId;
		this.decayId = decayId;
	}

	public void init()
	{
		inputs.put(attackSlider, attackInput);
		inputs.put(decaySlider, decayInput);
		inputs.put(releaseSlider, releaseInput);
		inputs.put(sustainSlider, sustainInput);

		ids.put(attackSlider, attackId);
		ids.put(decaySlider, decayId);
		ids.put(releaseSlider, releaseId);
		ids.put(sustainSlider, sustainId);
		
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

		loadData();
	}
	
	public void loadData() 
	{
		for (Slider slider:ids.keySet())
		{
			TextField input = inputs.get(slider);
			float value = controller.getPreset(currChannel).getParam(ids.get(slider));
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
		controller.updatePresetValue(currChannel, id, value);
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

	@Override
	public void setModuleEnabled(boolean value) 
	{
		for (ModuleContainer container:parent.getInputController().getAllContainers())
		{
			Module osci = container.findModuleById(id);
			osci.setEnabled(value);
		}
	}

	@Override
	public void setChildNodesEnabled(boolean value) 
	{
		for (Slider slider:inputs.keySet())
		{
			slider.setDisable(value);
		}
		for (TextField textField:inputs.values())
		{
			textField.setDisable(value);
		}
	}
}
