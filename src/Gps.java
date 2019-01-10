public class Gps {
 
	private double wgLat;
	private double wgLon;
 
	public Gps(double wgLon, double wgLat) {
		setWgLat(wgLat);
		setWgLon(wgLon);
	}
 
	public double getWgLat() {
		return wgLat;
	}
 
	public void setWgLat(double wgLat) {
		this.wgLat = wgLat;
	}
 
	public double getWgLon() {
		return wgLon;
	}
 
	public void setWgLon(double wgLon) {
		this.wgLon = wgLon;
	}
 
	@Override
	public String toString() {
		return wgLon + "," + wgLat;
	}
}
