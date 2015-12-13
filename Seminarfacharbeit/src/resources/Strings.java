package resources;

import modules.Ids;

public class Strings 
{
	public static final int ERROR_UNKNOWN = 0;
	public static final int ERROR_AUDIO = 1;
	public static final int ERROR_LOAD_ALL_PRESETS = 2;
	public static final int ERROR_CONNECT_MIDI_DEVICE = 3;
	public static final int ERROR_NO_MIDI_DEVICE_AVAILABLE = 4;
	public static final int ERROR_FILE_NOT_FOUND = 5;
	public static final int ERROR_WRITING_FILE = 6;
	public static final int ERROR_READING_FILE = 7;
	public static final int ERROR_TOO_LESS_DATA = 8;

	public static final String ae = "\u00e4";
	public static final String AE = "\u00c4";
	public static final String ue = "\u00fc";
	public static final String UE = "\u00dc";
	public static final String oe = "\u00f6";
	public static final String OE = "\u00d6";
	public static final String SS = "\u00df";

	public static final String APPLICATION_NAME = "Ton Total 2000";

	public static final String ERROR_TITLE = "Fehler";
	public static final String WARNING_TITLE = "Warnung";

	public static final String[] ERROR_HEADERS = new String[]
			{
		"Nicht n" + ae + "her spezifizierter Fehler!",
		"Fehler bei Audio-Initialisierung!",
		"Fehler beim Laden der Presets!",
		"Fehler beim Verbinden des MIDI-Ger" + ae + "tes!",
		"Keine MIDI-Ger" + ae + "te gefunden!",
		"Die angegebene Datei konnte nicht gefunden werden!",
		"Fehler beim Schreiben der Datei",
		"Fehler beim Lesen der Datei",
		"Preset nicht vollst" + ae + "ndig"
			};

	public static final String[] ERROR_EXPLANATIONS = new String[]
			{
		"Nicht n" + ae + "her spezifizierter Fehler ist aufgetreten. Wir bitten um Entschuldigung.",
		"Das Audiosystem konnte nicht mit den gew" + ue + "nschten Parametern initialisiert werden. Bitte " + ue + "berpr" + ue + "fen Sie Ihre Eingabe.",
		"Die Preset-Dateien konnten nicht vollst" + ae + "ndig geladen werden. Bitte " + ue + "berpr" + ue + "fen Sie die Dateien.",
		"Die Engine konnte nicht mit dem gew" + ue + "nschten MIDI-Ger" + ae + "t verbunden werden.  Bitte "+ ue + "berpr" + ue + "fen Sie,"
				+ "ob es von einer anderen Anwendung blockiert wird und trennen es gegebenenfalls.",
		"Es konnten keine MIDI-Ger" + ae + "te erkannt werden. Bitte verbinden Sie eines mit dem Computer.",
		"Das Programm konnte die angegebene Datei nicht finden. Bitte " + ue + "berpr" + ue + "fen sie den Pfad.",
		"Es ist ein Fehler beim Schreiben der Datei aufgetreten. Wir bitten um Entschuldigung.",
		"Die angegebene Datei konnte nicht gelesen werden. Bitte " + ue + "berpr" + ue + "fen Sie, ob die Datei g" + ue + "ltig ist.",
		"Die angegebene Datei ist nicht vollst" + ae + "ndig. Nicht genauer belegte Parameter behalten ihren alten Wert bei."
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
		"Lautst‰rke"
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
		{"Die Zeit, die der Ton ansteigt", "Die Zeit, die der Ton abf" + ae + "llt", "Das Level des Maximallevels, auf das der Ton abf‰llt", "Die Zeit, die der Ton verklingt."}
			};
	
	public static final String[][] INPUT_NAMES = new String[][]
			{
		{"Sample-Input", "Wellenform"},
		{"Sample-Input", "Cutoff-Frequenz", "Resonanz"},
		{"Sample-Input", "Attack-Zeit", "Decay-Zeit", "Sustain-Level", "Release-Zeit", "Steilheit", "Startlevel", "Peaklevel"},
		{"Sample-Input", "Verst‰rkungsfaktor"},
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

	public static final String[] MODULE_DESCRIPTIONS = new String[]
			{
		"Der Oszillator ist das Grundmodul der Tonerzeugung, es erzeugt Grundschwingungen in verschiedenen Formen.",
		"Der Tiefpassfilter l" + ae + "sst Frequenzen " + ue + "ber einer bestimmten Cutoff-Frequenz nicht passieren.",
		"Die H" + ue + "llkurve beschreibt den Amplitudenverlauf eines bestimmten Parameters. Hier noch ein bisschen Text zum Nerven.",
		"Das Oszilloskop zeigt die entstehenden Kl" + ae + "nge.",
		"Der Lautst" + ae +"rkeregler stellt die Lautst" + ae + "rke der Applikation ein. Er ver" + ae + "ndert den entstehenden Klang nicht."
			};
	
	public static final String[] STATUSBAR_TOOLTIPS = new String[]
			{
		"Samplingrate",
		"Latenz",
		"Enginestatus",
		"Maximale Polyphonie",
		"MIDI-Ger"+ ae + "t",
		"Aktuell ausgew"+ ae + "hltes Instrument"
			};

	public static final String OVERLAY_MIDI_STRING = "Bitte schlieﬂen Sie ein MIDI-Ger" + ae + "t an oder laden Sie eine MIDI-Datei!";

	public static final String STATUSBAR_NO_DEVICE_STRING = "Kein MIDI-Ger‰t angeschlossen";
	public static final String STATUSBAR_NOT_RUNNING_STRING = "Engine gestoppt";
	public static final String STATUSBAR_RUNNING_STRING = "Engine l" + ae + "uft!";


	public static final String VERSION_NUMBER = "v0.4.4 beta";

	public static final String SAVE_PRESET_FILE_NAME = "preset.xml";

	public static final String NO_MIDI_FILE_LOADED_LABEL = "Keine MIDI-Datei geladen!";
	
	public static final String MIDI_PLAYER_TITLE = "MIDI-Player";
	public static final String MIDI_LOGGER_TITLE = "MIDI-Logger";
	public static final String CLEAR_MIDI_LOGGER_MENU_ITEM = "Alles l" + oe + "schen";
	
	public static final String MIDI_PLAYER_INFO = "Info:\n";
	public static final String MIDI_PLAYER_COPYRIGHT = "Copyright:\n";
	public static final String MIDI_PLAYER_INSTRUMENTS = "Instrumente:\n";
	
	public static final String STATUSBAR_CONNECTING_DEVICE = "Verbinde mit MIDI-Ger" + ae + "t";
	
	//------------------------Dialogerl‰uterungen-------------
	
	public static final String ABOUT_DIALOG_TITLE = UE + "ber das Programm";
	public static final String ABOUT_DIALOG_TEXT =  "Entstanden in Rahmen der Seminarfacharbeit 2014/15 von Leonhard Braun, Barbara Ueltzen und Jakob Mache"
			+ " mit dem Thema \"Digitale Simulation analoger Synthesizer\"."
			+ " Herzlichen Dank an unseren Fachbetreuer Herr S" + ue + "pke!";
	public static final String ABOUT_DIALOG_HEADER = "(c) 2015 by Leonhard Braun, Barbara Ueltzen, Jakob Mache";	
	
	public static final String LIBRARIES_DIALOG_TITLE = "Verwendete Ressourcen";
	public static final String LIBRARIES_DIALOG_HEADER = "Folgende Bibliotheken und Grafiken wurden verwendet:";
	public static final String LIBRARIES_DIALOG_TEXT = "Fehler beim Laden der Bibliotheken";
	
	public static final String POLYPHONY_DIALOG_TITLE = "Maximale Polyphonie eingeben";
	public static final String POLYPHONY_DIALOG_HEADER = "Maximale Polyphonie aktualisieren";
	public static final String POLYPHONY_DIALOG_TEXT = "Neue maximale Polyphonie:";
	
	public static final String SAMPLERATE_DIALOG_TITLE = "Samplingrate eingeben";
	public static final String SAMPLERATE_DIALOG_HEADER = "Samplingrate aktualisieren";
	public static final String SAMPLINGRATE_DIALOG_TEXT = "Neue Samplingrate:";
	
	public static final String BUFFERTIME_DIALOG_TITLE = "Latenz eingeben";
	public static final String BUFFERTIME_DIALOG_HEADER = "Latenz (Pufferdauer) aktualisieren";
	public static final String BUFFERTIME_DIALOG_TEXT = "Neue Latenz (in ms):";
	
	public static final String MIDI_DEVICE_DIALOG_TITLE = "MIDI-Ger" + ae + "t ausw" + ae + "hlen";
	public static final String MIDI_DEVICE_DIALOG_HEADER = "MIDI-Ger" + ae + "t verbinden";
	public static final String MIDI_DEVICE_DIALOG_TEXT = "Verf" + ue + "gbare Ger" + ae + "te:";
	
	public static final String CURR_INSTRUMENT_DIALOG_TITLE = "Aktuelles Instrument ausw" + ae + "hlen";
	public static final String CURR_INSTRUMENT_DIALOG_HEADER = "Aktuelles Instrument / Programm ausw" + ae + "hlen";
	public static final String CURR_INSTRUMENT_DIALOG_TEXT = "Verf" + ue + "gbare Instrumente:";
	
	public static final String CHANNEL_INSTRUMENT_DIALOG_TITLE = "Instrumentenzuweisung";
	public static final String CHANNEL_INSTRUMENT_DIALOG_HEADER = "Einem MIDI-Kanal ein Instrument(Programm) zuweisen";
	
	public static final String BUTTON_TYPE_RESET_ALL = "Alles zur" + ue + "cksetzen";
	
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
			return "Amplituden-H" + ue + "llkurve";
		else if (id == Ids.ID_ENVELOPE_2)
			return "Tiefpass-H" + ue + "llkurve";
		else if (id == Ids.ID_LOWPASS_1)
			return "Tiefpassfilter";
		else if (id == Ids.ID_VOLUME)
			return "Lautst" + ae + "rke";
		else if (id == Ids.ID_HIGHPASS_1)
			return "Hochpassfilter";
		else 
			return "Modul";	
	}


}
