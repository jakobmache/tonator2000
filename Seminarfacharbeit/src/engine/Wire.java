/*
 * 
 */
package engine;

// TODO: Auto-generated Javadoc
/**
 * The Class Wire.
 */
public class Wire {
	
	/** The input module. */
	private Module inputModule;
	
	/** The output module. */
	private Module outputModule;
	
	/** The value. */
	private short value;
	
	/**
	 * Fügt ein neues Kabel hinzu.
	 *
	 * @param inputModule das Modul, welches die Samples weiterverarbeiten soll
	 * @param outputModule das Modul, welches die Samples weiterverarbeitet hat
	 */
	public Wire(Module inputModule, Module outputModule)
	{
		connectInputModule(inputModule);
		connectOutputModule(outputModule);
		System.out.printf("Connected %s to %s\n", inputModule.toString(), outputModule.toString());
	}

	/**
	 * Gets the input module.
	 *
	 * @return the input module
	 */
	public Module getInputModule() {
		return inputModule;
	}

	/**
	 * Connect input module.
	 *
	 * @param inputModule the input module
	 */
	public void connectInputModule(Module inputModule) {
		this.inputModule = inputModule;
		inputModule.connectOutputWire(this);
	}

	/**
	 * Gets the output module.
	 *
	 * @return the output module
	 */
	public Module getOutputModule() {
		return outputModule;
	}

	/**
	 * Connect output module.
	 *
	 * @param outputModule das Modul, welches die Samples weiterverarbeitet hat
	 */
	public void connectOutputModule(Module outputModule) {
		this.outputModule = outputModule;
		outputModule.connectInputWire(this);
	}

	/**
	 * Send sample.
	 *
	 * @param sampleValue the sample value
	 */
	public void sendSample(float sampleValue) throws InterruptedException
	{
		outputModule.processSample(sampleValue);
	}


}
