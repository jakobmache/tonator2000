package engine;

import java.util.ArrayList;
import java.util.List;

import modules.Ids;

public abstract class ModuleContainer extends Module
{
	private List<Module> modules;
	
	public ModuleContainer(SynthesizerEngine parent)
	{
		super(parent, 1, 1, Ids.ID_CONTAINER);
		modules = new ArrayList<Module>();
	}
	
	public void addModule(Module module)
	{
		modules.add(module);
	}

	@Override
	public abstract float requestNextSample();
}
