package test;

import org.controlsfx.tools.Borders;
import org.omg.CORBA.PRIVATE_MEMBER;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

/** Example of dragging anchors around to manipulate a line. */
public class LineManipulator extends Application {
	
	private Pane editor;
	
	private static final int WIDTH = 2000;
	private static final int HEIGHT = 2000;
	
  public static void main(String[] args) throws Exception { launch(args); }
  
  @Override public void start(final Stage stage) throws Exception 
  {

	editor = new Pane();
	
	editor.setPrefWidth(WIDTH);
	editor.setPrefHeight(HEIGHT);
	
	ScrollPane scrollPane = new ScrollPane(editor);
	
	Scene scene = new Scene(scrollPane, 1000, 500);
	
    stage.setScene(scene);
    stage.show();
    
    scene.setOnKeyPressed((event) -> 
    {
    	if (event.getCode() == KeyCode.A)
    	{
    		System.out.println("A");
    		addAnchors(100, 100, 400, 400);
    	}
    });
  }
  
  public void addAnchors(double startX, double startY, double endX, double endY)
  {
	    DoubleProperty startXProperty = new SimpleDoubleProperty(startX);
	    DoubleProperty startYProperty = new SimpleDoubleProperty(startY);
	    DoubleProperty endXProperty   = new SimpleDoubleProperty(endX);
	    DoubleProperty endYProperty   = new SimpleDoubleProperty(endY);

	    Anchor start    = new Anchor(Color.PALEGREEN, startXProperty, startYProperty);
	    Anchor end      = new Anchor(Color.TOMATO,    endXProperty,   endYProperty);
	    
	    Node startNode = Borders.wrap(start).lineBorder().title("Node").buildAll();
	    Node endNode = Borders.wrap(end).lineBorder().title("Node").buildAll();
	    
	   
	    
	    Line line = new BoundLine(startXProperty, startYProperty, endXProperty, endYProperty);
	    
	    editor.getChildren().addAll(startNode, endNode, line);
  }

  class BoundLine extends Line {
    BoundLine(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
      startXProperty().bind(startX);
      startYProperty().bind(startY);
      endXProperty().bind(endX);
      endYProperty().bind(endY);
      setStrokeWidth(2);
      setStroke(Color.GRAY.deriveColor(0, 1, 1, 0.5));
      setStrokeLineCap(StrokeLineCap.BUTT);
      getStrokeDashArray().setAll(10.0, 5.0);
      setMouseTransparent(true);
    }
  }

  // a draggable anchor displayed around a point.
  class Anchor extends Circle { 
    Anchor(Color color, DoubleProperty x, DoubleProperty y) {
      super(x.get(), y.get(), 10);
      setFill(color.deriveColor(1, 1, 1, 0.5));
      setStroke(color);
      setStrokeWidth(2);
      setStrokeType(StrokeType.OUTSIDE);

      x.bind(centerXProperty());
      y.bind(centerYProperty());
      enableDrag();
    }

    // make a node movable by dragging it around with the mouse.
    private void enableDrag() {
      final Delta dragDelta = new Delta();
      setOnMousePressed(new EventHandler<MouseEvent>() {
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

}

class Center {
    private ReadOnlyDoubleWrapper centerX = new ReadOnlyDoubleWrapper();
    private ReadOnlyDoubleWrapper centerY = new ReadOnlyDoubleWrapper();

    public Center(Node node) {
        calcCenter(node.getBoundsInParent());
        node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            @Override public void changed(
                   ObservableValue<? extends Bounds> observableValue, 
                   Bounds oldBounds, 
                   Bounds bounds
            ) {
                calcCenter(bounds);
            }
        });
    }

    private void calcCenter(Bounds bounds) {
        centerX.set(bounds.getMinX() + bounds.getWidth()  / 2);
        centerY.set(bounds.getMinY() + bounds.getHeight() / 2);
    }

    ReadOnlyDoubleProperty centerXProperty() {
        return centerX.getReadOnlyProperty();
    }

    ReadOnlyDoubleProperty centerYProperty() {
        return centerY.getReadOnlyProperty();
    }
}


