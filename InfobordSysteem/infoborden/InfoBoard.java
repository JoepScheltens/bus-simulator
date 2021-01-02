package infoborden;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import tijdtools.InfoBoardTimeFunctions;

public class InfoBoard extends Application{
	private final String title;
	private final Text timeText = new Text("00:00:00");

	private final Text[] infoTexts = new Text[]{new Text("De eerstvolgende bus"),
			new Text("De eestevolgende bus"), new Text("De derde bus"), new Text("De vierde bus")};

	private final String busStop;
	private final String busDirection;
	private final InfoBoardMessageHandler messages;

	public InfoBoard(String busStop, String busDirection) {
		this.title = "Bushalte " + busStop + " in busDirection " + busDirection;
		this.busStop = busStop;
		this.busDirection = busDirection;
		this.messages =new InfoBoardMessageHandler();
		start(new Stage());
	}

	/**
	 * Handles new messages that the InfoBoard receives by setting the time and it's values to the values from
	 * incoming messages.
	 */
	public void handleMessage() {
		if (messages.boardReceivedNewMessage()) {
			String[] newInfoBoardValues = messages.repaintInfoBoardValues();
			InfoBoardTimeFunctions timeFunctions = new InfoBoardTimeFunctions();
			String time = timeFunctions.getCentralTime().toString();
			timeText.setText(time);
			for(int i = 0; i <= infoTexts.length; i++){
				infoTexts[i].setText(newInfoBoardValues[i]);
			}
		};
	}

	public void updateBord() {
		Runnable updater = new Runnable() {
			@Override
			public void run() {
				handleMessage();
			}
		};
		Platform.runLater(updater);
	}

	@Override
	public void start(Stage primaryStage) {
		String selector = "JMSType = '"+ busStop + busDirection + "'";
		thread(new ListenerStarter(selector, this, messages),false);
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER_LEFT);
		pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		pane.setHgap(5.5);
		pane.setVgap(5.5);

		// Place nodes in the pane
		pane.add(new Label("Voor het laatst bijgewerkt op :"), 0, 0); 
		pane.add(timeText, 1, 0);

		for(int i = 1; i <= infoTexts.length + 1; i++){
			pane.add(new Label(Integer.toString(i)), 0, i);
			pane.add(infoTexts[i], 1, i);
		}

		// Create a scene and place it in the stage
		Scene scene = new Scene(pane,500,150);
		primaryStage.setTitle(title); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}

	public void thread(Runnable runnable, boolean daemon) {
		Thread brokerThread = new Thread(runnable);
		brokerThread.setDaemon(daemon);
		brokerThread.start();
	}
}