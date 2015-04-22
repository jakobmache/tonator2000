/*
 * 
 */
package modules;

import engine.Module;
import engine.ModuleContainer;

public class Amplifier extends Module {

	public Amplifier(ModuleContainer parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	private double factor;
	
	public double getFactor() {
		return factor;
	}

	public void setFactor(double factor) {
		this.factor = factor;
	}

	@Override
	public short handleSample(short sampleValue)
	{
		return (short)(factor * sampleValue);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}
