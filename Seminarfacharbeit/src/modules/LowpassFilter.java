package modules;

import engine.Event;
import engine.Module;

public class LowpassFilter extends Module {
	
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
	public void handleEvent(Event event)
	{
		
	}

}
