/*
 * 
 */
package modules;

import engine.Module;
import engine.ModuleContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class LowpassFilter.
 */
public class LowpassFilter extends Module {
	
	/**
	 * Instantiates a new lowpass filter.
	 *
	 * @param parent the parent
	 */
	public LowpassFilter(ModuleContainer parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	/** The cutoff frequency. */
	private int cutoffFrequency;
	
	/** The response. */
	private int response;

	/**
	 * Gets the cutoff frequency.
	 *
	 * @return the cutoff frequency
	 */
	public int getCutoffFrequency() {
		return cutoffFrequency;
	}

	/**
	 * Sets the cutoff frequency.
	 *
	 * @param cutoffFrequency the new cutoff frequency
	 */
	public void setCutoffFrequency(int cutoffFrequency) {
		this.cutoffFrequency = cutoffFrequency;
	}

	/**
	 * Gets the response.
	 *
	 * @return the response
	 */
	public int getResponse() {
		return response;
	}

	/**
	 * Sets the response.
	 *
	 * @param response the new response
	 */
	public void setResponse(int response) {
		this.response = response;
	}

	/* (non-Javadoc)
	 * @see engine.Module#handleSample(float)
	 */
	@Override
	public float handleSample(float sampleValue)
	{
		
	}

	/* (non-Javadoc)
	 * @see engine.Module#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
