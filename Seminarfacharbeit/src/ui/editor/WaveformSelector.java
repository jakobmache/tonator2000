package ui.editor;

import engine.Module;
import javafx.scene.control.ComboBox;
import javafx.scene.shape.Box;
import modules.Constant;
import modules.ModuleType;
import resources.Strings;

public class WaveformSelector extends ModuleGuiBackend
{
	
	public static final int NUM_WAVEFORMS = 5;
	
	private ComboBox<String> box;

	public WaveformSelector(SynthesizerEditor owner, String name) 
	{
		super(owner, ModuleType.CONSTANT, name);
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
		System.out.println("Module value: " + module.requestNextSample(Constant.VALUE_OUTPUT));
		return module;
	}
}
