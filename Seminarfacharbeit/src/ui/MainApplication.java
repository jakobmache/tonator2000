package ui;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import containers.StandardModuleContainer;
import modules.Envelope;
import modules.LowpassFilter;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import engine.SynthesizerEngine;

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

    @Override
    public void start(Stage primaryStage)
    {
    	try {
			this.engine = new SynthesizerEngine();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Seminarfacharbeit");
        
        synthesizerLayout = new HBox(5);

        initRootLayout();

        initOscillator();
        initFilter();
        initEnvelope();
        
        initStatusBar();
        
        initEventHandlers();
        
        updateStatusBar();
        
        primaryStage.show();
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
            
            rootLayout.setCenter(synthesizerLayout);
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
        	LowpassFilterController controller = new LowpassFilterController(engine, filter);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApplication.class.getResource("fxml/LowpassFilterLayout.fxml"));

            loader.setController(controller);
            TitledPane filterView = (TitledPane) loader.load();
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
            controller.init();

            synthesizerLayout.getChildren().add(envelopeView);
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
