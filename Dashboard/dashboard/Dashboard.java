package dashboard;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mockDatabaseLogger.ArrivaLogger;

public class Dashboard extends Application {

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		DashBoardRunners runners = new DashBoardRunners();
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		pane.setHgap(5.5);
		pane.setVgap(5.5);
		pane.add(new Label("Halte:"), 0, 0);
		TextField halteInput = new TextField("A-Z");
		pane.add(halteInput, 1, 0);
		pane.add(new Label("Richting:"), 0, 1);
		TextField richting = new TextField("1 of -1");
		pane.add(richting, 1, 1);
		Button btBord = new Button("Start Bord");
		btBord.setOnAction(e -> {
			runners.startBord(halteInput.getText(), richting.getText());
		});
		Button btStart = new Button("Start");
		btStart.setOnAction( e -> {
			runners.startAlles();
		});
		Button btLogger = new Button("Start Logger");
		btLogger.setOnAction( e -> {
			runners.thread(new ArrivaLogger(), false);
		});
		pane.add(btBord, 1, 5);
		pane.add(btStart, 2, 5);
		pane.add(btLogger, 3, 5);
		GridPane.setHalignment(btBord, HPos.LEFT);
		GridPane.setHalignment(btStart, HPos.LEFT);
		GridPane.setHalignment(btLogger, HPos.LEFT);
		Scene scene = new Scene(pane);
		primaryStage.setTitle("BusSimulatie control-Center");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * The main method is only needed for the IDE with limited
	 * JavaFX support. Not needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);
	}
} 