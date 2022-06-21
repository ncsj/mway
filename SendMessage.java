import  java.awt.*;
import  java.awt.event.*;

import  java.io.*;
import  java.util.ArrayList;
import  java.util.Calendar;
import  java.util.Date;

public class SendMessage extends Dialog{
	Ex01  ex01 = null;
	User  user = null;
	User  [] userlist = null;
	String lat = "35.0000";
	String lon = "137.0000";

	List  list = new List();
	Label label1 = new Label("DATE");
	Label label11 = new Label();
	
	Label label2 = new Label("TO");
	Label label21 = new Label();

	Label label3 = new Label("FROM");
	Label label31 = new Label();

	Label label4 = new Label("TITLE");
	Label label5 = new Label("BODY");

	Label label_latlon2 = new Label("35.0000 , 138.0000");
	Label label_latlon1 = new Label();

	TextField field = new TextField();
	TextArea area = new TextArea();

	public SendMessage(Ex01 ex01){
		super(ex01,"Messenger",false);
		setBounds(880,380,600,460);
		setLayout(null);

		this.ex01 = ex01;
		this.user = ex01.user;

		add(list);
		list.setBounds(20,50,150,300);
		list.addItemListener((ItemEvent e)->{selectUser();});

		add(label1);
		label1.setBounds(200,40,40,20);

		add(label11);
		label11.setBounds(260,40,280,20);

		add(label2);
		label2.setBounds(200,60,40,20);

		add(label21);
		label21.setBounds(260,60,100,20);
		add(label_latlon2);
		label_latlon2.setBounds(360,60,200,20);

		add(label3);
		label3.setBounds(200,80,40,20);

		add(label31);
		label31.setBounds(260,80,100,20);

		add(label_latlon1);
		label_latlon1.setBounds(360,80,200,20);
		
		lat = String.valueOf(ex01.user.point.lat);
		lon = String.valueOf(ex01.user.point.lon);
		label_latlon1.setText(lat + " , " + lon);

		add(label4);
		label4.setBounds(200,100,60,20);

		add(field);
		field.setBounds(280,100,200,20);

		add(label5);
		label5.setBounds(200,120,60,20);

		add(area);
		area.setBounds(200,140,360,200);

		{
		Button btn = new Button("SEND");
		btn.addActionListener((ActionEvent e)->{sendMessage();});
		add(btn);
		btn.setBounds(500,400,90,25);
		}
		{
		Button btn = new Button("CLOSE");
		btn.addActionListener((ActionEvent e)->{close();});
		add(btn);
		btn.setBounds(500,425,90,25);
		}

		addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e){
				loadMessage();
			}
		});
		setVisible(true);
	}

	void selectUser(){
		int index = list.getSelectedIndex();
		if(index > -1){
			Calendar cal = Calendar.getInstance();
			int y = cal.get(Calendar.YEAR);
			int m = cal.get(Calendar.MONTH) + 1;
			int d = cal.get(Calendar.DAY_OF_MONTH);
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int min  = cal.get(Calendar.MINUTE);
			int sec  = cal.get(Calendar.SECOND);
			String sdate = String.format("%d/%d/%d",y,m,d);
			String stime = String.format("%02d:%02d:%02d",hour,min,sec);
			label11.setText(sdate + " " + stime);
			
			label21.setText(userlist[index].name);
			label31.setText(ex01.user.name);
			label_latlon1.setText(lat + " , " + lon);
			label4.setText("TITLE : ");

			String lat2 = String.valueOf(userlist[index].point.lat);
			String lon2 = String.valueOf(userlist[index].point.lon);
			label_latlon2.setText(lat2 + " , " + lon2);

			ex01.target = userlist[index];
			ex01.repaint();
		}
	}

	ArrayList <Message> array = new ArrayList <Message> ();
	void loadMessage(){
		try{
			User [] users = UserManager.list();

			for(User user : users){
				list.add(user.name);
			}

			userlist = users;
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

	void sendMessage(){
		Calendar cal = Calendar.getInstance();
		Message message = new Message();
		message.checked	= false;
		message.year	= String.valueOf(cal.get(Calendar.YEAR));
		message.month	= String.format("%02d",cal.get(Calendar.MONTH));
		message.date	= String.format("%02d",cal.get(Calendar.DATE));
		message.hour	= String.format("%02d",cal.get(Calendar.HOUR_OF_DAY));
		message.minute	= String.format("%02d",cal.get(Calendar.MINUTE));
		message.second	= String.format("%02d",cal.get(Calendar.SECOND));

		message.lat		= String.valueOf(user.point.lat);
		message.lon		= String.valueOf(user.point.lon);

		message.from	= user.name;
		message.title	= field.getText();
		message.body	= area.getText();

		String msg = message.toString();

		int index = list.getSelectedIndex();
		if(index > -1){
			try{
				String path = userlist[index].home + "/message";
				String file = String.format("%d%02d%02d-%02d%02d%02d-%s.msg",
									cal.get(Calendar.YEAR),
									cal.get(Calendar.MONTH),
									cal.get(Calendar.DATE),
									cal.get(Calendar.HOUR),
									cal.get(Calendar.MINUTE)+1,
									cal.get(Calendar.SECOND),
									user.name);

				FileOutputStream fout = new FileOutputStream(path + "/" + file);
				PrintStream pout = new PrintStream(fout);

				pout.print(msg);

				//	System.out.println(path + "/" + file);
				//	System.out.println(msg);

				pout.close();
				fout.close();
			}
			catch(Exception e){
				System.out.println(e.toString());
			}
			finally{
				close();
			}
		}
	}

	void close(){
		ex01.target = null;
		ex01.msgPoint = null;
		ex01.repaint();

		setVisible(false);
		dispose();
	}
}
