package containers;


import modules.Amplifier;
import modules.LowpassFilter;
import engine.Module;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
import engine.Wire;

public class StandardModuleContainer extends ModuleContainer 
{
	
	private Module inputModule;
	private Amplifier amplifier;
	private LowpassFilter filter;

	public StandardModuleContainer(SynthesizerEngine parent, Module inputModule)
	{
		super(parent);
		this.inputModule = inputModule;
		initModules();

	}
	
	private void initModules()
	{
		filter = new LowpassFilter(parent, 1, 1);
		new Wire(filter, inputModule, 0, 0);
	}

	public LowpassFilter getLowpassFilter()
	{
		return filter;
	}

	@Override
	public short requestNextSample(int outputWireIndex) 
	{
		short value = filter.requestNextSample(0);
		return value;
	}
}
