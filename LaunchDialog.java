import  java.awt.*;
import  java.awt.event.*;
import  java.util.ArrayList;
import  java.io.*;

public class LaunchDialog extends Dialog implements Runnable{
	Button [] buttons = null;
	Checkbox check1 = new Checkbox("Display Locus ...");
	Checkbox check2 = new Checkbox("Display Grid ...");
	Checkbox check3 = new Checkbox("Check Point ...");

	Button  printBtn = new Button("PRINT");
	Button  sbox = new Button("SEND MESSAGE");
	Button  mbox = new Button("MESSAGE");
	Ex01  ex01 = null;

	boolean  doCheck  =  false;

	MessageChecker checker = null;
	Thread thread = null;

	Scrollbar sbar = new Scrollbar();
	List	list = new List();

	public LaunchDialog(Ex01 ex01){
		super(ex01,"Menu",false);
		this.ex01 = ex01;
		setLayout(null);

		Rectangle rect = ex01.getBounds();
		int w = 200;
		int x = rect.width - w - 30;
		int y = rect.y + 30;
		setBounds(x,y,w,460);

		setButtons();

		add(printBtn);
		printBtn.setBounds(30,190,150,20);
		printBtn.addActionListener((ActionEvent e)->{print();});
		
		add(sbox);
		sbox.setBounds(30,210,150,20);
		sbox.addActionListener((ActionEvent e)->{openSender();});

		add(mbox);
		mbox.setBounds(30,230,150,20);
		mbox.addActionListener((ActionEvent e)->{openMessenger();});

		{
			Label  label = new Label("Scale");
			add(label);
			label.setBounds(30,260,80,15);

			int initial		= (int)ex01.mag;
			int visible		= 1;
			int min			= 1;
			int max			= (int)initial * 8;
			sbar = new Scrollbar(Scrollbar.HORIZONTAL,initial,visible,min,max);
			add(sbar);

			sbar.setBounds(30,275,160,15);
			sbar.setOrientation(Scrollbar.HORIZONTAL);
			sbar.addAdjustmentListener((AdjustmentEvent e)->{
				int n = e.getValue();
				checkScroll(n);
			});
		}

	
		add(check1);
		check1.setBounds(30,290,200,10);
		check1.addItemListener((ItemEvent e)->{ check(); });

		add(check2);
		check2.setBounds(30,305,200,10);
		check2.addItemListener((ItemEvent e)->{ check2(); });

		add(check3);
		check3.setBounds(30,330,200,10);
		check3.addItemListener((ItemEvent e)->{ check3(); });

		addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e){
				listupTrackFiles();
				startThread();
			}
		});

		Button btn1 = new Button("START");
		add(btn1);
		btn1.setBounds(30,345,80,20);
		btn1.addActionListener((ActionEvent e)->{startTracking();});

		Button btn2 = new Button("STOP");
		add(btn2);
		btn2.setBounds(110,345,80,20);
		btn2.addActionListener((ActionEvent e)->{stopTracking();});

		add(list);
		list.setBounds(30,365,160,70);
		list.addItemListener((ItemEvent e)->{ loadPointList(); });

		setVisible(true);
	}

	void checkScroll(int n){
		ex01.mag = n;
		ex01.initMapData();
		ex01.repaint();
	}

	void listupTrackFiles(){
		File dir = new File("yamanakako");
		String [] files = dir.list();
		for(String s : files){
			list.add(s);
		}
	}

	void loadPointList(){
		if(list.getSelectedIndex() > -1){
			String file = list.getSelectedItem();
			String dir = "yamanakako/";
			Point [] points = PointLoader.load(dir + file);
			ex01.points = points;
			ex01.repaint();
		}
	}

	void startThread(){
		if(this.thread == null){
			this.doCheck = true;
			this.checker = new MessageChecker(ex01.user);
			this.thread = new Thread(this);
			this.thread.start();
		}
	}

	void startTracking(){
		if(ex01.thread == null){
			ex01.thread = new Thread(ex01);
			ex01.trackmode = true;
			ex01.thread.start();
		}
	}

	void stopTracking(){
		if(ex01.thread != null){
			ex01.thread = null;
			ex01.trackmode = false;
		}
	}

	public void run(){
		System.out.println("MESSAGE CHECKER STARTED.");
		while(doCheck){
			try{
				messageCheck();
				Thread.sleep(1000);
			}
			catch(Exception e){
				System.out.println(e.toString());
			}
		}
		System.out.println("MESSAGE CHECKER STOPPED.");
	}

	void print(){
		ex01.print();
	}

	void messageCheck(){
		int count     = checker.count();
		int unchecked = checker.check();

		String s = String.format("MESSAGE(%d/%d)",unchecked,count);

		mbox.setLabel(s);
	}

	void openSender(){
		new SendMessage(this.ex01);
	}

	void openMessenger(){
		new Messenger(this.ex01);
	}

	void check(){
		if(ex01.locus_mode){
			ex01.locus_mode = false;
		}
		else{
			ex01.locus_mode = true;
		}

		ex01.repaint();
	}

	void check2(){
		if(ex01.gridline){
			ex01.gridline = false;
		}
		else{
			ex01.gridline = true;
		}

		ex01.repaint();
	}

	void check3(){
		if(ex01.check_mode){
			ex01.check_mode = false;
		}
		else{
			ex01.check_mode = true;
		}
	}

	void setButtons(){
		ArrayList <String> array = new ArrayList <String> ();
		try{
			String path = ex01.user.home + "/apps";
			FileInputStream fin = new FileInputStream(path);
			InputStreamReader is = new InputStreamReader(fin);
			BufferedReader reader = new BufferedReader(is);

			while(true){
				String line = reader.readLine();
				if(line == null){
					break;
				}
				else{
					String [] cols = line.split(",");
					array.add(cols[0]);
				}
			}

			reader.close();
			is.close();
			fin.close();

			buttons = new Button [array.size()];

			for(int i=0;i<buttons.length;i++){
				String s = array.get(i);
				buttons[i] = new Button(s);

				int h = 30;
				int y = 30 + (i*h);
				add(buttons[i]);
				buttons[i].setBounds(20,y,160,h);
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	public void close(){
		doCheck = false;
		setVisible(false);
		dispose();
	}

}
