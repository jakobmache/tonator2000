
package modules;

import engine.Module;
import engine.ModuleContainer;

public class LowpassFilter extends Module {
	
	public LowpassFilter(ModuleContainer parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	private int cutoffFrequency;
	private int response;

	public int getCutoffFrequency() {
		return cutoffFrequency;
	}

	public void setCutoffFrequency(int cutoffFrequency) {
		this.cutoffFrequency = cutoffFrequency;
	}

	public int getResponse() {
		return response;
	}

	public void setResponse(int response) {
		this.response = response;
	}

	@Override
	public float handleSample(float sampleValue)
	{
		return sampleValue;
		
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
