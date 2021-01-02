package infoborden;

/**
 * Created by Joep Scheltens on 02-01-2021.
 */
public class InfoBoardFactory {
    public InfoBoard createInfoBoard(String busStop, String busDirection){
        InfoBoard infoBoard = new InfoBoard(busStop, busDirection);
        return new InfoBoard(busStop, busDirection);
    }
}
