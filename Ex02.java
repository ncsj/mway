import  java.io.*;
import  java.util.ArrayList;

public class Ex02{
	public static void main(String args[]){
		try{
			String fname = "yamanakako-20180312-10Hz.csv";
			FileInputStream fin = new FileInputStream(fname);
			InputStreamReader is = new InputStreamReader(fin);
			BufferedReader reader = new BufferedReader(is);

			int err   = 0;
			int count = 0;
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
							System.out.println(line);
							point = new Point(cols[3],cols[5]);
							point.setSpeed(cols[7]);

							array.add(point);
							count++;
						}
					}
				}
				catch(Exception e){
					// System.out.println(e.toString());
					err++;
				}
			}

			System.out.println("COUNT : " + count);
			System.out.println("ERROR : " + err);

			reader.close();
			is.close();
			fin.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
}
