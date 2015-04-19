/*
 * 
 */
package engine;

// TODO: Auto-generated Javadoc
/**
 * The Class Module.
 */
public abstract class Module {
	
	/** The input wire. */
	private Wire inputWire;
	
	/** The output wire. */
	private Wire outputWire;
	
	/** The parent. */
	private ModuleContainer parent;
	
	/** The is active. */
	private boolean isActive;
	
	/**
	 * Instantiates a new module.
	 *
	 * @param parent the parent
	 */
	public Module(ModuleContainer parent)
	{
		this.parent = parent;
	}
	
	/**
	 * Process sample.
	 *
	 * @param sampleValue the sample value
	 */
	public void processSample(float sampleValue)
	{
		float newSample = handleSample(sampleValue);
		System.out.printf("\tModule %s received sample %f, sends %f\n", toString(), sampleValue, newSample);
		if (outputWire != null)
			outputWire.sendSample(newSample);
	}

	/**
	 * Handle sample.
	 *
	 * @param sampleValue the sample value
	 * @return the float
	 */
	public abstract float handleSample(float sampleValue);
	
	/**
	 * Close.
	 */
	public abstract void close();
	
	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * Gets the input wire.
	 *
	 * @return the input wire
	 */
	public Wire getInputWire() {
		return inputWire;
	}

	/**
	 * Connect input wire.
	 *
	 * @param inputWire the input wire
	 */
	public void connectInputWire(Wire inputWire) {
		System.out.printf("\tModule %s: InputWire %s was connected\n", toString(), inputWire.toString());
		this.inputWire = inputWire;
	}

	/**
	 * Gets the output wire.
	 *
	 * @return the output wire
	 */
	public Wire getOutputWire() {
		return outputWire;
	}

	/**
	 * Connect output wire.
	 *
	 * @param outputWire the output wire
	 */
	public void connectOutputWire(Wire outputWire) {
		System.out.printf("\tModule %s: OutputWire %s was connected\n", toString(), outputWire.toString());
		this.outputWire = outputWire;
	}

	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public ModuleContainer getParent() {
		return parent;
	}
	
}
