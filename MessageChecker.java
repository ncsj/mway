import  java.io.*;

public class MessageChecker{
	User user =  null;
	public MessageChecker(User user){
		this.user = user;
	}

	public int check(){
		return unchecked;
	}

	int unchecked = 0;
	public int count(){
		int n = 0;
		unchecked = 0;

		String path = user.home + "/message";
		File dir = new File(path);
		String [] files = dir.list();
		for(String file : files){
			n++;
			if(checkFile(path + "/" + file)){
				unchecked++;
			}
		}

		return n;
	}

	boolean checkFile(String file){
		boolean rtc = false;
		
		try{
			FileInputStream fin = new FileInputStream(file);
			InputStreamReader is = new InputStreamReader(fin);
			BufferedReader reader = new BufferedReader(is);
			
			String line = reader.readLine();
			if(line.equals("unchecked")){
				rtc = true;
			}

			reader.close();
			is.close();
			fin.close();
		}
		catch(Exception e){
		}

		return rtc;
	}
}
