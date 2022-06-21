public
class Longitude{
	public static void main(String args[]){
		double lat = Double.valueOf(args[0]).doubleValue();
		double rad = Math.toRadians(lat);
		double lon = Math.cos(rad);

		double min = 1852.25 * lon;

		System.out.println("min : " + min);
	}
}
