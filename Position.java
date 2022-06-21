public class Position{
	int  x;
	int  y;

	public Position(){
	}

	public Position(java.awt.event.MouseEvent e){
		this.x = e.getX();
		this.y = e.getY();
	}

	public Position diff(Position p){
		Position pos = new Position();
		pos.x = x - p.x;
		pos.y = y - p.y;

		return pos;
	}
}
