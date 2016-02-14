	package test;
	
	import org.controlsfx.tools.Borders;
	
	import javafx.geometry.Pos;
	import javafx.scene.Node;
	import javafx.scene.input.MouseButton;
	import javafx.scene.layout.HBox;
	import javafx.scene.layout.Pane;
	
	public class Module extends Pane
	{
		private double oldX;
		private double oldY;
		
		private Port circle;
	
		public Module()
		{
			HBox root = new HBox(5);
			root.setAlignment(Pos.CENTER);
			circle = new Port();
			root.getChildren().add(circle);
	
			getChildren().add(root);
		}
	
		public Node getGui()
		{
			Node returnPane = Borders.wrap(this).lineBorder().title("Test").buildAll();
	
			returnPane.setOnMousePressed((event) ->
			{
				if (event.getButton() == MouseButton.PRIMARY)
				{
					oldX = returnPane.getLayoutX() - event.getSceneX();
					oldY = returnPane.getLayoutY() - event.getSceneY();
				}
			});
	
			returnPane.setOnMouseDragged((event) -> 
			{
				if (event.getButton() == MouseButton.PRIMARY)
				{
					double newX = event.getSceneX() + oldX;
					if (newX > 0 && newX < returnPane.getScene().getWidth()) 
					{
						returnPane.setLayoutX(newX);
					}  
					double newY = event.getSceneY() + oldY;
					if (newY > 0 && newY < returnPane.getScene().getHeight()) 
					{
						returnPane.setLayoutY(newY);
					}  
				}
			});
			
			returnPane.layoutXProperty().addListener((obs, newValue, oldValue) -> 
			{
				circle.update();
			});
			
			returnPane.layoutYProperty().addListener((obs, newValue, oldValue) -> 
			{
				circle.update();
			});
	
			return returnPane;
		}
		
		public Port getPort()
		{
			return circle;
		}
	}
