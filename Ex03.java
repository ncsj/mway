import  java.awt.*;
import  java.awt.event.*;

class Ex03 extends Frame{
	Point [] points = null;

	public Ex03(){
		setBounds(0,0,1280,1080);

		addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e){
				repaint();
			}
		});
	}

	public void paint(Graphics g){
		g.setColor(Color.white);
		g.fillRect(50,50,1180,700);

		double b = 12000.0;
		int offset_x = 300;
		int offset_y = 300;
		int base_y   = 200;

		g.setColor(Color.blue);
		for(Point p : points){
			// int x = (int)((p.lon * b) - 138.0 * b);
			// int y = (int)(800.0 - ((p.lat * b) - 35.0 * b));
			int x = (int)((p.lon*b - west.lon*b)) + offset_x;
			int y = base_y - (int)(p.lat*b - north.lat*b) + offset_y;
			g.fillOval(x,y,2,2);
		}
	}

	static Point north;
	static Point south;
	static Point west;
	static Point east;
	public static void main(String args[]){
		Point [] points = PointLoader.load(args[0]);

		north = PointLoader.north;
		south = PointLoader.south;
		west  = PointLoader.west;
		east  = PointLoader.east;

		Ex03  ex03 = new Ex03();
		ex03.points = points;
		ex03.setVisible(true);

	}
}
