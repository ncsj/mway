import  java.awt.*;
import  java.awt.event.*;
import  java.util.ArrayList;
import  java.io.*;

public class Ex01 extends Frame{
	LaunchDialog	launchDialog	= null;
	User			user			= null;
	
	Point [] points = null;
	static Point north;
    static Point south;
    static Point west;
    static Point east;

	boolean locus_mode = false;

	Label label_lat = new Label("Lat : 00.00000");
	Label label_lon = new Label("Lon : 000.00000");
	
	ArrayList <Polygon> parray = new ArrayList <Polygon> ();
	ArrayList <Polygon> roadlist = new ArrayList <Polygon> ();

	ArrayList <Point> pointlist = new ArrayList <Point> ();

	public Ex01(){
		setLayout(null);

		add(label_lat);
		label_lat.setBounds(80,750,110,20);

		add(label_lon);
		label_lon.setBounds(200,750,110,20);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();
		Rectangle rect = new Rectangle();
		rect.x = 0;
		rect.y = 0;
		rect.width = dim.width;
		rect.height = dim.height;

		setBounds(rect);

		loadObject();

		loadPoints();

		Ex01MouseAdapter ma = new Ex01MouseAdapter(this);
		addMouseListener(ma);
		addMouseMotionListener(ma);

		addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e){
				openLoginDialog();
				repaint();
			}
			public void windowClosing(WindowEvent e){
				close();
			}
		});
	}

	void loadObject(){

		{
			Point [] points = PointLoader.loadObject("river/yamanakako.csv");
			Polygon polygon = new Polygon();
			for(Point p : points){
				int x = (int)((p.lon - west.lon)*b) + offset_x;
				int y = base_y - (int)((p.lat - south.lat)*b) + offset_y;

				polygon.addPoint(x,y);
			}
			parray.add(polygon);
		}

		String [] roads = {"road/R138-301.csv","road/R138-201.csv","road/R138-101.csv",
							"road/R413-001.csv","road/R413-002.csv"};
		for(String road : roads){
			Point [] ps = PointLoader.loadObject(road);
			Polygon polygon = new Polygon();
			for(Point p : ps){
				int x = (int)((p.lon - west.lon)*b) + offset_x;
				int y = base_y - (int)((p.lat - south.lat)*b) + offset_y;
				
				polygon.addPoint(x,y);
			}

			roadlist.add(polygon);
		}
	}

	void loadPoints(){
		try{
			File dir = new File("points");
			if(dir.isDirectory()){
				String [] ds = dir.list();
				for(String d : ds){
					String path = "points/" + d + "/point";
					Point point = loadPoint(path);

					pointlist.add(point);
				}
			}
		}
		catch(Exception e){
			System.out.println("ERROR : Load Points ...");
			System.out.println(e.toString());
		}
	}

	Point loadPoint(String path){
		Point point = null;
		try{
			FileInputStream fin = new FileInputStream(path);
			InputStreamReader is = new InputStreamReader(fin);
			BufferedReader reader = new BufferedReader(is);

			String name = reader.readLine();
			String latlon = reader.readLine();
			String [] str = latlon.split(",");
			String lat = str[0];
			String lon = str[1];

			point = new Point();
			point.name = name;
			point.lat = Double.valueOf(lat).doubleValue();
			point.lon = Double.valueOf(lon).doubleValue();

			reader.close();
			is.close();
			fin.close();
		}
		catch(Exception e){
			System.out.println("ERROR : Load Point ...");
			System.out.println(e.toString());
		}

		return point;
	}

	void openLoginDialog(){
		LoginDialog dlg = new LoginDialog(this);
		dlg.setVisible(true);

		try{
			String id = dlg.getID();
			String pw = dlg.getPW();

			user = UserManager.login(id,pw);
			if(user == null){
				close();
			}
			else{
				loadEnv();
			}
		}
		catch(UserException e){
			close();
		}
	}

	void loadEnv(){
		try{
			FileInputStream fin = new FileInputStream(user.home + "/username");
			InputStreamReader is = new InputStreamReader(fin);
			BufferedReader reader = new BufferedReader(is);
			String s = reader.readLine();

			setTitle(s);

			reader.close();
			is.close();
			fin.close();
		}
		catch(Exception e){
		}

		launchDialog = new LaunchDialog(this);
	}

    double b = 12000.0;
    int offset_x = 300;
    int offset_y = 50;
    int base_y   = 200;

	public void paint(Graphics g){
        g.setColor(Color.white);
        g.fillRect(50,50,1180,700);

		g.setColor(Color.cyan);
		for(Polygon polygon : parray){
			g.fillPolygon(polygon);
		}

		g.setColor(Color.orange);
		for(Polygon polygon : roadlist){
			g.fillPolygon(polygon);
		}

		for(Point p :  pointlist){
			int x = (int)((p.lon - west.lon)*b) + offset_x;
            int y = base_y - (int)((p.lat - south.lat)*b) + offset_y;
            
			g.setColor(Color.black);
			g.drawString(p.name,x,y);
			g.setColor(Color.green);
			g.fillOval(x,y,16,16);
		}

		if(locus_mode){
			g.setColor(Color.blue);
			for(Point p : points){
				int x = (int)((p.lon - west.lon)*b) + offset_x;
				int y = base_y - (int)((p.lat - south.lat)*b) + offset_y;
				g.fillOval(x,y,1,1);
			}
		}
    }
	
	Point getCurrentPoint(int x,int y){
		Point point = new Point();
		point.lon = ((x - offset_x) / b) + west.lon;
		point.lat = ((base_y + offset_y - y) / b) + south.lat;
		return point;
	}

	void openPopup(int x,int y){
		Popup popup = Popup.getInstance(this,x,y);
		Point point = getCurrentPoint(x,y);
		popup.setPoint(point);
		popup.setVisible(true);
	}

	void addPoint(Point point){
		pointlist.add(point);
		repaint();

		savePoint(point);
	}

	void savePoint(Point point){
		try{
			int count = pointlist.size();
			File dir = new File("points/" + (count+1));
			if(!dir.exists()){
				dir.mkdir();
			}

			String fname = "points/" + (count+1) + "/point";
			FileOutputStream fout = new FileOutputStream(fname);
			PrintStream out = new PrintStream(fout);
			out.println(point.name);
			out.print(point.lat + "," + point.lon);
			out.close();
			fout.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	void close(){
		setVisible(false);
		dispose();
	}

	public static void main(String args[]){
		Point [] points = PointLoader.load(args[0]);

        north = PointLoader.north;
        south = PointLoader.south;
        west  = PointLoader.west;
        east  = PointLoader.east;

        Ex01  ex01 = new Ex01();
        ex01.points = points;
        ex01.setVisible(true);
	}
}
