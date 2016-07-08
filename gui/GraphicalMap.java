package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import gui.drawingTool.DrawingObject;
import math.Point2f;
import math.Rectangle2f;
import math.Vector2f;
import simulator.agent.mind.Festivalgoer;
import simulator.environment.dataStructure.quadTree.QuadTree;
import simulator.environment.worldObject.Bar;
import simulator.environment.worldObject.FestivalgoerBody;
import simulator.environment.worldObject.Scene;
import simulator.environment.worldObject.Toilet;
import simulator.environment.worldObject.WorldObject;

public class GraphicalMap extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3379077206119377471L;
	
	private QuadTree tree;
	private Rectangle2f fieldOfView;
	private Point2f center;
	private float scale = 10;
	
	private ArrayList <DrawingObject> listObj;
	
	public GraphicalMap(QuadTree tree){
		super();
		this.tree = tree;
		this.listObj = new ArrayList<DrawingObject>();
		this.fieldOfView = new Rectangle2f(-(float)(this.getWidth())/(float)(2*scale), -(float)(this.getHeight())/(float)(2*scale), (float)(this.getWidth())/(float)(2*scale), (float)(this.getHeight())/(float)(2*scale));
		this.center = new Point2f(0, 0);
	}
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getWidth());
		

		for(DrawingObject tempObject : this.listObj){

			g.setColor(tempObject.getC());
			g.fillOval(tempObject.getCentreX() - tempObject.getWidth()/2, tempObject.getCentreY() - tempObject.getHeight()/2,tempObject.getWidth(),tempObject.getHeight());
		}
		
	}
	public void setVariable(){
		
		ArrayList <WorldObject> tempList;
		this.fieldOfView = new Rectangle2f(-(float)(this.getWidth())/(float)(2*scale), -(float)(this.getHeight())/(float)(2*scale), (float)(this.getWidth())/(float)(2*scale), (float)(this.getHeight())/(float)(2*scale));
		this.fieldOfView = this.fieldOfView.translate(new Vector2f(this.center));
		if(this.tree != null){
			this.listObj.clear();
			tempList = this.tree.getObjectInShape(this.fieldOfView);
			int x,y,size;
			for(WorldObject tempObject : tempList){
				x = (int) ((tempObject.getPosition().getX() - this.center.getX()) * this.scale) + this.getWidth()/2;
				y = (int) (-(tempObject.getPosition().getY() - this.center.getY()) * this.scale) + this.getHeight()/2;
				size = (int) (tempObject.getShape().getRadius() * this.scale*2);
				Color c = getColorObject(tempObject);
				this.listObj.add(new DrawingObject(x,y,size,size,c));
			}
		}
	}
	private Color getColorObject (WorldObject o){
		if(o instanceof FestivalgoerBody){
			switch ( ((Festivalgoer)((FestivalgoerBody) o).getLinkedAgent().getAgentMind()).getMusicLike() ){
			case 0 :
				return Color.BLUE;
			case 1 :
				return Color.RED;
			case 2 :
				return Color.ORANGE;
			default :	
				return Color.BLUE;
			}
		}
		if(o instanceof Scene){
			switch ( ((Scene)o).getMusicPlay() ){
			case 0 :
				return Color.BLUE;
			case 1 :
				return Color.RED;
			case 2 :
				return Color.ORANGE;
			default :	
				return Color.BLUE;
			}
		}
		if(o instanceof Bar){
			return Color.GREEN;
		}
		if(o instanceof Toilet){
			return Color.CYAN;
		}
		
		return Color.BLACK;
	}
		
}
