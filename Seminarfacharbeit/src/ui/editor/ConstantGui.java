package ui.editor;

import engine.Module;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import modules.Constant;
import modules.ModuleType;
import ui.utils.NumberInputField;

public class ConstantGui extends ModuleGuiBackend 
{
	private float maxValue;
	private float minValue;
	private float defaultValue;
	
	private NumberInputField input;
	
	private static int sliderHeight = 100;
	
	public ConstantGui(SynthesizerEditor owner, ModuleType type, String name, float maxValue, float minValue, float defaultValue) 
	{
		super(owner, type, name, sliderHeight);
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.defaultValue = defaultValue;

		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		
//		Slider slider = new Slider(minValue, maxValue, defaultValue);
//		slider.setMaxHeight(sliderHeight);
//		slider.setOrientation(Orientation.VERTICAL);
//		slider.setMin(minValue);
//		slider.setMax(maxValue);
//		vBox.getChildren().add(slider);
		
		input = new NumberInputField(this.maxValue, this.minValue, true);
		input.setText(Float.toString(this.defaultValue));
		vBox.getChildren().add(input);
		
		gui.getChildren().add(vBox);
		
		input.setText(Float.toString(defaultValue));
		input.setLayoutX(0);
		input.setLayoutY(0);
		input.setMaxHeight(2 * radius);
		input.setMaxWidth(width);
		owner.requestFocus();
	}
	
	@Override
	public Module getModule()
	{
		((Constant) module).setValue(Float.valueOf(input.getText()));
		return module;
	}
}
