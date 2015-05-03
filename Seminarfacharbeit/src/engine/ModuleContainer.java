package engine;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;

import modules.OutputModule;

public abstract class ModuleContainer 
{
	private OutputModule outputModule;
	
	private List<Module> modules = new ArrayList<Module>();
	private SynthesizerEngine engine;
	
	public ModuleContainer(SynthesizerEngine parent) throws LineUnavailableException
	{
		engine = parent;
		outputModule = new OutputModule(this);
		addModule(outputModule);
	}
	
	public OutputModule getOutputModule() 
	{
		return outputModule;
	}

	public SynthesizerEngine getEngine()
	{
		return engine;
	}
	
	public void addModule(Module module)
	{
		modules.add(module);
	}
	
	public Module getModule(int index)
	{
		return modules.get(index);
	}
}
