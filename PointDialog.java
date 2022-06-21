import  java.awt.*;
import  java.awt.event.*;
import  javax.imageio.*;
import  java.awt.image.*;
import  java.io.File;

public class PointDialog extends Dialog{
	Button btn1 = new Button("OK");
	Button btn2 = new Button("CANCEL");

	TextField field1	= new TextField();
	TextField field2	= new TextField();
	TextField field3	= new TextField();
	TextField field4	= new TextField();
	TextArea  area		= new TextArea();

	Point point = null;
	Ex01  ex01 = null;
	public PointDialog(Ex01 ex01,int x,int y){
		super(ex01,"Point ...",true);
		setLayout(null);
		setBounds(x,y,400,300);

		this.ex01 = ex01;

		point = ex01.getCurrentPoint(x,y);

		{
			Label label = new Label("NAME : ");
			add(label);
			label.setBounds(20,30,80,20);

			add(field1);
			field1.setBounds(100,30,200,20);
		}

		{
			Label label = new Label("Lat : ");
			add(label);
			label.setBounds(20,55,40,20);

			add(field2);
			field2.setBounds(60,55,100,20);

			String s = String.valueOf(point.lat);
			field2.setText(s);
		}
		
		{
			Label label = new Label("Lon : ");
			add(label);
			label.setBounds(200,55,40,20);

			add(field3);
			field3.setBounds(240,55,100,20);

			String s = String.valueOf(point.lon);
			field3.setText(s);
		}

		{
			Label label = new Label("Image : ");
			add(label);
			label.setBounds(20,90,60,20);

			add(field4);
			field4.setBounds(100,90,180,20);

			Button btn = new Button("...");
			add(btn);
			btn.setBounds(284,90,40,20);
			btn.addActionListener((ActionEvent e)->{ openFileDialog(); });
		}


		add(btn1);
		btn1.setBounds(310,250,80,20);
		btn1.addActionListener((ActionEvent e)->{setPoint();});

		add(btn2);
		btn2.setBounds(310,270,80,20);
		btn2.addActionListener((ActionEvent e)->{close();});

		setVisible(true);
	}

	void loadImage(Graphics g){
		int  w = 160;
		int  h = 120;

		try{
			BufferedImage src = ImageIO.read(new File(point.image));
			BufferedImage scaleImg =
                    new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D gr2 = scaleImg.createGraphics();
			Image img = src.getScaledInstance(w,h, Image.SCALE_AREA_AVERAGING);
			gr2.drawImage(img,0,0,w,h,null);

			g.drawImage(scaleImg,100,120,this);
		}
		catch(Exception e){
		}
	}

	public void paint(Graphics g){
		g.setColor(Color.white);
		g.fillRect(100,120,160,120);

		loadImage(g);
	}

	void openFileDialog(){
		FileDialog fd = new FileDialog(ex01,"イメージファイルの選択",FileDialog.LOAD);
		fd.setFile("*.jpg,*.jpeg");
		fd.setVisible(true);

		String dir = fd.getDirectory();
		String file = fd.getFile();

		if(dir != null && file != null){
			field4.setText(dir + file);
			point.image = field4.getText();
		}

		repaint();
	}

	void setPoint(){
		point.name = field1.getText();
		point.image = field4.getText();

		ex01.addPoint(point);

		close();
	}

	void close(){
		setVisible(false);
		dispose();
	}
}
