package simulator.environment.dataStructure.quadTree;



import simulator.environment.worldObject.WorldObject;

import java.util.ArrayList;

import math.Rectangle2f;
import math.Shape2f;
import math.Vector2f;


public class QuadTree {
private Node root;
	
	public QuadTree(float width, float height){
		root = new Node(new Rectangle2f(-width/2,-height/2,width/2,height/2));
	}
	public synchronized void insertObject(WorldObject o){
		this.root.insertObject(o);
	}
	
	
	public synchronized ArrayList <WorldObject> getObjectInShape(Shape2f <?> shape){
		return this.root.getObjectInShape(shape);
	}
	public synchronized void translate(Vector2f v, WorldObject o) {
		o.Translate(v);
		o.getNode().removeObject(o);
		this.insertObject(o);
		
	}
}
