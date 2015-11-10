package engine;

import java.util.ArrayList;
import java.util.List;

import modules.Constant;
import containers.ContainerPreset;
import containers.ModuleContainerListener;

public abstract class ModuleContainer extends Module
{
	public static final int SAMPLE_INPUT = 0;
	public static final int SAMPLE_OUTPUT = 0;
	
	protected List<Module> modules;
	
	protected ContainerPreset preset;
	
	private List<ModuleContainerListener> listeners = new ArrayList<ModuleContainerListener>();
	
	public ModuleContainer(SynthesizerEngine parent, int numInputWires, int numOutputWires, int id)
	{
		super(parent, numInputWires, numOutputWires, id);
		modules = new ArrayList<Module>();
	}
	
	public void addModule(Module module)
	{
		modules.add(module);
	}
	
	public void addConnection(Module moduleDataIsGrabbedFrom, Module moduleDataIsSentTo, int indexDataIsGrabbedFrom, int indexDataIsSentTo)
	{
		new Wire(moduleDataIsSentTo, moduleDataIsGrabbedFrom, indexDataIsGrabbedFrom, indexDataIsSentTo);
	}
	
	public List<Module> findModulesById(int id)
	{
		List<Module> result = new ArrayList<Module>();
		for (Module module:modules)
		{
			if (module.getId() == id)
				result.add(module);
		}
		return result;
	}
	
	public Module findModuleById(int id)
	{
		for (Module module:modules)
		{
			if (module.getId() == id)
				return module;
		}
		return null;
	}
	
	public void applyContainerPreset(ContainerPreset preset)
	{
		this.preset = preset;
		for (Integer id:preset.getIds())
		{
			Constant module = (Constant) findModuleById(id);
			module.setValue(preset.getParam(id));
		}
	}
	
	public ContainerPreset getPreset()
	{
		return preset;
	}
	
	public void updatePreset(Integer id, float value)
	{
		preset.setParam(id, value);
		Constant module = (Constant) findModuleById(id);
		module.setValue(value);
	}
	
	public void addListener(ModuleContainerListener listener)
	{
		listeners.add(listener);
	}
	
	public void onFinished() 
	{
		for (ModuleContainerListener listener:listeners)
		{
			listener.onContainerFinished(this);
		}
	}
	
	public Wire getOutputWire()
	{
		return outputWires[SAMPLE_OUTPUT];
	}
}
