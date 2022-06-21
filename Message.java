import  java.io.*;

public class Message{
	boolean		checked = false;
	
	String		year;
	String		month;
	String		date;
	String		hour;
	String		minute;
	String		second;

	String		lat;
	String		lon;

	String		from;
	String		title;
	String		body;


	public Message(){
	}

	public Message(String from,String title,String body){
		this.from = from;
		this.title = title;
		this.body  = body;
	}

	public Message(String file){
		try{
			FileInputStream fin = new FileInputStream(file);
			InputStreamReader is = new InputStreamReader(fin);
			BufferedReader reader = new BufferedReader(is);

			String line1 = reader.readLine();
			if(line1.equals("checked")){
				checked = true;
			}
			else{
				checked = false;
			}

			String line2 = reader.readLine();
			String [] cols = line2.split(",");
			year	= cols[0];
			month	= cols[1];
			date	= cols[2];
			hour	= cols[3];
			minute	= cols[4];
			second	= cols[5];
			
			String line3 = reader.readLine();
			String [] latlon = line3.split(",");
			lat = latlon[0];
			lon = latlon[1];

			String line4 = reader.readLine();
			from = line4;

			String line5 = reader.readLine();
			title = line5;

			StringBuilder sb = new StringBuilder();
			while(true){
				String line = reader.readLine();
				if(line == null){
					break;
				}
				else{
					sb.append(line + "\n");
				}
			}

			body = sb.toString();

			reader.close();
			is.close();
			fin.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();

		if(checked){
			sb.append("checked\n");
		}
		else{
			sb.append("unchecked\n");
		}

		sb.append(year + "," + month + "," + date + "," + hour + "," + minute + "," + second + "\n");
		sb.append(lat + "," + lon + "\n");
		sb.append(from + "\n");
		sb.append(title + "\n");
		sb.append(body);

		return sb.toString();
	}
}
