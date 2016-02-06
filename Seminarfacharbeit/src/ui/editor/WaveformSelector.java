package ui.editor;

import engine.Module;
import javafx.scene.control.ComboBox;
import modules.Constant;
import modules.ModuleType;
import resources.Strings;

public class WaveformSelector extends ConstantGui
{	
	public static final int NUM_WAVEFORMS = 5;
	
	private ComboBox<String> box;

	public WaveformSelector(SynthesizerEditor owner, String name, int defaultValue) 
	{
		super(owner, ModuleType.CONSTANT, name, 4, 0, defaultValue);
		box.setValue(Strings.WAVEFORMS[(int) getDefaultValue()]);
	}
	
	@Override
	protected void initSize() 
	{
		height = 75;
		width = 100;
	};
	
	@Override
	protected void drawInputs()
	{
		box = new ComboBox<>();
		box.setPrefWidth(width);
		
		for (int i = 0; i < Strings.WAVEFORMS.length; i++)
		{
			box.getItems().add(Strings.WAVEFORMS[i]);
		}
		
		box.setValue(Strings.WAVEFORMS[0]);
		
		box.valueProperty().addListener((ov, oldValue, newValue) -> 
		{
			setDefaultValue(Float.valueOf(indexOf(Strings.WAVEFORMS, newValue)));
		});
		
		gui.getChildren().add(box);
	}
	
	public static int indexOf(String[] array, String text)
	{
		for (int i = 0; i < array.length; i++)
		{
			if (text.equals(array[i]))
				return i;
		}
		
		return array.length;
	}
	
	public Module getModule()
	{
		((Constant) module).setValue(indexOf(Strings.WAVEFORMS, box.getValue()));
		return module;
	}
	
	@Override
	public boolean isEditable()
	{
		return box.isEditable();
	}
	
	@Override
	public void setEditable(boolean editable)
	{
		box.setEditable(editable);
	}
}
