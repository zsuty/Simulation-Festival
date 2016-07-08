package simulator.environment.worldObject;

import math.Circle2f;
import math.Point2f;
import math.Vector2f;
import simulator.environment.dataStructure.quadTree.Node;
import simulator.environment.dataStructure.quadTree.QuadTree;

public abstract class WorldObject{
	
	private Point2f position;
	private Circle2f shape;
	
	private Node node;
	private QuadTree tree;
	public WorldObject (WorldObject o){
		this.position = o.position.clone();
		this.shape = o.shape.clone();
	}
	
	public WorldObject(Point2f p, float radius){
		this.position = p;
		this.shape = new Circle2f(p, radius);
	}
	public synchronized Point2f getPosition() {
		return position;
	}
	public synchronized void Translate(Vector2f v) {
		this.position = this.position.operator_plus(v);
		this.shape = this.shape.translate(v);
		
	}
	public synchronized Circle2f getShape() {
		return shape;
	}
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}
	
	
}
