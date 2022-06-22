import  java.awt.*;
import  java.awt.event.*;
import  java.awt.image.BufferedImage;
import  java.util.ArrayList;
import  java.io.*;

public class Ex01 extends Frame implements Runnable{
	LaunchDialog	launchDialog	= null;
	User			user			= null;
	User			target			= null;
	
	Point [] points = null;
	static Point north;
    static Point south;
    static Point west;
    static Point east;
	Point  cp;

	boolean locus_mode = false;
	boolean check_mode = false;

	Label label_lat = new Label("Lat : 00.00000");
	Label label_lon = new Label("Lon : 000.00000");
	
	ArrayList <Polygon> parray = new ArrayList <Polygon> ();
	ArrayList <Polygon> roadlist = new ArrayList <Polygon> ();

	ArrayList <Point> pointlist = new ArrayList <Point> ();

	Point msgPoint = null;

	Dimension dim = null;
	Image  image = null;
	Graphics ig = null;

	Thread  thread		= null;
	boolean trackmode	= false;
	boolean gridline	= false;
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

		initMapData();

		loadPoints();

		Ex01MouseAdapter ma = new Ex01MouseAdapter(this);
		addMouseListener(ma);
		addMouseMotionListener(ma);

		addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e){
				openLoginDialog();
				initImage();
				repaint();
			}
			public void windowClosing(WindowEvent e){
				close();
			}
		});
	}

	void initMapData(){
		this.parray = new ArrayList <Polygon> ();
		this.roadlist = new ArrayList <Polygon> ();

		loadObject();
	}

	void loadObject(){

		{
			// System.out.println("check - 1");
			Point [] points = PointLoader.loadObject("river/yamanakako.csv");
			Polygon polygon = new Polygon();
			for(Point p : points){
				int x = (int)((p.lon - west.lon)*(b*mag)) + offset_x;
				int y = base_y - (int)((p.lat - south.lat)*(b*mag)) + offset_y;

				polygon.addPoint(x,y);

				// System.out.printf("ctx.lineTo(%d,%d);\n",x,y);
			}
			parray.add(polygon);
		}

		String [] roads = {"R138-301.csv","R138-201.csv","R138-101.csv",
							"R413-001.csv","R413-002.csv",
							"R246-001.csv","R246-002.csv","R246-003.csv",
							"R246-004.csv","R246-101.csv","R246-102.csv",
							"P22L147-001.csv",
							"kendo.csv",
							"FSW001.csv","FSW002.csv","FSW003.csv","FSW004.csv","FSW005.csv",
							"FSW006.csv","FSW007.csv","FSW008.csv","FSW009.csv","FSW010.csv",
							"FSW011.csv","FSW012.csv","FSW013.csv"};
		/**
		{
			File road_dir = new File("./road");
			if(road_dir.isDirectory()){
				String [] files = road_dir.list();
				//for(String file : files){
				//	System.out.println(file);
				//}

				roads = files;
			}
		}
		**/

		for(String road : roads){
			// System.out.println("road : " + road);
			Point [] ps = PointLoader.loadObject("road/" + road);
			Polygon polygon = new Polygon();
			for(Point p : ps){
				int x = (int)((p.lon - west.lon)*(b*mag)) + offset_x;
				int y = base_y - (int)((p.lat - south.lat)*(b*mag)) + offset_y;
			
				// System.out.printf("ctx.lineTo(%d,%d);\n",x,y);
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

			String image = reader.readLine();

			point = new Point();
			point.name = name;
			point.lat = Double.valueOf(lat).doubleValue();
			point.lon = Double.valueOf(lon).doubleValue();

			if(image != null){
				if(image.equals("none")){
					point.image = null;
				}
				else{
					point.image = image;
				}
			}

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

	void initImage(){
		Toolkit toolkit = Toolkit.getDefaultToolkit();
        this.dim = toolkit.getScreenSize();
		this.image = createImage(dim.width,dim.height);
		
		this.ig = this.image.getGraphics();
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

	double mag = 100.0;
    double b = 100.0;
    int offset_x = 300;
    int offset_y = 50;
    int base_y   = 200;

	public void paint(Graphics g){
		if(this.image != null){
			_paint(ig);
			g.drawImage(this.image,0,0,this);
		}
	}

	public void printPaint(Graphics g,int w,int h){
        g.setColor(Color.white);
        g.fillRect(20,20,w,h);

        if(this.image != null){
            if(this.image instanceof BufferedImage){
                BufferedImage src = (BufferedImage)this.image;
                BufferedImage scaleImg =
                    new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D gr2 = scaleImg.createGraphics();
                Image img = src.getScaledInstance(w,h, Image.SCALE_AREA_AVERAGING);
                gr2.drawImage(img,0,0,w,h,null);

                g.drawImage(scaleImg,20,20,this);
            }
        }
    }

	public void print(){
		JobAttributes jobAttrs = new JobAttributes();
		
		PageAttributes pageAttrs = new PageAttributes();
		pageAttrs.setOrientationRequested(PageAttributes.OrientationRequestedType.LANDSCAPE);
		
		String jobTitle = "Ex01 - Map";
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		PrintJob job = toolkit.getPrintJob(this,jobTitle,jobAttrs,pageAttrs);

		Graphics g = job.getGraphics();
		// g.drawImage(this.image,0,0,this);
		printPaint(g,830,580);

		/** these codes are for check
		g.setColor(Color.black);
		g.drawLine(0,50,830,50);
		g.drawLine(50,0,50,580);
		**/

		g.dispose();
		job.end();
	}

	public void _paint(Graphics g){
        g.setColor(Color.white);
        g.fillRect(0,0,dim.width,dim.height);

        // g.setColor(Color.white);
        // g.fillRect(50,50,1180,700);

		g.setColor(Color.cyan);
		for(Polygon polygon : parray){
			g.fillPolygon(polygon);
		}

		g.setColor(Color.orange);
		for(Polygon polygon : roadlist){
			g.fillPolygon(polygon);
		}

		for(Point p :  pointlist){
			Position pos = getPosition(p);
			if(pos != null){
				Font font = new Font(Font.SERIF, Font.PLAIN, 18);
				g.setFont(font);
				g.setColor(Color.black);
				g.drawString(p.name,pos.x,pos.y);

				Color c = new Color(0,255,0);
				g.setColor(c);
				g.fillOval(pos.x,pos.y,16,16);

				/**
		System.out.printf("ctx.strokeText('%s',%d,%d);\n",p.name,pos.x,pos.y);
		System.out.printf("ctx.arc(%d,%d,6,0,2*Math.PI);\n",pos.x,pos.y);
				**/
			}
		}

		if(target != null){
			Position pos = getPosition(target.point);
           
			Font font = new Font(Font.DIALOG_INPUT,Font.PLAIN,12);
			g.setFont(font);
			g.setColor(Color.black);
			g.drawString(target.name,pos.x,pos.y);

			Color c = new Color(255,64,64);
			g.setColor(c);
			g.fillOval(pos.x,pos.y,16,16);
		}

		if(gridline){	// draw lat&lon line.
		Color c = new Color(192,192,192);
		g.setColor(c);
		// draw latitude
		double dh = 1.0 / 60.0;
		for(int i=0;i<=120;i++){
			Point wp = new Point();
			wp.lat = 36.0 - (double)(dh * (double)i);
			wp.lon = 130.0;

			Point ep = new Point();
			ep.lat = wp.lat;
			ep.lon = 145.0;

			Position p1 = getPosition(wp);
			Position p2 = getPosition(ep);

			g.drawLine(p1.x,p1.y,p2.x,p2.y);
		}

		// draw longitude
		// double dh = 1.0 / 60.0;
		for(int i=0;i<=180;i++){
			Point np = new Point();
			np.lat = 38.0;
			np.lon = 138.0 + (double)(dh * (double)i);

			Point sp = new Point();
			sp.lat = 34.0;
			sp.lon = np.lon;

			Position p1 = getPosition(np);
			Position p2 = getPosition(sp);

			g.drawLine(p1.x,p1.y,p2.x,p2.y);
		}
		}

		if(cp != null){
			Position pos = getPosition(cp);
			
			Color c = Color.pink;
			g.setColor(c);
			g.fillOval(pos.x-8,pos.y-8,16,16);
		}



		if(msgPoint != null){
			Position pos = getPosition(msgPoint);

            Font font = new Font(Font.DIALOG_INPUT,Font.PLAIN,12);
            g.setFont(font);
            g.setColor(Color.black);
            g.drawString("from - " + msgPoint.name,pos.x,pos.y);

            Color c = new Color(240,0,0);
            g.setColor(c);
            g.fillOval(pos.x,pos.y,16,16);
		}

		if(locus_mode){
			g.setColor(Color.blue);
			for(Point p : points){
				Position pos = getPosition(p);
				g.fillOval(pos.x,pos.y,2,2);

				// String sformat = "ctx.lineTo(%d,%d);\n";
				// System.out.printf(sformat,pos.x,pos.y);
			}
		}
    }

	public void run(){
		try{
			for(Point cp : points){
				Thread.sleep(10);

				this.cp = cp;
				if(trackmode == false){
					break;
				}
				repaint();
			}
			this.cp = null;
		}
		catch(InterruptedException e){
		}
	}

	boolean checkColor(int rgb,int x,int y){
        boolean rtc = false;

        if(this.image instanceof BufferedImage){
            BufferedImage buffimg = (BufferedImage)this.image;

            try{
                int bc = buffimg.getRGB(x,y);
                if(bc == rgb){
                    rtc = true;
                }
            }
            catch(ArrayIndexOutOfBoundsException e){
                rtc = false;
            }
        }

        return rtc;
    }

	Position getPosition(Point p){
		Position pos = null;
		try{
			Position _pos = new Position();
			_pos.x = (int)((p.lon - west.lon)*(b*mag)) + offset_x;
			_pos.y = base_y - (int)((p.lat - south.lat)*(b*mag)) + offset_y;
		
			pos = _pos;
		}
		catch(Exception e){
		}
		return pos;
	}

	Point getCurrentPoint(int x,int y){
		Point point = new Point();
		point.lon = ((x - offset_x) / (b*mag)) + west.lon;
		point.lat = ((base_y + offset_y - y) / (b*mag)) + south.lat;
		return point;
	}

	Point getPoint(Position pos){
		Point point = new Point();
		point.lon = ((pos.x - offset_x) / (b*mag)) + west.lon;
		point.lat = ((base_y + offset_y - pos.y) / (b*mag)) + south.lat;
		return point;
	}

	Point search(int x,int y){
		Point point = null;
		int   diff = -1;
		for(Point p : pointlist){
			int x1 = (int)((p.lon - west.lon)*(b*mag)) + offset_x;
            int y1 = base_y - (int)((p.lat - south.lat)*(b*mag)) + offset_y;

			int dx = x1 - x;
			int dy = y1 - y;

			if(dx < 0){
				dx = dx * -1;
			}
			if(dy < 0){
				dy = dy * -1;
			}

			if(diff < 0 || diff > (dx + dy)){
				diff = dx + dy;
				point = p;
			}
		}
		return point;
	}

	void openPopup(int x,int y){
		Popup popup = Popup.getInstance(this,x,y);
		// Point point = getCurrentPoint(x,y);
		Point point = search(x,y);
		if(point != null){
			popup.setPoint(point);
			popup.setVisible(true);
		}
	}

	void closePopup(){
		if(Popup.popup != null){
			Popup.popup.close();
		}
	}

	void openPointDialog(int x,int y){
		new PointDialog(this,x,y);
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
			out.println(point.lat + "," + point.lon);
			out.print(point.image);
			out.close();
			fout.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	void close(){
		launchDialog.close();
		setVisible(false);
		dispose();
	}

	public static void main(String args[]){
		String org = "yamanakako-20180312-10Hz.csv";
		if(args.length == 1){
			org = args[0];
		}

		Point [] points = PointLoader.load(org);

        north = PointLoader.north;
        south = PointLoader.south;
        west  = PointLoader.west;
        east  = PointLoader.east;

        Ex01  ex01 = new Ex01();
        ex01.points = points;
        ex01.setVisible(true);
	}
}
