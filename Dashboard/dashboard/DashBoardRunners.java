package dashboard;

import bussimulator.RunnerFactory;
import infoborden.InfoBoardFactory;
import javafx.application.Platform;

/**
 * Created by Joep Scheltens on 02-01-2021.
 */
class DashBoardRunners {

    void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    void startBord(String halte, String richting) {
        InfoBoardFactory infoBoardFactory = new InfoBoardFactory();
        Platform.runLater(new Runnable() {
            public void run() {
                infoBoardFactory.createInfoBoard(halte,richting);
            }
        });
    }

    void startAlles() {
        RunnerFactory factory = new RunnerFactory();
        thread(factory.createRunner(),false);
    }
}
