/*
 * 
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class StandardModuleContainer.
 */
public class StandardModuleContainer extends ModuleContainer 
{
	
	/**
	 * Instantiates a new standard module container.
	 *
	 * @param parent the parent
	 * @throws Exception the exception
	 */
	public StandardModuleContainer(SynthesizerEngine parent) throws Exception 
	{
		super(parent);
		addOscillator();
		addOutput();
		addOtherModules();
		List<Module> modules = getModules();
		new Wire(modules.get(0), modules.get(1));
	}
	
	/**
	 * Adds the oscillator.
	 */
	private void addOscillator()
	{
		Oscillator osci = new SineOscillator(this);
		setToneModule(osci);
	}
	
	/**
	 * Adds the output.
	 *
	 * @throws LineUnavailableException the line unavailable exception
	 */
	private void addOutput() throws LineUnavailableException
	{
		OutputModule module = new OutputModule(this);
		setOutputModule(module);		
	}
	
	/**
	 * Adds the other modules.
	 *
	 * @throws Exception the exception
	 */
	private void addOtherModules() throws Exception
	{
		Amplifier ampli = new Amplifier(this);
		ampli.setFactor(2);
		addModule(ampli);
	}
}
