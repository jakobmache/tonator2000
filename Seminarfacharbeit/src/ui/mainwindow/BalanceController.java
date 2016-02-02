package ui.mainwindow;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import engine.SynthesizerEngine;

//TODO: Modulnamen

public class BalanceController extends ModuleController 
{	
	@FXML
	protected Slider balanceSlider;
	@FXML
	protected TextField balanceInput;
	@FXML
	protected Label module1Label;
	@FXML
	protected Label module2Label;
	
	private int balanceConstantId;
	private int module1Id;
	private int module2Id;
	
	public BalanceController(SynthesizerEngine parent, int id, int module1Id, int module2Id, int balanceId) 
	{
		super(parent, id);
		balanceConstantId = balanceId;
		this.module1Id = module1Id;
		this.module2Id = module2Id;
	}
	
	public void init()
	{
		balanceSlider.valueProperty().addListener((observableValue, oldValue, newValue) ->
		{
			update();
		});
	}

	@Override
	protected void update() 
	{
		float value = (float) balanceSlider.getValue();
		parent.getProgramManager().updateInstrumentPresetValue(currProgram, balanceConstantId, value);
		balanceInput.setText(Float.toString(Math.round(value * 100f) / 100f));
	}

	@Override
	public void loadData() 
	{
		module1Label.setText(parent.getInputController().getSynthesizerContainer().findModuleById(module1Id).getName());
		module2Label.setText(parent.getInputController().getSynthesizerContainer().findModuleById(module2Id).getName());
		float balance = parent.getProgramManager().getInstrumentPreset(currProgram).getParam(balanceConstantId);
		balanceSlider.setValue(balance);
		update();
	}

	@Override
	public void setModuleEnabled(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setChildNodesEnabled(boolean value) {
		// TODO Auto-generated method stub

	}
	
	public void onBalanceInputAction(ActionEvent event)
	{
		double value = Double.valueOf(balanceInput.getText());
		balanceSlider.setValue(value);
		update();
	}

}
