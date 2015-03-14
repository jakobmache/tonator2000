package modules;

import engine.Module;

public class Oscillator extends Module {
	
	private double frequency;
	private double phase;
	private double pulseWidth;
	
	public Oscillator(double frequency, double pulseWidth, double phase)
	{
		this.frequency = frequency;
		this.pulseWidth = pulseWidth;
		this.phase = phase;
	}

	public void run(double sample) {
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public double getPhase() {
		return phase;
	}

	public void setPhase(double phase) {
		this.phase = phase;
	}

	public double getPulseWidth() {
		return pulseWidth;
	}

	public void setPulseWidth(double pulseWidth) {
		this.pulseWidth = pulseWidth;
	}

}
