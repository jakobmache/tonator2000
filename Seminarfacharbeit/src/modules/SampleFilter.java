package modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import engine.Module;
import engine.SynthesizerEngine;

//TODO:Namen ausdenken
public class SampleFilter extends Module
{	
	private short currSample = 0;
	private List<Short> bufferList;
	int stop = 10;
	int count = 0;

	public SampleFilter(SynthesizerEngine parent) 
	{
		super(parent, 1, 1);
		bufferList = new ArrayList<Short>();
	}

	@Override
	public short requestNextSample(int outputWireIndex) 
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
	
	public List<Short> getBufferList()
	{
		List<Short> returnCopy = new ArrayList<Short>(bufferList);
		bufferList.clear();
		returnCopy.removeAll(Collections.singleton(null));
		return returnCopy;
	}
	
	public short getCurrSample()
	{
		return currSample;
	}
}