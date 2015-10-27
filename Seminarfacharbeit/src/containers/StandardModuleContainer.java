package containers;


import modules.Constant;
import modules.Envelope;
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
	private SampleFilter sampleFilter;
	private Envelope cutoffEnvelope; 
	private Constant cutoffConstant;

	public StandardModuleContainer(SynthesizerEngine parent, Module inputModule)
	{
		super(parent);
		this.inputModule = inputModule;
		initModules();
	}
	
	private void initModules()
	{
		filter = new LowpassFilter(parent);
		cutoffConstant = new Constant(parent);
		cutoffConstant.setValue((short)10); 
		new Wire(filter, inputModule, 0, 0);
		cutoffEnvelope = new Envelope(parent, cutoffConstant);
		cutoffEnvelope.setMaxValue((short) 1000); 
		cutoffEnvelope.setAttackTime(1000);
		cutoffEnvelope.setDecayTime(1000);
		cutoffEnvelope.setSustainLevel(0.5F);
		new Wire(cutoffEnvelope, cutoffConstant, 0, 0);
		new Wire(filter, cutoffEnvelope, 0, 1);
		sampleFilter = new SampleFilter(parent);
		new Wire(sampleFilter, filter, 0, 0);
	}

	public LowpassFilter getLowpassFilter()
	{
		return filter;
	}
	
	public Constant getCutoffFrequencyInput()
	{
		return cutoffConstant;
	}
	
	public SampleFilter getSampleFilter()
	{
		return sampleFilter;
	}

	@Override
	public float requestNextSample() 
	{
		float value = sampleFilter.requestNextSample();
		return value;
	}
}
