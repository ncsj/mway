import  java.io.*;
import  java.util.ArrayList;

public class UserManager{
	public static Point getCurrentPoint(User user){
		Point point = null;
		try{
			FileInputStream fin = new FileInputStream(user.home + "/current");
			InputStreamReader is = new InputStreamReader(fin);
			BufferedReader reader = new BufferedReader(is);
			
			String line = reader.readLine();
			String [] cols = line.split(",");

			reader.close();
			is.close();
			fin.close();

			point = new Point();
			point.lat = Double.valueOf(cols[0]).doubleValue();
			point.lon = Double.valueOf(cols[1]).doubleValue(); 
		}
		catch(Exception e){
			System.out.println(e.toString());
			point = new Point();
		}

		return point;
	}

	public static User login(String id,String pw) throws UserException{
		User user = null;
		
		try{
			FileInputStream fin = new FileInputStream("etc/passwd");
			InputStreamReader is = new InputStreamReader(fin);
			BufferedReader reader = new BufferedReader(is);

			while(true){
				String line = reader.readLine();
				String [] cols = line.split(":");

				if(cols[0].equals(id) && cols[1].equals(pw)){
					user = new User();
					user.id = cols[0];
					user.name = cols[0];
					user.home = cols[2];

					user.point = getCurrentPoint(user);

					break;
				}
				else if(line == null){
					break;
				}
			}
			reader.close();
			is.close();
			fin.close();

			if(user == null){
				System.out.println("throw!");
				throw new UserException();
			}

		}
		catch(Exception e){
		}

		return user;
	}

	public static User [] list(){
		User [] userlist = null;
		try{
			ArrayList <User> array = new ArrayList <User> ();
			FileInputStream fin = new FileInputStream("etc/passwd");
			InputStreamReader is = new InputStreamReader(fin);
			BufferedReader reader = new BufferedReader(is);

			while(true){
				String line = reader.readLine();
				if(line != null){
					String [] cols = line.split(":");
					User user = new User();
					user.id = cols[0];
					user.name = cols[0];
					user.home = cols[2];
					user.point = getCurrentPoint(user);

					array.add(user);
				}
				else{
					break;
				}

			}
			reader.close();
			is.close();
			fin.close();

			userlist = new User [array.size()];
			for(int i=0;i<userlist.length;i++){
				userlist[i] = array.get(i);
			}

		}
		catch(Exception e){
		}

		return userlist;
	}
}
