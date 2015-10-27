package ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;

import modules.Constant;
import modules.LowpassFilter;
import resources.Strings;
import containers.StandardModuleContainer;
import engine.SynthesizerEngine;

//TODO: H�llkurve--> Mit Decay knackt es, Attack bei 0 falsch, stellenweise Wert�berschreitungen
//TODO: Midi-Player Fenster
//TODO: Konstanten
//TODO: Presets--> Container und Module
public class MainApplication extends Application {
	
    private Stage primaryStage;
    private BorderPane rootLayout;
    private SynthesizerEngine engine;
    
    private StatusBarController statusBarController;
    
    @FXML 
    private CheckMenuItem monoMenuItem;
    @FXML
    private CheckMenuItem stereoMenuItem;
    
    private HBox synthesizerLayout;
    private VBox mainLayout;

    @Override
    public void start(Stage primaryStage)
    {
    	try 
    	{
			this.engine = new SynthesizerEngine();
		} 
    	catch (LineUnavailableException e) 
    	{
			e.printStackTrace();
		}
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Strings.APPLICATION_NAME + " - " + Strings.VERSION_NUMBER);
        
        synthesizerLayout = new HBox(5);
        mainLayout = new VBox(5);

        initRootLayout();

        initOscillator();
        initFilter();
        initEnvelope();
        initVolume();
        
        mainLayout.getChildren().add(synthesizerLayout);
        initPlotter();

        initStatusBar();
        
        initEventHandlers();
        
        updateStatusBar();
        
//        Alert alert = new Alert(AlertType.INFORMATION);
//        alert.setTitle(Strings.START_POPUP_TITLE);
//        alert.setHeaderText(null);
//        alert.setContentText(Strings.START_POPUP_TEXT);
//        alert.setResizable(true);
//        alert.getDialogPane().setPrefSize(480, 200);

        
        primaryStage.show();
        primaryStage.setFullScreen(true);
//        alert.showAndWait();
    }

    public void initRootLayout() 
    {
        try 
        {
        	MenuController controller = new MenuController(engine, this);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApplication.class.getResource("fxml/RootLayout.fxml"));
            
            loader.setController(controller);
            rootLayout = (BorderPane) loader.load();
            
            controller.loadData();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            
            rootLayout.setCenter(mainLayout);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    public void initStatusBar()
    {

    	
    	try 
    	{
        	FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(MainApplication.class.getResource("fxml/StatusBarLayout.fxml"));
        	
			statusBarController = new StatusBarController(engine);
			loader.setController(statusBarController);
        	
			ToolBar statusBar = (ToolBar) loader.load();
			rootLayout.setBottom(statusBar);
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    	
    	
    }

    public void initOscillator() {
        try 
        {
            OscillatorController controller = new OscillatorController(engine);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApplication.class.getResource("fxml/Oscillator.fxml"));

            loader.setController(controller);
            TitledPane oscillatorView = (TitledPane) loader.load();
            oscillatorView.setTooltip(new Tooltip(Strings.OSCILLATOR_TOOLTIP_STRING));
            controller.setMainPane(loader);

            synthesizerLayout.getChildren().add(oscillatorView);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    public void initFilter()
    {
        try 
        {
        	LowpassFilter filter = ((StandardModuleContainer) engine.getAllContainer()).getLowpassFilter();
        	Constant cutoffInput = ((StandardModuleContainer) engine.getAllContainer()).getCutoffFrequencyInput();
        	LowpassFilterController controller = new LowpassFilterController(engine, filter, cutoffInput);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApplication.class.getResource("fxml/LowpassFilterLayout.fxml"));

            loader.setController(controller);
            TitledPane filterView = (TitledPane) loader.load();
            filterView.setTooltip(new Tooltip(Strings.LOWPASS_TOOLTIP_STRING));
            controller.init();

            synthesizerLayout.getChildren().add(filterView);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    public void initEnvelope()
    {
        try 
        {
        	EnvelopeController controller = new EnvelopeController(engine);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApplication.class.getResource("fxml/EnvelopeLayout.fxml"));

            loader.setController(controller);
            TitledPane envelopeView = (TitledPane) loader.load();
            envelopeView.setTooltip(new Tooltip(Strings.ENVELOPE_TOOLTIP_STRING));
            controller.init();

            synthesizerLayout.getChildren().add(envelopeView);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    public void initPlotter()
    {
    	Plotter plotter = new Plotter(engine);
    	plotter.setTooltip(new Tooltip(Strings.PLOTTER_TOOLTIP_STRING));
    	//synthesizerLayout.getChildren().add(plotter);
    	mainLayout.getChildren().add(plotter);
    	VBox.setVgrow(plotter, Priority.ALWAYS);
    }
    
    public void initVolume()
    {
        try 
        {
        	VolumeController controller = new VolumeController(engine);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApplication.class.getResource("fxml/VolumeLayout.fxml"));

            loader.setController(controller);
            TitledPane volumeView = (TitledPane) loader.load();
            volumeView.setTooltip(new Tooltip(Strings.VOLUME_TOOLTIP_STRING));
            controller.init();

            synthesizerLayout.getChildren().add(volumeView);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    public void updateStatusBar()
    {
    	statusBarController.updateStatusBar();
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

	public static void main(String[] args) {
		launch(args);
	}
}
