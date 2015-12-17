package listener;

public interface ProgramListener 
{
	/**
	 * Wird aufgerufen, wenn sich ein Parameter eines Instruments verändert hat.
	 * 
	 * @param program das veränderte Instrument
	 * @param id ID des veränderten Parameters
	 * @param newValue neuer Wert des Parameters
	 */
	public void programValueChanged(int program, int id, float newValue);
}
