package infoborden;

import tijdtools.InfoBoardTimeFunctions;

public class JSONMessage {
	private int tijd;
	private int aankomsttijd;
	private String lijnNaam;
	private String busID;
	private String bedrijf;
	private String eindpunt;

	public JSONMessage(int tijd, int aankomsttijd, String lijnNaam, String busID, String bedrijf, String eindpunt) {
		super();
		this.tijd = tijd;
		this.aankomsttijd = aankomsttijd;
		this.lijnNaam = lijnNaam;
		this.busID = busID;
		this.bedrijf = bedrijf;
		this.eindpunt = eindpunt;
	}

	public JSONMessage(){	}

	public int getTijd() {
		return tijd;
	}

	public void setTijd(int tijd) {
		this.tijd = tijd;
	}

	public int getArrivalTime() {
		return aankomsttijd;
	}

	public void setAankomsttijd(int aankomsttijd) {
		this.aankomsttijd = aankomsttijd;
	}

	public String getLijnNaam() {
		return lijnNaam;
	}

	public void setLijnNaam(String lijnNaam) {
		this.lijnNaam = lijnNaam;
	}

	public String getBusID() {
		return busID;
	}

	public void setBusID(String busID) {
		this.busID = busID;
	}

	public String getBedrijf() {
		return bedrijf;
	}

	public void setBedrijf(String bedrijf) {
		this.bedrijf = bedrijf;
	}

	public String getEindpunt() {
		return eindpunt;
	}

	public void setEindpunt(String eindpunt) {
		this.eindpunt = eindpunt;
	}

	public String getInfoTextLine() {
		InfoBoardTimeFunctions tijdFuncties = new InfoBoardTimeFunctions();
		String tijd = tijdFuncties.getFormattedTimeFromCounter(aankomsttijd);
		String regel = String.format("%8s - %5s - %12s", this.lijnNaam, this.eindpunt, tijd);
		return regel;
	}

	@Override
	public String toString() {
		return "JSONBericht [tijd=" + tijd + ", aankomsttijd=" + aankomsttijd + ", lijnNaam=" + lijnNaam + ", busID="
				+ busID + ", bedrijf=" + bedrijf + ", eindpunt=" + eindpunt + "]";
	}
}
