package resources;

import modules.Ids;

public class Strings 
{
	public static final int ERROR_UNKNOWN = 0;
	public static final int ERROR_AUDIO = 1;
	public static final int ERROR_LOAD_ALL_PRESETS = 2;

	public static final String ae = "\u00e4";
	public static final String AE = "\u00c4";
	public static final String ue = "\u00fc";
	public static final String UE = "\u00dc";
	public static final String oe = "\u00f6";
	public static final String OE = "\u00d6";
	public static final String SS = "\u00df";

	public static final String APPLICATION_NAME = "Ton Total 2000";

	public static final String ERROR_TITLE = "Fehler";

	public static final String[] ERROR_HEADERS = new String[]
			{
		"Nicht n" + ae + "her spezifizierter Fehler",
		"Fehler bei Audio-Initialisierung",
		"Fehler beim Laden der Presets"
			};

	public static final String[] ERROR_EXPLANATIONS = new String[]
			{
		"Nicht n" + ae + "her spezifizierter Fehler ist aufgetreten. Wir bitten um Entschuldigung.",
		"Das Audiosystem konnte nicht mit den gew" + ue + "nschten Parametern initialisiert werden. Bitte " + ue + "berpr" + ue + "fen Sie Ihre Eingabe.",
		"Die Preset-Dateien konnten nicht vollst" + ae + "ndig geladen werden. Bitte " + ue + "berpr" + ue + "fen Sie die Dateien." 
			};

	public static final String[] MODULE_NAMES = new String[]
			{
		"Oszillator",
		"Tiefpassfilter",
		"H" + ue + "llkurve",
		"Verst" + ae + "rker",
		"Mixer",
		"Balance",
		"Hochpassfilter",
		"Konstante",
		"Oszilloskop",
		"Lautstärke"
			};

	public static final String[][] PARAM_NAMES_MAIN = new String[][]
			{
		{"Wellenform"},
		{"Cutoff-Frequenz", "Resonanz"},
		{"Attack-Zeit", "Decay-Zeit", "Sustain-Level", "Release-Zeit"}
			};

	public static final String[][] PARAM_DESCRIPTIONS = new String[][]
			{
		{"Das ist die Wellenform, die der Oszillator erzeugt."},
		{"Das ist die Frequenz, ab der abgeschnitten wird.", "Gibt an, wie stark die Cutoff-Frequenz betont wird."},
		{"Die Zeit, die der Ton ansteigt", "Die Zeit, die der Ton abf" + ae + "llt", "Das Level des Maximallevels, auf das der Ton abfällt", "Die Zeit, die der Ton verklingt."}
			};
	
	public static final String[][] INPUT_NAMES = new String[][]
			{
		{"Sample-Input", "Wellenform"},
		{"Sample-Input", "Cutoff-Frequenz", "Resonanz"},
		{"Sample-Input", "Attack-Zeit", "Decay-Zeit", "Sustain-Level", "Release-Zeit", "Steilheit", "Startlevel", "Peaklevel"},
		{"Sample-Input", "Verstärkungsfaktor"},
		{"Sample-Input"},
		{"Modul 1", "Modul 2", "Balance"},
		{"Sample-Input", "Cutoff-Frequenz", "Resonanz"},
		{}
			};
	
	public static final String[][] OUTPUT_NAMES = new String[][]
			{
		{"Sample-Output"},
		{"Sample-Output"},
		{"Sample-Output"},
		{"Sample-Output"},
		{"Sample-Output"},
		{"Sample-Output"},
		{"Sample-Output"},
		{"Wert-Output"}
			};

	public static final String[] TOOLTIPS = new String[]
			{
		"Der Oszillator ist das Grundmodul der Tonerzeugung, es erzeugt Grundschwingungen in verschiedenen Formen.",
		"Der Tiefpassfilter l" + ae + "sst Frequenzen " + ue + "ber einer bestimmten Cutoff-Frequenz nicht passieren.",
		"Die H" + ue + "llkurve beschreibt den Amplitudenverlauf eines bestimmten Parameters. Hier noch ein bisschen Text zum Nerven.",
		"Das Oszilloskop zeigt die entstehenden Kl" + ae + "nge.",
		"Der Lautst" + ae +"rkeregler stellt die Lautst" + ae + "rke der Applikation ein. Er ver" + ae + "ndert den entstehenden Klang nicht."
			};

	public static final String OVERLAY_MIDI_STRING = "Bitte schließen Sie ein MIDI-Ger" + ae + "t an oder laden Sie eine MIDI-Datei!";

	public static final String STATUSBAR_NO_DEVICE_STRING = "Kein MIDI-Gerät angeschlossen";
	public static final String STATUSBAR_NOT_RUNNING_STRING = "Engine gestoppt";
	public static final String STATUSBAR_RUNNING_STRING = "Engine l" + ae + "uft!";

	public static final String START_POPUP_TEXT = "Herzlich Willkommen zu " + APPLICATION_NAME + ", dem ersten und einzigen Programm, welches Sie jemals zum Erzeugen von T" + oe + "nen und zur Verzerrung bekannter Lieder benutzen werden!"
			+ "\nViel Spaß w" + ue 
			+ "nschen Ihnen \n\nLeonhard von Braun, Chief Executive Officer bei Neumann Enterprises\nBarbara Ueltzen, Executive Chairman bei Neumann Enterprises\nJakob Mache, Chief Technical Officer bei Neumann Enterprises";
	public static final String START_POPUP_TITLE = "Information";
	public static final String START_POPUP_HEADER = "Herzlich Willkommen!";

	public static final String VERSION_NUMBER = "v0.4.3 beta";

	public static final String SAVE_PRESET_FILE_NAME = "preset.xml";


	//-------------------------Modulnamen---------------------

	public static String getStandardModuleName(int id)
	{
		if (id == Ids.ID_MIXER_1)
			return "Mixer";
		else if (id == Ids.ID_MIXER_2)
			return "Balance";
		else if (id == Ids.ID_OSCILLATOR_1)
			return "Oszillator 1";
		else if (id == Ids.ID_OSCILLATOR_2)
			return "Oszillator 2";
		else if (id == Ids.ID_ENVELOPE_1)
			return "Amplituden-Hüllkurve";
		else if (id == Ids.ID_ENVELOPE_2)
			return "Tiefpass-Hüllkurve";
		else if (id == Ids.ID_LOWPASS_1)
			return "Tiefpassfilter";
		else if (id == Ids.ID_VOLUME)
			return "Lautstärke";
		else 
			return "Modul";	
	}


}
