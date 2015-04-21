
package containers;

import java.util.List;

import javax.sound.sampled.LineUnavailableException;

import modules.Amplifier;
import modules.Oscillator;
import modules.OutputModule;
import oscillators.SineOscillator;
import engine.Module;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
import engine.Wire;

public class StandardModuleContainer extends ModuleContainer 
{

	public StandardModuleContainer(SynthesizerEngine parent) throws Exception 
	{
		super(parent);
		addOscillator();
		addOutput();
		addOtherModules();
		List<Module> modules = getModules();
		connectAll();
	}
	
	private void addOscillator()
	{
		Oscillator osci = new SineOscillator(this);
		setToneModule(osci);
	}
	
	private void addOutput() throws LineUnavailableException
	{
		OutputModule module = new OutputModule(this);
		setOutputModule(module);		
	}

	private void addOtherModules() throws Exception
	{
		Amplifier ampli = new Amplifier(this);
		ampli.setFactor(2);
		addModule(ampli);
	}
}
