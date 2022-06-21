import  java.awt.*;
import  java.awt.event.*;
import  java.io.*;

public class grec extends Frame implements Runnable{
	boolean flag = false;
	Thread thread = null;
	String dev = "/dev/tty.Qstarz818XT-SPPslave";
	String fname = "";

	Label statLabel = new Label("STATUS : IDOL");
	
	public grec(String fname){
		setLayout(null);
		setBounds(100,80,400,200);

		if(fname != null){
			this.fname = fname;
		}

		{
		Label label = new Label("DEV : " + dev);
		add(label);
		label.setBounds(30,40,350,20);
		}

		{
		Label label = new Label("FILE : " + fname);
		add(label);
		label.setBounds(30,70,350,20);
		}

		add(statLabel);
		statLabel.setBounds(30,100,150,20);

		Button  btn1 = new Button("START");
		add(btn1);
		btn1.setBounds(100,160,100,20);
		btn1.addActionListener((ActionEvent e)->{startThread();});
		
		Button  btn2 = new Button("STOP");
		add(btn2);
		btn2.setBounds(200,160,100,20);
		btn2.addActionListener((ActionEvent e)->{stopThread();});

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				close();
			}
		});

		setVisible(true);
	}

	void close(){
		setVisible(false);
		dispose();
	}

	/**
	  java grec <record-file>
	**/
	public static void main(String args[]){
		new grec(args[0]);
	}

	void startThread(){
		if(thread == null){
			flag = true;
			thread = new Thread(this);
			thread.start();
			statLabel.setText("STATUS : RECORDING...");
		}
	}

	void stopThread(){
		if(thread != null){
			flag = false;
			thread = null;
			statLabel.setText("STATUS : IDOL");
		}
	}

	public void run(){
		try{
			FileInputStream fin = new FileInputStream(dev);
			InputStreamReader is = new InputStreamReader(fin);
			BufferedReader reader = new BufferedReader(is);

			FileOutputStream fout = new FileOutputStream(fname);
			PrintStream out = new PrintStream(fout);

			while(flag){
				String line = reader.readLine();
				if(line == null){
					break;
				}
				else{
					out.println(line);
					System.out.println(line);
				}
			}

			out.close();
			fout.close();

			reader.close();
			is.close();
			fin.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
}
