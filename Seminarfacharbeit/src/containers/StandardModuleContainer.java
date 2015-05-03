package containers;

import javax.sound.sampled.LineUnavailableException;

import modules.Oscillator;
import modules.OutputModule;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
import engine.Wire;

public class StandardModuleContainer extends ModuleContainer 
{
	private Oscillator osci;

	public StandardModuleContainer(SynthesizerEngine parent) throws LineUnavailableException 
	{
		super(parent);
		initModules();
		initWires();
	}
	
	private void initModules()
	{
		osci = new Oscillator(this);
	}
	
	private void initWires()
	{
		Wire wire = new Wire(getOutputModule(), osci);
	}
	
	public Oscillator getOscillator()
	{
		return osci;
	}
}
