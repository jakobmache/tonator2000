package containers;

import resources.Strings;
import modules.Ids;
import modules.SampleFilter;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
import engine.Wire;

public class StandardModuleContainer extends ModuleContainer 
{
	
	private SampleFilter filter;

	public StandardModuleContainer(SynthesizerEngine parent, int numInputWires,
			int numOutputWires, int id, String name) 
	{
		super(parent, numInputWires, numOutputWires, id, name);
		
		filter = new SampleFilter(parent, Ids.ID_SAMPLE_FILTER_1,  Strings.getStandardModuleName(Ids.ID_SAMPLE_FILTER_1));
		addModule(filter);
	}
	
	@Override
	public void connectInputWire(int index, Wire wire)
	{
		findModuleById(Ids.ID_SAMPLE_FILTER_1).connectInputWire(index, wire);
	}

	@Override
	public float calcNextSample(int index) {
		float sample = filter.requestNextSample(SampleFilter.SAMPLE_OUTPUT);
		return sample;
	}

	@Override
	public float calcNextDisabledSample(int index) {
		float sample = filter.requestNextSample(SampleFilter.SAMPLE_OUTPUT);
		return sample;
	}

}
