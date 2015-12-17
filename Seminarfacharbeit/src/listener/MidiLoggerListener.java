package listener;

public interface MidiLoggerListener 
{
	/**
	 * Wird aufgerufen, wenn der MIDI-Logger einen neuen Eintrag hinzugef�gt hat.
	 * @param description der neue Eintrag
	 */
	public void eventReceived(String description);
}
