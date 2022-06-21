import  java.io.*;
import  java.util.ArrayList;

public class PointLoader{
	static Point  north = null;
	static Point  south = null;
	static Point  east  = null;
	static Point  west  = null;

	public static Point[] loadObject(String file){
		Point [] points = null;

		try{
			FileInputStream fin = new FileInputStream(file);
			InputStreamReader is = new InputStreamReader(fin);
			BufferedReader reader = new BufferedReader(is);

			ArrayList <Point> array = new ArrayList <Point> ();
			while(true){
				String line = reader.readLine();
				if(line == null){
					break;
				}

				try{
					String [] cols = line.split(",");
					// System.out.println(line);
					if(cols.length >= 3){
						Point point = new Point(cols[1],cols[2]);

						if(cols[0].length() > 0){
							point.name = cols[0];
						}

						array.add(point);
						// count++;
					}
				}
				catch(Exception e){
					// System.out.println(e.toString());
				}
			}


			reader.close();
			is.close();
			fin.close();

			points = new Point [array.size()];
			for(int i=0;i<points.length;i++){
				points[i] = array.get(i);
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		return points;
	}

	public static Point [] load(String file){
		Point [] points = null;
		try{
			FileInputStream fin = new FileInputStream(file);
			InputStreamReader is = new InputStreamReader(fin);
			BufferedReader reader = new BufferedReader(is);

			ArrayList <Point> array = new ArrayList <Point> ();
			while(true){
				String line = reader.readLine();
				if(line == null){
					break;
				}

				try{
					String [] cols = line.split(",");
					if(cols[2].equals("A")){
						Point point = null;
						if(cols[0].equals("$GPRMC")){
							// System.out.println(line);
							point = new Point(cols[3],cols[5]);
							point.name = "<" + cols[3] + "," + cols[5] + ">";
							point.setSpeed(cols[7]);

							if(north == null){
								north = point;
							}
							else{
								if(north.lat > point.lat){
									north = point;
								}
							}
							
							if(south == null){
								south = point;
							}
							else{
								if(south.lat < point.lat){
									south = point;
								}
							}

							if(west == null){
								west = point;
							}
							else{
								if(west.lon > point.lon){
									west = point;
								}
							}
							
							if(east == null){
								east = point;
							}
							else{
								if(east.lon < point.lon){
									east = point;
								}
							}
							
							array.add(point);
						//	count++;
						}
					}
				}
				catch(Exception e){
					System.out.println(e.toString());
				}
			}


			reader.close();
			is.close();
			fin.close();

			points = new Point [array.size()];
			for(int i=0;i<points.length;i++){
				points[i] = array.get(i);
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}

		return points;
	}

	public static void main(String args[]){
		Point [] points = PointLoader.loadObject(args[0]);

		for(Point p : points){
			System.out.println(p.toString());
		}

		load(args[0]);

		System.out.println("NORTH " + north.toString());
		System.out.println("SOUTH " + south.toString());
		System.out.println("EAST " + east.toString());
		System.out.println("WEST " + west.toString());
	}
}
