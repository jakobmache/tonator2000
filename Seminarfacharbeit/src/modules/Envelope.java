/*
 * 
 */
package modules;

import engine.Module;
import engine.ModuleContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class Envelope.
 */
public class Envelope extends Module {

	/**
	 * Instantiates a new envelope.
	 *
	 * @param parent the parent
	 */
	public Envelope(ModuleContainer parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	/** The attack time. */
	private int attackTime;
	
	/** The delay time. */
	private int delayTime;
	
	/** The release time. */
	private int releaseTime;
	
	/** The sustain value. */
	private int sustainValue;
	
	/** The max value. */
	private short maxValue;
	
	/**
	 * Gets the attack time.
	 *
	 * @return the attack time
	 */
	public int getAttackTime() {
		return attackTime;
	}

	/**
	 * Sets the attack time.
	 *
	 * @param attackTime the new attack time
	 */
	public void setAttackTime(int attackTime) {
		this.attackTime = attackTime;
	}

	/**
	 * Gets the delay time.
	 *
	 * @return the delay time
	 */
	public int getDelayTime() {
		return delayTime;
	}

	/**
	 * Sets the delay time.
	 *
	 * @param delayTime the new delay time
	 */
	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	/**
	 * Gets the release time.
	 *
	 * @return the release time
	 */
	public int getReleaseTime() {
		return releaseTime;
	}

	/**
	 * Sets the release time.
	 *
	 * @param releaseTime the new release time
	 */
	public void setReleaseTime(int releaseTime) {
		this.releaseTime = releaseTime;
	}

	/**
	 * Gets the sustain value.
	 *
	 * @return the sustain value
	 */
	public int getSustainValue() {
		return sustainValue;
	}

	/**
	 * Sets the sustain value.
	 *
	 * @param sustainValue the new sustain value
	 */
	public void setSustainValue(int sustainValue) {
		this.sustainValue = sustainValue;
	}

	/**
	 * Gets the max value.
	 *
	 * @return the max value
	 */
	public short getMaxValue() {
		return maxValue;
	}

	/**
	 * Sets the max value.
	 *
	 * @param maxValue the new max value
	 */
	public void setMaxValue(short maxValue) {
		this.maxValue = maxValue;
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
