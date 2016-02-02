package ui.editor;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import modules.Constant;
import modules.ModuleType;
import resources.Strings;

public class WaveformSelector extends ModuleGuiBackend
{
	
	public static final int NUM_WAVEFORMS = 5;

	public WaveformSelector(SynthesizerEditor owner, String name, int numPositions, String[] names) 
	{
		super(owner, ModuleType.CONSTANT, name);
	}
	
	@Override
	protected void initSize() 
	{
		height = 200;
		width = 200;
	};
	
	@Override
	protected void drawInputs()
	{
		HBox box = new HBox();
		for (int i = 0; i < Strings.WAVEFORMS.length; i++)
		{
			CheckBox checkBox = new CheckBox(Strings.WAVEFORMS[i]);
			final int type = i;
			checkBox.setOnAction((value) -> 
			{
				((Constant) module).setValue(type);
			});
			box.getChildren().add(box);
		}
		
		gui.getChildren().add(box);
	}
}
