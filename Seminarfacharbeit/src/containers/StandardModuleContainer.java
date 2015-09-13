package containers;


import modules.Amplifier;
import modules.Oscillator;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
import engine.Wire;

public class StandardModuleContainer extends ModuleContainer 
{

	private Oscillator oscillator;
	
	public StandardModuleContainer(SynthesizerEngine parent)
	{
		super(parent);
		initModules();
	}
	
	private void initModules()
	{
		oscillator = new Oscillator(parent);
		Amplifier amplifier = new Amplifier(parent);
		amplifier.setFactor(5);
		Wire wire = new Wire(amplifier, oscillator, 0, 0);

		Wire wire2 = new Wire(this, amplifier, 0, 0);
	}
	
	public Oscillator getOscillator()
	{
		return oscillator;
	}

	@Override
	public short requestNextSample(int outputWireIndex) 
	{
		return inputWires[0].getNextSample();
	}
}
