package ui.editor;

import engine.Module;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import modules.Constant;
import modules.ModuleType;
import resources.Strings;
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

//		Slider slider = new Slider(minValue, maxValue, defaultValue);
//		slider.setMaxHeight(sliderHeight);
//		slider.setOrientation(Orientation.VERTICAL);
//		slider.setMin(minValue);
//		slider.setMax(maxValue);
//		vBox.getChildren().add(slider);
		
		if (getClass() == ConstantGui.class)
			drawCustomInputs();
		
		owner.requestFocus();
		if (input != null)
			input.setEditable(true);
	}
	
	@Override
	protected void drawInputs()
	{
		
	}

	protected void drawCustomInputs()
	{
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		input = new NumberInputField(this.maxValue, this.minValue, false);
		vBox.getChildren().add(input);
		
		gui.getChildren().add(vBox);
		input.setLayoutX(0);
		input.setLayoutY(0);
		input.setMaxHeight(2 * radius);
		input.setMaxWidth(width);
		
		input.setText(Float.toString(this.defaultValue));
		
		input.textProperty().addListener((observable, newValue, oldValue) -> {
			System.out.println("New text: " + input.getText());
			if (input.isValid())
			{
				setDefaultValue(Float.valueOf(input.getText()));
				System.out.println("Set default value!");
			}
		});
	}
	
	@Override
	protected void initSize()
	{
		int oldRadius = radius;
		radius = 16;
		int max = Math.max(Strings.INPUT_NAMES_EDITOR[type.getIndex()].length, Strings.OUTPUT_NAMES_EDITOR[type.getIndex()].length);
		if (width == 0)
			width = 2 * radius * max + (max + 1) * xOffset;
		if (height == 0)
			height = 4 * radius + 4 * yOffset;
		
		radius = oldRadius;
		gui.setMinWidth(width);
		gui.setMinHeight(height);
	}
	
	@Override
	public Module getModule()
	{
		((Constant) module).setValue(Float.valueOf(input.getText()));
		return module;
	}
	
	public void setEditable(boolean editable)
	{
		input.setEditable(editable);
	}
	
	public boolean isEditable()
	{
		return input.isEditable();
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public float getMinValue() {
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public float getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(float defaultValue) {
		this.defaultValue = defaultValue;
	}
}
