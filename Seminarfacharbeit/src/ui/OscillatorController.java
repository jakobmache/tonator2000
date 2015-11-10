package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import modules.Ids;
import modules.InputController;
import modules.Oscillator;
import engine.Module;
import engine.ModuleContainer;
import engine.SynthesizerEngine;

public class OscillatorController extends ModuleController
{
	@FXML
	private CheckBox sineBox;
	@FXML
	private CheckBox sawBox;
	@FXML
	private CheckBox triangleBox;
	@FXML
	private CheckBox squareBox;
	
	private int type;
	
	private InputController controller;
	
	public OscillatorController(SynthesizerEngine engine, int id)
	{
		super(engine, id);
		controller = engine.getInputController();
		type = (int) controller.getPreset().getParam(Ids.ID_CONSTANT_OSCITYPE_1);
	}

	public void onSineBoxAction(ActionEvent event)
	{
		type = Oscillator.TYPE_SINE;
		update();
	}
	
	public void onSawBoxAction(ActionEvent event)
	{
		type = Oscillator.TYPE_SAW;
		update();
	}
	
	public void onSquareBoxAction(ActionEvent event)
	{
		type = Oscillator.TYPE_SQUARE;
		update();
	}
	
	public void onTriangleBoxAction(ActionEvent event)
	{
		//engine.setOscillatorType(Oscillator.TYPE_SAW);
		update();
	}
	
	@Override
	protected void update()
	{		
		if (type == Oscillator.TYPE_SINE)
		{
			sineBox.setSelected(true);
			sawBox.setSelected(false);
			squareBox.setSelected(false);
			triangleBox.setSelected(false);
		}
		
		else if (type == Oscillator.TYPE_SAW)
		{
			sawBox.setSelected(true);
			sineBox.setSelected(false);
			squareBox.setSelected(false);
			triangleBox.setSelected(false);
		}
		
		else if (type == Oscillator.TYPE_SQUARE)
		{
			sawBox.setSelected(false);
			sineBox.setSelected(false);
			squareBox.setSelected(true);
			triangleBox.setSelected(false);
		}
		
		controller.updatePresetValue(Ids.ID_CONSTANT_OSCITYPE_1, type);
	}
	
	public void setMainPane(FXMLLoader loader)
	{
		sineBox = (CheckBox) loader.getNamespace().get("sineBox");
		sawBox = (CheckBox) loader.getNamespace().get("sawBox");
		triangleBox = (CheckBox) loader.getNamespace().get("triangleBox");
		squareBox = (CheckBox) loader.getNamespace().get("squareBox");	
		
		update();
	}

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
		//triangleBox.setDisable(value);
		sineBox.setDisable(value);
		squareBox.setDisable(value);
		sawBox.setDisable(value);
	}



}


