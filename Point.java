public class Point{
	String name = "";
	double lat;
	double lon;
	double speed;
	String	image = null;

	public Point(){
		this.lat	= 0.0;
		this.lon	= 0.0;
		this.speed	= 0.0;
	}

	public Point(String lat,String lon){
		String slat_d = String.format("%c%c",lat.charAt(0),lat.charAt(1));
		String slat_m = lat.substring(2,9);

		double dd = Double.valueOf(slat_d).doubleValue();
		double dm = Double.valueOf(slat_m).doubleValue() / 60.0;

		double dlat = dd + dm;
		// System.out.println("LAT : " + dlat);

		String slon_d = String.format("%c%c%c",lon.charAt(0),lon.charAt(1),lon.charAt(2));
		String slon_m = lon.substring(3,10);

		double ddd = Double.valueOf(slon_d).doubleValue();
		double ddm = Double.valueOf(slon_m).doubleValue() / 60.0;

		double dlon = ddd + ddm;
		// System.out.println("LON : " + dlon);

		this.lat = dlat;
		this.lon = dlon;
	}

	public void setSpeed(String s){
		double d = Double.valueOf(s).doubleValue();

		this.speed = d;
	}

	public String toString(){
		String s = name + " : " + lat + "," + lon;
		return s;
	}

	public static void main(String args[]){
		Point p = new Point(args[0],args[1]);
	}

}
