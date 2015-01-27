package modules;

import engine.Module;

public class Oscillator extends Module {
	
	private int frequency;
	private int phase;
	private int pulseWidth;

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	public int getPulseWidth() {
		return pulseWidth;
	}

	public void setPulseWidth(int pulseWidth) {
		this.pulseWidth = pulseWidth;
	}

}
