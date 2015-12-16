package ui;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javafx.animation.AnimationTimer;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import modules.Ids;
import modules.SampleFilter;
import containers.StandardModuleContainer;
import engine.SynthesizerEngine;

public class Plotter extends VBox
{

	public static final int Y_START_BOUND = 4000;

	private int maxDataPoints = 500;

	private SampleFilter sampleFilter;

	private int xSeriesData = 0;
	private XYChart.Series<Number, Number> series = new XYChart.Series<>();

	private ExecutorService executor;
	private ConcurrentLinkedQueue<Float> dataQ = new ConcurrentLinkedQueue<>();

	private NumberAxis xAxis;

	public Plotter(SynthesizerEngine engine)
	{
		sampleFilter = ((SampleFilter) ((StandardModuleContainer) engine.getAllContainer()).findModuleById(Ids.ID_SAMPLE_FILTER_1));
		
		xAxis = new NumberAxis(0, maxDataPoints, 1000);
		xAxis.setForceZeroInRange(false);
		xAxis.setAutoRanging(false);
		xAxis.setTickLabelsVisible(false);
		xAxis.setTickMarkVisible(false);
		xAxis.setMinorTickVisible(false);

		NumberAxis yAxis = new NumberAxis();
		yAxis.setAutoRanging(false);
		yAxis.setUpperBound(Short.MAX_VALUE);
		yAxis.setLowerBound(Short.MIN_VALUE);
		yAxis.setTickUnit(10000);

		LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
		lineChart.setCreateSymbols(false);

		lineChart.setAnimated(false);
		lineChart.setHorizontalGridLinesVisible(true);
		lineChart.setLegendVisible(false);
		
		setVgrow(lineChart, Priority.ALWAYS);
		lineChart.setMaxHeight(Double.MAX_VALUE);

		lineChart.getData().add(series);

		getChildren().add(lineChart);

		executor = Executors.newCachedThreadPool(new ThreadFactory() 
		{
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setDaemon(true);
				return thread;
			}
		});

		AddToQueue addToQueue = new AddToQueue();
		executor.execute(addToQueue);

		prepareTimeline();

		lineChart.setMaxWidth(Double.MAX_VALUE);
		lineChart.setMaxHeight(Double.MAX_VALUE);
		
		setOnScroll((event) -> {
			double upperBound = yAxis.getUpperBound() - 5 * event.getDeltaY();
			double lowerBound = yAxis.getLowerBound() + 5 * event.getDeltaY();
			if (upperBound > Short.MAX_VALUE)
				yAxis.setUpperBound(Short.MAX_VALUE);
			else if (upperBound <= 0)
				yAxis.setUpperBound(yAxis.getUpperBound());
			else
				yAxis.setUpperBound(upperBound);
			
			if (lowerBound < Short.MIN_VALUE)
				yAxis.setLowerBound(Short.MIN_VALUE);
			else if (lowerBound >= 0)
				yAxis.setLowerBound(yAxis.getLowerBound());
			else
				yAxis.setLowerBound(lowerBound);
		});
		
		yAxis.setUpperBound(Y_START_BOUND);
		yAxis.setLowerBound(-1 * Y_START_BOUND);

	}

	  private class AddToQueue implements Runnable {
	        public void run() {
	            try {
	            	for (float sample:sampleFilter.getBufferList())
	            		dataQ.add(sample);

	                Thread.sleep(50);
	                executor.execute(this);
	            } catch (InterruptedException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }

	    //-- Timeline gets called in the JavaFX Main thread
	    private void prepareTimeline() {
	        // Every frame to take any data from queue and add to chart
	        new AnimationTimer() {
	            @Override public void handle(long now) {
	                	addDataToSeries();
	            }
	        }.start();
	    }

	    private void addDataToSeries() 
	    {
	        for (int i = 0; i < 200; i++)
	        {
	        	if (dataQ.isEmpty())
	        		break;
	        	series.getData().add(new XYChart.Data<Number, Number>(xSeriesData++, dataQ.remove()));
	        }
	        // remove points to keep us at no more than MAX_DATA_POINTS
	        if (series.getData().size() > maxDataPoints) {
	            series.getData().remove(0, series.getData().size() - maxDataPoints);
	        }
	        // update 
	        xAxis.setLowerBound(xSeriesData-maxDataPoints);
	        xAxis.setUpperBound(xSeriesData-1);
	    }



}
