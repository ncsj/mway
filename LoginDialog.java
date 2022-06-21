import  java.awt.*;
import  java.awt.event.*;

public class LoginDialog extends Dialog{
	TextField field1 = new TextField();
	TextField field2 = new TextField();

	Button btn1 = new Button("OK");
	Button btn2 = new Button("CANCEL");

	String id = null;
	String pw = null;

	public LoginDialog(Frame frame){
		super(frame,"Login ... ",true);
		setLayout(null);
		setBounds(200,100,400,220);

		Label label1 = new Label("ID :");
		add(label1);
		label1.setBounds(50,50,50,20);

		add(field1);
		field1.setBounds(100,50,200,20);

		Label label2 = new Label("PW :");
		add(label2);
		label2.setBounds(50,80,50,20);

		add(field2);
		field2.setBounds(100,80,200,20);
		field2.setEchoChar('*');
		field2.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_ENTER){
					setID();
					close();
				}
			}
		});


		add(btn1);
		btn1.setBounds(300,120,80,30);
	
		add(btn2);
		btn2.setBounds(300,150,80,30);

		btn1.addActionListener((ActionEvent e)->{setID();close();});
		btn2.addActionListener((ActionEvent e)->{close();});
	}

	void setID(){
		this.id = field1.getText();
		this.pw = field2.getText();
	}

	public String getID(){
		return this.id;
	}

	public String getPW(){
		return this.pw;
	}

	void close(){
		setVisible(false);
		dispose();
	}
}
