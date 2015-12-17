package listener;

public interface ProgramListener 
{
	/**
	 * Wird aufgerufen, wenn sich ein Parameter eines Instruments ver�ndert hat.
	 * 
	 * @param program das ver�nderte Instrument
	 * @param id ID des ver�nderten Parameters
	 * @param newValue neuer Wert des Parameters
	 */
	public void programValueChanged(int program, int id, float newValue);
}
