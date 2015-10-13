package containers;


import modules.SampleFilter;
import modules.LowpassFilter;
import engine.Module;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
import engine.Wire;

public class StandardModuleContainer extends ModuleContainer 
{
	
	private Module inputModule;
	private LowpassFilter filter;
	private SampleFilter identity;

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
		identity = new SampleFilter(parent);
		new Wire(identity, filter, 0, 0);
	}

	public LowpassFilter getLowpassFilter()
	{
		return filter;
	}
	
	public SampleFilter getIdentity()
	{
		return identity;
	}

	@Override
	public short requestNextSample(int outputWireIndex) 
	{
		short value = identity.requestNextSample(0);
		return value;
	}
}
