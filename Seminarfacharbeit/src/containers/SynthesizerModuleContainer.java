package containers;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import engine.Module;
import engine.SynthesizerEngine;
import modules.Constant;
import modules.Envelope;
import modules.ModuleType;

public class SynthesizerModuleContainer extends PlayableModuleContainer
{
	private int zeroCounter = 0;
	private int zeroMax = 100;
	private float epsilon = 0.0001F;
	
	public SynthesizerModuleContainer(SynthesizerEngine parent, int numInputWires, int numOutputWires, int id,
			String name, PlayableModuleContainer container) {
		super(parent, numInputWires, numOutputWires, id, name, container);
	}
	
	public SynthesizerModuleContainer(SynthesizerEngine parent, int numInputWires, int numOutputWires, int id, String name) 
	{
		super(parent, numInputWires, numOutputWires, id, name);
	}
	
	public SynthesizerModuleContainer(SynthesizerEngine parent, int numInputWires, int numOutputWires, String name, String xmlPath) throws ParserConfigurationException, SAXException, IOException 
	{
		super(parent, numInputWires, numOutputWires, name, xmlPath);
	}

	@Override
	public void startPlaying(float frequency, float amplitude) 
	{	
		((Constant) findModuleById(getFrequencyId())).setValue(frequency);
		((Constant) findModuleById(getAmplitudeId())).setValue(amplitude);
		
		for (Module module:modules)
		{ 
			if (module.getType() == ModuleType.ENVELOPE)
				((Envelope) module).start();
		}
	}

	@Override
	public void stopPlaying() 
	{	
		for (Module module:modules)
		{
			if (module.getType() == ModuleType.ENVELOPE)
				((Envelope) module).release();
		}
		
		for (Module module:modules)
		{
			if (module.getType() == ModuleType.CONSTANT && ((Constant) module).isToZeroOnStop())
			{
				((Constant) module).setValue(0.0F);
			}
		}
	}
	
	public float calcNextSample(int index)
	{
		float value = inputWires[SAMPLE_INPUT].getNextSample();
		
		if (Math.abs(value) < epsilon)
		{
			System.out.println("0: " + zeroCounter);
			zeroCounter++;
		}
		else
		{
			zeroCounter = 0;
		}
		
		if (zeroCounter > zeroMax)
		{
			onFinished();
		}
		
		return value;
	}


}
