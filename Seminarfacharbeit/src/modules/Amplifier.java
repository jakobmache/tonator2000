/*
 * 
 */
package modules;

import engine.Module;
import engine.ModuleContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class Amplifier.
 */
public class Amplifier extends Module {

	/**
	 * Instantiates a new amplifier.
	 *
	 * @param parent the parent
	 */
	public Amplifier(ModuleContainer parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	/** The factor. */
	private double factor;
	
	/**
	 * Gets the factor.
	 *
	 * @return the factor
	 */
	public double getFactor() {
		return factor;
	}

	/**
	 * Sets the factor.
	 *
	 * @param factor the new factor
	 */
	public void setFactor(double factor) {
		this.factor = factor;
	}

	/* (non-Javadoc)
	 * @see engine.Module#handleSample(float)
	 */
	@Override
	public float handleSample(float sampleValue)
	{
		return (float) factor * sampleValue;
	}

	/* (non-Javadoc)
	 * @see engine.Module#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
