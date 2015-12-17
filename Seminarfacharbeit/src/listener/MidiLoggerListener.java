package listener;

public interface MidiLoggerListener 
{
	/**
	 * Wird aufgerufen, wenn der MIDI-Logger einen neuen Eintrag hinzugefügt hat.
	 * @param description der neue Eintrag
	 */
	public void eventReceived(String description);
}
