package zhwy.gis;

public class GisPoint {
	public String StationName="";
	public double Lon = -9999;
	public double Lat = -9999;
	public double Value = 0;

	public double ColIndex = -9999;
	public double RowIndex = -9999;
	public double Distance = 0;

	public GisPoint(String stationName,double lon, double lat, double value) {
		StationName=stationName;
		Lon = lon;
		Lat = lat;
		Value = value;
	}

	@Override
	public String toString() {
		return "GisPoint [StationName=" + StationName + ", Lon=" + Lon
				+ ", Lat=" + Lat + ", Value=" + Value + ", ColIndex="
				+ ColIndex + ", RowIndex=" + RowIndex + ", Distance="
				+ Distance + "]";
	}
	
}