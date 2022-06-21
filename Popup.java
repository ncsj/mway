import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import java.io.*;
import javax.imageio.*;

public class Popup extends Window{
	static Popup popup = null;
	public static Popup getInstance(Ex01 ex01,int x,int y){
		if(popup == null){
			popup = new Popup(ex01,x,y);
		}
		else{
		}
		return popup;
	}

	Ex01  ex01 = null;
	Point  point = null;

	Label	label1 = new Label();
	Label	label2 = new Label();
	Label	label3 = new Label();

	Image  image = null;

	private Popup(Ex01 ex01,int x,int y){
		super(ex01);

		this.ex01 = ex01;

		setBounds(x,y,420,160);
		setLayout(null);

		add(label1);
		label1.setBounds(200,30,100,20);

		add(label2);
		label2.setBounds(200,50,100,20);

		add(label3);
		label3.setBounds(300,50,100,20);

		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				close();
			}
		});

		addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e){
				loadImage();
			}
		});
	}

	void loadImage(){
		if(point != null){
			try{
				this.image = ImageIO.read(new File(point.image));
			}
			catch(Exception e){
			}
		}
	}

	public void paint(Graphics g){
		int w = 160;
		int h = 120;
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

	void setPoint(Point point){
		this.point = point;

		String slat = String.valueOf(point.lat);
        String slon = String.valueOf(point.lon);

		label1.setText(point.name);
		label2.setText(slat);
        label3.setText(slon);
	}

	void close(){
		setVisible(false);
		dispose();

	}
}
