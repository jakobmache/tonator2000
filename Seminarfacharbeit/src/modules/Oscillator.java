/*
 * 
 */
package modules;

import engine.Event;
import engine.Module;
import engine.ModuleContainer;
import events.PlayEvent;

public abstract class Oscillator extends Module {
	
	protected float frequency;
	protected float phase;
	protected float pulseWidth;
	protected float duration;
	protected float amplitude;
	
	public Oscillator(ModuleContainer parent)
	{
		super(parent);
	}

	public abstract short handleSample(short sampleValue);

	public abstract void startPlaying(PlayEvent event);

	public abstract void stopPlaying(Event event);
	
	public abstract void pausePlaying();

	public double getFrequency() {
		return frequency;
	}

	public double getPhase() {
		return phase;
	}

	public double getPulseWidth() {
		return pulseWidth;
	}
	
	public float getDuration() {
		return duration;
	}

	public float getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	public void setPhase(float phase) {
		this.phase = phase;
	}

	public void setPulseWidth(float pulseWidth) {
		this.pulseWidth = pulseWidth;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}
	
	

}
