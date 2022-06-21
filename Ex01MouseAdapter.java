import  java.awt.event.*;

public class Ex01MouseAdapter extends MouseAdapter{
	Ex01  ex01 = null;
	public Ex01MouseAdapter(Ex01 ex01){
		this.ex01 = ex01;
	}

	@Override
	public void mouseMoved(MouseEvent e){
		int x = e.getX();
		int y = e.getY();

		Point p = ex01.getCurrentPoint(x,y);
		String slat = String.format("Lat : %.05f",p.lat);
		String slon = String.format("Lon : %.05f",p.lon);

		ex01.label_lat.setText(slat);
		ex01.label_lon.setText(slon);

		int rgb = 0xff00ff00;
		if(ex01.checkColor(rgb,x,y)){
			ex01.openPopup(x,y);
		}
		else{
			ex01.closePopup();
		}

	}

	@Override
	public void mouseClicked(MouseEvent e){
		int x = e.getX();
		int y = e.getY();
		
		if(ex01.check_mode){
			System.out.println("CLICKED!");
			System.out.println("ctx.lineTo(" + x + "," + y + ");");
		}
		else{
			ex01.openPointDialog(x,y);
		}
	}

	Position  pos = null;

	@Override
	public void mousePressed(MouseEvent e){
		pos = new Position(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		drawMap(e);
	}

	void drawMap(MouseEvent ex){
		Position cur = new Position(ex);
		Position diff = pos.diff(cur);
		
		Position n = ex01.getPosition(Ex01.north);
		Position w = ex01.getPosition(Ex01.west);
		Position s = ex01.getPosition(Ex01.south);
		Position e = ex01.getPosition(Ex01.east);

		n.x = n.x + diff.x;
		n.y = n.y + diff.y;
		ex01.north = ex01.getPoint(n);

		w.x = w.x + diff.x;
		w.y = w.y + diff.y;
		ex01.west = ex01.getPoint(w);

		s.x = s.x + diff.x;
		s.y = s.y + diff.y;
		ex01.south = ex01.getPoint(s);

		e.x = e.x + diff.x;
		e.y = e.y + diff.y;
		ex01.north = ex01.getPoint(e);

		pos = cur;
		ex01.initMapData();
		ex01.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e){
		drawMap(e);
	}
}
