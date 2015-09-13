package engine;

import java.util.ArrayList;
import java.util.List;

public abstract class ModuleContainer extends Module
{
	private Wire outputWire;
	private List<Module> modules;
	
	public ModuleContainer(SynthesizerEngine parent)
	{
		super(parent, 1, 1);
		modules = new ArrayList<Module>();
	}
	
	public void addModule(Module module)
	{
		modules.add(module);
	}

	@Override
	public abstract short requestNextSample(int outputWireIndex);
}
