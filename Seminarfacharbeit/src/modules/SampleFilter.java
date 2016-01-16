package modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import engine.Module;
import engine.SynthesizerEngine;

public class SampleFilter extends Module
{	
	public static final int SAMPLE_INPUT = 0;
	public static final int SAMPLE_OUTPUT = 0;
	
	private float currSample = 0;
	private List<Float> bufferList;
	int stop = 10;
	int count = 0;

	/**
	 * Der Samplefilter speichert jedes n-te Sample und hält die gespeicherten Samples abrufbar.
	 * 
	 * @param parent Engine
	 * @param id ID
	 * @param name Name
	 */
	public SampleFilter(SynthesizerEngine parent, int id, String name) 
	{
		super(parent, 1, 1, id, name);
		bufferList = new ArrayList<Float>();
	}

	@Override
	public float calcNextSample(int index) 
	{
		count++;
		currSample = inputWires[0].getNextSample();
		if (stop == count)
		{
			count = 0;
			bufferList.add(currSample);
		} 
		return currSample;
	}
	
	@Override
	public float calcNextDisabledSample(int index) 
	{
		return inputWires[SAMPLE_INPUT].getNextSample();
	}
	
	public List<Float> getBufferList()
	{
		List<Float> returnCopy = new ArrayList<Float>(bufferList);
		bufferList.clear();
		returnCopy.removeAll(Collections.singleton(null));
		return returnCopy;
	}
	
	public float getCurrSample()
	{
		return currSample;
	}

}