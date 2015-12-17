package listener;

import modules.Envelope;

public interface EnvelopeFinishedListener 
{
	/**
	 * Wird aufgerufen, wenn eine Hüllkurve fertig mit Abspielen ist.
	 * @param envelope die fertige Hüllkurve
	 */
	public void onEnvelopeFinished(Envelope envelope);
}
