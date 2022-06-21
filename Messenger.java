import  java.awt.*;
import  java.awt.event.*;

import  java.util.ArrayList;
import  java.io.*;

public class Messenger extends Dialog{
	Ex01  ex01 = null;
	User  user = null;
	String [] files = null;

	List  list = new List();
	Label label1 = new Label();
	Label label2 = new Label();
	Label label_checked = new Label("---------");
	Label label3 = new Label("FROM");
	Label label4 = new Label("TITLE");

	TextArea area = new TextArea();

	public Messenger(Ex01 ex01){
		super(ex01,"Messenger",false);
		setBounds(880,480,600,480);
		setLayout(null);

		this.ex01 = ex01;
		this.user = ex01.user;

		add(list);
		list.setBounds(20,50,150,400);
		list.addItemListener((ItemEvent e)->{selectMessage();});

		{
			Label label = new Label("DATE");
			add(label);
			label.setBounds(200,50,40,20);
		}
		add(label1);
		label1.setBounds(250,50,80,20);

		add(label2);
		label2.setBounds(330,50,80,20);

		add(label_checked);
		label_checked.setBounds(420,50,80,20);

		add(label3);
		label3.setBounds(200,80,100,20);

		add(label4);
		label4.setBounds(200,100,200,20);

		add(area);
		area.setBounds(200,120,360,200);

		{
			Button btn = new Button("CHECK");
			btn.addActionListener((ActionEvent e)->{checkMessage();});
			add(btn);
			btn.setBounds(200,321,360,30);
		}

		{
			Button btn = new Button("CLOSE");
			btn.addActionListener((ActionEvent e)->{close();});
			add(btn);
			btn.setBounds(500,440,90,25);
		}

		addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e){
				loadMessage();
			}
		});
		setVisible(true);
	}

	void selectMessage(){
		int index = list.getSelectedIndex();
		if(index > -1){
			Message message = array.get(index);
			String ymd = message.year + "/" + message.month + "/" + message.date;
			String hms = message.hour + ":" + message.minute + ":" + message.second;

			label1.setText(ymd);
			label2.setText(hms);

			if(message.checked){
				label_checked.setText("checked");
			}
			else{
				label_checked.setText("unchecked");
			}

			label3.setText("FROM : " + message.from);
			label4.setText("TITLE : " + message.title);

			area.setText(message.body);

			ex01.msgPoint = new Point();
			ex01.msgPoint.lat = Double.valueOf(message.lat).doubleValue();
			ex01.msgPoint.lon = Double.valueOf(message.lon).doubleValue();
			ex01.msgPoint.name = message.from;

			ex01.repaint();
		}
	}

	void checkMessage(){
		int index = list.getSelectedIndex();
		if(index > -1){
			Message message = array.get(index);
			message.checked = true;

			label_checked.setText("checked");

			saveMessage(message);
		}
	}

	void saveMessage(Message message){
		try{
			String path = user.home + "/message";
			int index = list.getSelectedIndex();
			String fname = files[index];

			FileOutputStream fout = new FileOutputStream(path + "/" + fname);
			PrintStream out = new PrintStream(fout);
			out.print(message.toString());
			out.close();
			fout.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	ArrayList <Message> array = new ArrayList <Message> ();
	void loadMessage(){
		try{
			String path = user.home + "/message";
			File dir = new File(path);
			files = dir.list();
			for(String file : files){
				if(checkFileType(file)){
					Message message = new Message(path + "/" + file);
					array.add(message);
					list.add(message.title);
				}
			}
		}
		catch(Exception e){
		}
	}

	boolean checkFileType(String file){
		boolean rtc = false;
		String [] fname = file.split("\\.");
		if(fname.length == 2){
			if(fname[1].equals("msg")){
				rtc = true;
			}
		}
		return rtc;
	}

	void close(){
		ex01.msgPoint = null;
		ex01.repaint();

		setVisible(false);
		dispose();
	}
}
