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
	public static final int ERROR_BUILDING_SYNTHESIZER = 9;
	public static final int ERROR_LOADING_SYNTHESIZER = 10;

	public static final String ae = "\u00e4";
	public static final String AE = "\u00c4";
	public static final String ue = "\u00fc";
	public static final String UE = "\u00dc";
	public static final String oe = "\u00f6";
	public static final String OE = "\u00d6";
	public static final String SS = "\u00df";

	public static final String APPLICATION_NAME = "Tonator 2000 Professional Edition";
	//public static final String APPLICATION_NAME = "Seminarfacharbeit";
	
	public static final String VERSION_NUMBER = "v2.0.2 beta [unstable]";

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
		"Preset nicht vollst" + ae + "ndig",
		"Synthesizer konnte nicht erstellt werden",
		"Synthesizer konnte nicht geladen werden"
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
		"Die angegebene Datei ist nicht vollst" + ae + "ndig. Nicht genauer belegte Parameter behalten ihren alten Wert bei.",
		"Der Synthesizer konnte nicht erstellt werden. Bitte " + ue + "berpr" + ue + "fen Sie, ob mit jedem Ein-und Ausgang ein Kabel verbunden ist.",
		"Der Synthesizer konnte nicht geladen werden. Bitte " + ue + "berpr " + ue + "fen Sie, ob die Datei fehlerfrei erstellt wurde."
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
		"Lautstärke",
		"Audioausgabe",
		"Duplicator"
			};

	public static final String[][] PARAM_NAMES_MAIN = new String[][]
			{
		{"Wellenform"},
		{"Cutoff-Frequenz", "Resonanz"},
		{"Attack-Zeit", "Decay-Zeit", "Sustain-Level", "Release-Zeit", "Steilheit"},
		{"Faktor"},
		{},
		{"Balance"},
		{"Cutoff-Frequenz", "Resonanz"},
		{},
		{},
		{},
		{}
			};

	public static final String[][] PARAM_DESCRIPTIONS = new String[][]
			{
		{"Das ist die Wellenform, die der Oszillator erzeugt."},
		{"Das ist die Frequenz, ab der die Obert" + oe + "ne gefiltert werden. Hohe Werte bedeuten eine hohe Cutoff-Frequenz, niedrige Werte eine kleinere Cutoff-Frequenz.", 
			"Gibt an, wie stark die Cutoff-Frequenz betont wird."},
		{"Die Zeitspanne in ms, in der die Amplitude des Parameters bis zum Maximallevel steigt.", "Die Zeitspanne in ms, in der die Amplitude des Parameters vom Maximallevel auf das Sustainlevel abf" + ae + "llt.", 
			"Das Level, auf das der Ton nach der Decayphase abfällt. Ist ein Vielfaches der anfänglichen Amplitude. ", "Die Zeitspanne in ms, in der die Amplitude des Parameters vom Sustainlevel auf das Endlevel abf" + ae + "llt.",
			"Gibt an, wie stark exponentiell die Amplitude steigt oder abf" + ae + "llt. Hohe Werte sorgen für einen fast linearen Verlauf, niedrige für einen eher exponentiellen."},
		{"Das ist der Faktor, mit der das Eingangssample multipliziert wird."},
		{},
		{"Die Balance gibt an, wie stark die einzelnen Eingangssample gewichtet werden. Je mehr man den Regler zu einem Modul zieht, desto lauter ist es zu hören."},
		{"Das ist die Frequenz, ab der die Obert" + oe + "ne gefiltert werden. Hohe Werte bedeuten eine hohe Cutoff-Frequenz, niedrige Werte eine kleinere Cutoff-Frequenz.", 
			"Gibt an, wie stark die Cutoff-Frequenz betont wird."},
		{},
		{},
		{}
			};

			
			
	public static final String[][] INPUT_NAMES_EDITOR = new String[][]
			{
		{"Frequenz", "Amplitude", "Wellenform"},
		{"Sample-Input", "Cutoff-Frequenz", "Resonanz"},
		{"Sample-Input", "Attack-Zeit", "Decay-Zeit", "Sustain-Level", "Release-Zeit", "Steilheit", "Startlevel", "Peaklevel"},
		{"Sample-Input", "Verstärkungsfaktor"},
		{"Sample-Input"},
		{"Modul 1", "Modul 2", "Balance"},
		{"Sample-Input", "Cutoff-Frequenz", "Resonanz"},
		{},
		{},
		{},
		{"Sample-Input"},
		{"Sample-Input"}
			};

	public static final String[][] OUTPUT_NAMES_EDITOR = new String[][]
			{
		{"Sample-Output"},
		{"Sample-Output"},
		{"Sample-Output"},
		{"Sample-Output"},
		{"Sample-Output"},
		{"Sample-Output"},
		{"Sample-Output"},
		{"Wert-Output"},
		{},
		{},
		{},
		{"Sample-Output 1", "Sample-Output 2"}
			};
			
	public static final String MIN_SHORT = Short.toString(Short.MIN_VALUE);
	public static final String MAX_SHORT = Short.toString(Short.MAX_VALUE);
	public static final String INFINITY = "Nicht beschr" + ae + "nkt";
			
	public static final String[][] MINS_EDITOR = new String[][]
			{
		{"0", MIN_SHORT, "/"},
		{MIN_SHORT, "0", "0"},
		{MIN_SHORT, "0", "0", "0", "0", "-20", "0", "0"},
		{MIN_SHORT, "0"},
		{MIN_SHORT},
		{MIN_SHORT, MIN_SHORT, "0"},
		{MIN_SHORT, "0", "0"},
		{}, {}, {},
		{MIN_SHORT},
		{MIN_SHORT}
			};
			
	public static final String[][] MAXS_EDITOR = new String[][]
					{
				{INFINITY, MAX_SHORT, "/"},
				{MAX_SHORT, "1", "1"},
				{MAX_SHORT, INFINITY, INFINITY, INFINITY, INFINITY, "-1", INFINITY, INFINITY},
				{MAX_SHORT, "0"},
				{MAX_SHORT},
				{MAX_SHORT, MAX_SHORT, "0"},
				{MAX_SHORT, "0", "0"},
				{}, {}, {},
				{MAX_SHORT},
				{MAX_SHORT}
					};

	public static final String[] MODULE_DESCRIPTIONS = new String[]
			{
		"Der Oszillator ist das Grundmodul der Tonerzeugung, es erzeugt Grundschwingungen in verschiedenen Formen.",
		"Der Tiefpassfilter l" + ae + "sst Obert" + oe + "ne " + ue + "ber einer bestimmten Cutoff-Frequenz nicht passieren.",
		"Die H" + ue + "llkurve beschreibt den Amplitudenverlauf eines bestimmten Parameters in vier Phasen.",
		"",
		"Der Mixer mixt alle eingehenden Signale im selben Verh" + ae + "ltnis zusammen.",
		"Der balancierte Mixer addiert zwei Eingangssignale und wichtet diese unterschiedlich stark.",
		"Der Hochpassfilter l" + ae + "sst Obert" + oe + "ne unter einer bestimmten Cutoff-Frequenz nicht passieren.",
		"",
		"Das Oszilloskop zeigt die entstehenden Kl" + ae + "nge. Achtung: Es zeichnet diese nicht exakt, sondern plottet aus Performancegründen nur einen Teil der Daten!"
				+ " Deshalb kann es zu Ungenauigkeiten bei der Darstellung kommen.",
		"Der Lautst" + ae +"rkeregler stellt die Lautst" + ae + "rke der Applikation ein. Er ver" + ae + "ndert die Amplitude des entstehenden Klanges nicht, nur die Lautstärke der Audioausgabe.",
		"",
		"Der Verdoppler verdoppelt ein Eingangssignal auf beide Ausg" + ae + "nge."
			};
	
	public static final String[] WAVEFORMS = new String[]
			{
					"Sinus", "S" + ae + "gezahn", "Rechteck", "Dreieck", "Wei" + SS + "es Rauschen"
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
	
	public static final String WAVEFORM_SELECTOR_NAME = "Wellenform";

	public static final String OVERLAY_MIDI_STRING = "Bitte schließen Sie ein MIDI-Ger" + ae + "t an oder laden Sie eine MIDI-Datei!";

	public static final String STATUSBAR_NO_DEVICE_STRING = "Kein MIDI-Gerät angeschlossen";
	public static final String STATUSBAR_NOT_RUNNING_STRING = "Engine gestoppt";
	public static final String STATUSBAR_RUNNING_STRING = "Engine l" + ae + "uft!";

	public static final String SAVE_PRESET_FILE_NAME = "preset.xml";

	public static final String NO_MIDI_FILE_LOADED_LABEL = "Keine MIDI-Datei geladen!";

	public static final String MIDI_PLAYER_TITLE = "MIDI-Player";
	public static final String MIDI_LOGGER_TITLE = "MIDI-Logger";
	public static final String CLEAR_MIDI_LOGGER_MENU_ITEM = "Alles l" + oe + "schen";

	public static final String MIDI_PLAYER_INFO = "Info:\n";
	public static final String MIDI_PLAYER_COPYRIGHT = "Copyright:\n";
	public static final String MIDI_PLAYER_INSTRUMENTS = "Instrumente:\n";

	public static final String STATUSBAR_CONNECTING_DEVICE = "Verbinde mit MIDI-Ger" + ae + "t";

	//------------------------Dialogerläuterungen-------------

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
	
	public static final String NOISE_HELP_DIALOG_TITLE = "Wichtiger Hinweis";
	public static final String NOISE_HELP_DIALOG_HEADER = "Verhalten bei St" + oe + "rger" + ae  + "uschen";
	public static final String NOISE_HELP_DIALOG_TEXT = "Achtung! Geraten Sie im Falle des Auftretens von St" + oe + "rger" + ae + "uschen nicht in Panik und bewahren Sie Ruhe! "
			+ "Verringern Sie die Samplingrate und die Pufferzeit! Achten Sie zusätzlich bitte auf die korrekte Konfiguration aller Module, eine Releasezeit von 0 sorgt oftmals auch für Knacken!"
			+ " Wenn Ihnen das Programm zu leise ist, verringern Sie die maximale Polyphonie!";
	
	public static final String MODULE_NAME_INPUT_DIALOG_TITLE = "Modulnamen eingeben";
	public static final String MODULE_NAME_INPUT_DIALOG_TEXT = "Bitte geben Sie einen Namen f" + ue + "r das Modul an: ";
	
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
	
	//----------------------------Menü----------------------------
	
	public static final String MENU_FILE = "Datei";
	public static final String MENU_SAVE = "Speichern";
	public static final String MENU_LOAD = "Laden";
	public static final String MENU_BUILD = "In Synthesizer laden";
	
	public static final String TITLE_SAVE_SYNTHESIZER = "Synthesizer speichern";
	public static final String TITLE_LOAD_SYNTHESIZER = "Synthesizer laden";
	
	public static final String TITLE_MIXER_DIALOG = "Anzahl an Eing" + ae + "ngen";
	public static final String HEADER_MIXER_DIALOG = "Bitte geben Sie die Anzahl an Eing" + ae + "ngen des Mixers an!";
	public static final String TEXT_MIXER_DIALOG = "Anzahl an Eing" + ae + "ngen:";
	
	public static final String TITLE_ZERO_DIALOG = "Nullzusetzende Module";
	public static final String HEADER_ZERO_DIALOG = "Bitte w" + ae + "hlen Sie aus, welche Module bei Stoppen des Tones ausgeschaltet werden sollen.";
	
	public static final String SAVE_SYNTH_DEFAULT_FILENAME = "synthesizer.xml";
	
	public static final String ADD_MODULE_MENU_ITEM_TEXT = "Modul hinzuf" + ue + "gen";
	public static final String CLEAR_MENU_ITEM_TEXT = "Alles l" + oe + "schen";
	public static final String ZERO_MODULES_MENU_ITEM_TEXT = "Nullzusetzende Konstanten festlegen";


}
