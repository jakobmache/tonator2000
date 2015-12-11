package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;

import modules.Ids;

import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import org.controlsfx.control.action.Action;
import org.controlsfx.tools.Borders;

import resources.Strings;
import engine.Module;
import engine.SynthesizerEngine;

//TODO: Midi-Player Fenster
//TODO: Fix disablen
public class MainApplication extends Application 
{

	public static final int OVERLAY_MIDI = 0;

	private Stage primaryStage;
	private BorderPane rootLayout;
	private SynthesizerEngine engine;

	private HBox synthesizerLayout;

	private SynthiStatusBar statusBar;
	private NotificationPane overlayPane;

	private MenuController menuController;
	private List<ModuleController> moduleControllers = new ArrayList<ModuleController>();

	private int currProgram = 0;

	@Override
	public void start(Stage primaryStage)
	{
		initStage(primaryStage);
		initEngine();

		initRootLayout();
		
		initEventHandlers();

		updateStatusBar();
		
		URL iconUrl = getClass().getClassLoader().getResource("resources/icon.png");
		primaryStage.getIcons().add(new Image(iconUrl.toString()));
		
		showStandardConfiguration();
		
		setCurrProgram(0);
		update();

		primaryStage.show();

		showOverlay(OVERLAY_MIDI);

//		SynthesizerEditor editor = new SynthesizerEditor(this, engine);
//		editor.show();
		engine.run();
	}

	private void initEngine()
	{
		try 
		{
			this.engine = new SynthesizerEngine();
		} 

		catch (LineUnavailableException e) 
		{
			e.printStackTrace();
			Alert alert = UiUtils.generateExceptionDialog(primaryStage, e, Strings.ERROR_TITLE, Strings.ERROR_HEADERS[Strings.ERROR_AUDIO], 
					Strings.ERROR_EXPLANATIONS[Strings.ERROR_AUDIO]);
			alert.showAndWait();
		}

		catch (IOException e) 
		{
			e.printStackTrace();
			Alert alert = UiUtils.generateExceptionDialog(primaryStage, e, Strings.ERROR_TITLE, Strings.ERROR_HEADERS[Strings.ERROR_UNKNOWN], 
					Strings.ERROR_EXPLANATIONS[Strings.ERROR_UNKNOWN]);
			alert.showAndWait();
		}
	}

	private void initStage(Stage primaryStage)
	{
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(Strings.APPLICATION_NAME + " - " + Strings.VERSION_NUMBER);
	}

	private void initRootLayout() 
	{
		try 
		{
			menuController = new MenuController(engine, this);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("fxml/RootLayout.fxml"));

			loader.setController(menuController);
			rootLayout = (BorderPane) loader.load();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		synthesizerLayout = new HBox(5);
		initStaticModules(rootLayout);

		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
	}

	private void initStaticModules(BorderPane rootLayout)
	{
		VBox mainLayout = new VBox(5);
		mainLayout.setMaxHeight(Double.MAX_VALUE);
		mainLayout.setMaxWidth(Double.MAX_VALUE);
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(synthesizerLayout);
		scrollPane.setMaxHeight(Double.MAX_VALUE);
		scrollPane.setMaxWidth(Double.MAX_VALUE);
		
		VBox.setVgrow(scrollPane, Priority.ALWAYS);
		
		mainLayout.getChildren().add(scrollPane);

		//Statusbar und Overlay initialisieren
		statusBar = new SynthiStatusBar(engine, this);
		rootLayout.setBottom(statusBar);
		overlayPane = new NotificationPane(scrollPane);

		mainLayout.getChildren().add(overlayPane);

		//Plotter initialisieren
		Node plotter = null;
		try 
		{
			plotter = UiUtils.generateModuleGui(engine, this, Module.PLOTTER, null);
			initMouseHandler(plotter, Module.PLOTTER);
			mainLayout.getChildren().add(plotter);
			VBox.setVgrow(plotter, Priority.ALWAYS);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		rootLayout.setCenter(mainLayout);
	}

	private void showStandardConfiguration()
	{
		VBox osciBox = new VBox();
		osciBox.setSpacing(5);
		HBox balanceBox = new HBox();
		balanceBox.setSpacing(5);

		try
		{
			Node oscillator1 = UiUtils.generateModuleGui(engine, this, Module.OSCILLATOR, new int[]{Ids.ID_OSCILLATOR_1, Ids.ID_CONSTANT_OSCITYPE_1});
			Node oscillator2 = UiUtils.generateModuleGui(engine, this, Module.OSCILLATOR, new int[]{Ids.ID_OSCILLATOR_2, Ids.ID_CONSTANT_OSCITYPE_2});
			Node lowpass = UiUtils.generateModuleGui(engine, this, Module.LOWPASS, new int[]{Ids.ID_LOWPASS_1, Ids.ID_CONSTANT_CUTOFF_1, Ids.ID_CONSTANT_RESONANCE_1});
			Node envelope1 = UiUtils.generateModuleGui(engine, this, Module.ENVELOPE, new int[]{Ids.ID_ENVELOPE_1, Ids.ID_CONSTANT_ATTACK_1, Ids.ID_CONSTANT_DECAY_1,
					Ids.ID_CONSTANT_SUSTAIN_1, Ids.ID_CONSTANT_RELEASE_1, Ids.ID_CONSTANT_STEEPNESS_1});
			Node envelope2 = UiUtils.generateModuleGui(engine, this, Module.ENVELOPE, new int[]{Ids.ID_ENVELOPE_2, Ids.ID_CONSTANT_ATTACK_2, Ids.ID_CONSTANT_DECAY_2,
					Ids.ID_CONSTANT_SUSTAIN_2, Ids.ID_CONSTANT_RELEASE_2, Ids.ID_CONSTANT_STEEPNESS_2});
			Node volume = UiUtils.generateModuleGui(engine, this, Module.VOLUME, new int[]{Ids.ID_VOLUME});
			Node balance = UiUtils.generateModuleGui(engine, this, Module.BALANCED_MIXER, new int[]{Ids.ID_MIXER_2, Ids.ID_OSCILLATOR_1, Ids.ID_OSCILLATOR_2, Ids.ID_CONSTANT_OSCIBALANCE_1});

			osciBox.getChildren().add(oscillator1);
			osciBox.getChildren().add(oscillator2);
			
			balanceBox.getChildren().add(osciBox);
			balanceBox.getChildren().add(balance);
			synthesizerLayout.getChildren().add(balanceBox);
			synthesizerLayout.getChildren().add(envelope1);
			synthesizerLayout.getChildren().add(lowpass);
			synthesizerLayout.getChildren().add(envelope2);
			synthesizerLayout.getChildren().add(volume);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}	
	}

	public void updateStatusBar()
	{
		statusBar.update();
	}

	private void initEventHandlers()
	{
		primaryStage.setOnCloseRequest((event) -> {
			engine.close();
		});      
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void showOverlay(int type)
	{
		if (type == OVERLAY_MIDI)
		{
			overlayPane.setText(Strings.OVERLAY_MIDI_STRING);
			overlayPane.getActions().add(new Action("MIDI-Ger" + Strings.ae + "t ausw" + Strings.ae + "hlen", ae ->
			{
				menuController.onSelectMidiDeviceAction(ae);	
				overlayPane.hide();
			}));
			overlayPane.getActions().add(new Action("MIDI-Datei laden", ae ->
			{
				menuController.onOpenMidiPlayer(ae);
				menuController.getMidiPlayerController().onOpenAction(ae);
				
				overlayPane.hide();
			}));
		}

		overlayPane.show();
	}

	public void setCurrProgram(int newProgram)
	{
		currProgram = newProgram;
		for (ModuleController controller:moduleControllers)
		{
			controller.setCurrProgram(currProgram);
		}
	}

	public void update()
	{
		for (ModuleController controller:moduleControllers)
		{
			controller.loadData();
		}
	}

	public void initMouseHandler(Node pane, int module)
	{
		pane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> 
		{
			if (event.getClickCount() == 2)
			{
				PopOver popOver = new PopOver();
				popOver.setDetachable(true);
				popOver.setDetachedTitle(Strings.MODULE_NAMES[module]);
				popOver.setDetached(true);
				popOver.setArrowLocation(ArrowLocation.TOP_CENTER);

				VBox content = new VBox();
				Label mainInfo = new Label(Strings.TOOLTIPS[module]);
				mainInfo.setMaxWidth(500);
				mainInfo.setWrapText(true);
				Node borderedInfo = Borders.wrap(mainInfo).lineBorder().buildAll();
				content.getChildren().add(borderedInfo);

				//Modul hat Parameter zum Darstellen
				if (Strings.PARAM_NAMES_MAIN.length > module)
				{
					String[] paramNames = Strings.PARAM_NAMES_MAIN[module];
					String[] paramDescriptions = Strings.PARAM_DESCRIPTIONS[module];
					for (int i = 0; i < paramNames.length; i++)
					{
						Node label = createTitledPane(paramNames[i], paramDescriptions[i]);
						content.getChildren().add(label);
					}
				}
				popOver.setContentNode(content);

				popOver.show(pane);
			}
		});
	}

	private Node createTitledPane(String title, String text) 
	{
		Label label = new Label(text);
		label.setWrapText(true);
		label.setMaxWidth(500);
		Node borderedLabel = Borders.wrap(label).etchedBorder().title(title).buildAll();
		return borderedLabel;
	}

	public List<ModuleController> getModuleControllers()
	{
		return moduleControllers;
	}

	public int getCurrProgram()
	{
		return currProgram;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
