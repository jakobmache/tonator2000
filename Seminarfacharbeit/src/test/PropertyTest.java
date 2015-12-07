package test;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class PropertyTest extends Application
{

	private Pane pane;

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		initLayout();
		initBindings(); 

		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void initBindings()
	{

	}

	private void initLayout()
	{
		pane = new Pane();
		CustomCircle c1 = new CustomCircle(100, 100, 20);
		CustomCircle c2 = new CustomCircle(200, 500, 20);
		pane.getChildren().add(c1);
		pane.getChildren().add(c2);
		
		CustomLine line = new CustomLine();
		line.setStart(c1);
		line.setEnd(c2);
		pane.getChildren().add(line);
	}

	public static void main(String[] args)
	{
		launch(args);
	}

}

class CustomCircle extends Circle
{
	public CustomCircle(int x, int y, int radius)
	{
		setCenterX(x);
		setCenterY(y);
		setRadius(radius);
		setStroke(Color.BLACK);
		setFill(Color.ALICEBLUE);

		final Delta dragDelta = new Delta();
		setOnMousePressed(new EventHandler<MouseEvent>() 
				{
			@Override public void handle(MouseEvent mouseEvent) {
				// record a delta distance for the drag and drop operation.
				dragDelta.x = getCenterX() - mouseEvent.getX();
				dragDelta.y = getCenterY() - mouseEvent.getY();
				getScene().setCursor(Cursor.MOVE);
			}
		});
		setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				getScene().setCursor(Cursor.HAND);
			}
		});
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				double newX = mouseEvent.getX() + dragDelta.x;
				if (newX > 0 && newX < getScene().getWidth()) {
					setCenterX(newX);
				}  
				double newY = mouseEvent.getY() + dragDelta.y;
				if (newY > 0 && newY < getScene().getHeight()) {
					setCenterY(newY);
				}  
			}
		});
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				if (!mouseEvent.isPrimaryButtonDown()) {
					getScene().setCursor(Cursor.HAND);
				}
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				if (!mouseEvent.isPrimaryButtonDown()) {
					getScene().setCursor(Cursor.DEFAULT);
				}
			}
		});
	}

	// records relative x and y co-ordinates.
	private class Delta { double x, y; }
}


class CustomLine extends Line
{
	public void setStart(CustomCircle circle)
	{
		setStartX(circle.getCenterX());
		setStartY(circle.getCenterY());
		
		circle.centerXProperty().addListener((value, oldValue, newValue) ->
		{
			setStartX(newValue.doubleValue());
		});
		
		circle.centerYProperty().addListener((value, oldValue, newValue) ->
		{
			setStartY(newValue.doubleValue());
		});
	}
	
	public void setEnd(CustomCircle circle)
	{
		setEndX(circle.getCenterX());
		setEndY(circle.getCenterY());
		
		circle.centerXProperty().addListener((value, oldValue, newValue) ->
		{
			System.out.println("End changed X");
		});
		
		circle.centerYProperty().addListener((value, oldValue, newValue) ->
		{
			System.out.println("End changed Y");
		});
	}
}
