package modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import engine.Module;
import engine.SynthesizerEngine;

//TODO:Namen ausdenken
public class SampleFilter extends Module
{	
	private float currSample = 0;
	private List<Float> bufferList;
	int stop = 10;
	int count = 0;

	public SampleFilter(SynthesizerEngine parent) 
	{
		super(parent, 1, 1, Ids.ID_SAMPLE_FILTER);
		bufferList = new ArrayList<Float>();
	}

	@Override
	public float requestNextSample() 
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