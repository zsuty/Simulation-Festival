
package simulator.environment.dataStructure.quadTree;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import simulator.environment.worldObject.WorldObject;
import math.Rectangle2f;
import math.Shape2f;


public class Node {
	private List <Node> child;
	
	private Rectangle2f bounds;
	
	private List <WorldObject> listObject;
	
	Node(Rectangle2f bounds){
		this.bounds = bounds;
		this.listObject = new ArrayList<WorldObject> ();
	}
	
	public synchronized void insertObject(WorldObject o){
		if(child == null){
			if(listObject.size() < 10){
				this.listObject.add(o);
				o.setNode(this);
			}
			else{
				this.createchilds();				
				ArrayList<WorldObject> tempList = new ArrayList<WorldObject>(this.listObject);
				this.listObject.clear();
				for(WorldObject objectInList : tempList){
					this.insertObject(objectInList);
				}
				this.insertObject(o);
			}
		}
			
		else{
			boolean intersect = false;
			for(Node c : this.child){
				if(o.getShape().intersects(c.bounds))
					intersect = true;
			}
			if(intersect){
				this.listObject.add(o);
				o.setNode(this);
			}
			else{
				boolean find = false;
				Iterator<Node> it = this.child.iterator();
				Node tempChild;
				while(it.hasNext() && !find){
					tempChild = it.next();
					if(o.getPosition().getX() <= tempChild.bounds.getUpper().getX() && o.getPosition().getX() >= tempChild.bounds.getLower().getX() && o.getPosition().getY() <= tempChild.bounds.getUpper().getY() && o.getPosition().getY() >= tempChild.bounds.getLower().getY()){
						find = true;
						tempChild.insertObject(o);
					}
				}
			}
		}
	}
	
	
	public synchronized ArrayList<WorldObject> getObjectInShape(Shape2f<?> shape){
		ArrayList <WorldObject> tempListObject = new ArrayList<WorldObject> ();
		if(child == null){
			for(WorldObject o : this.listObject){
				if(o.getShape().intersects(shape))
					tempListObject.add(o);
			}
		}
		else{
			int intersection = 0;
			for(Node c : this.child){
				if(c.bounds.intersects(shape)){
					++ intersection;
					tempListObject.addAll(c.getObjectInShape(shape));
				}
			}
			if(intersection > 1){
				for(WorldObject o : this.listObject){
					if(o.getShape().intersects(shape))
						tempListObject.add(o);
				}
			}
		}
		return tempListObject;
	}
	
	private void createchilds(){
		this.child = new ArrayList<Node>();
		
		this.child.add(new Node(new Rectangle2f(this.bounds.getLower().getX(),this.bounds.getUpper().getY(),this.bounds.getCenter().getX(),this.bounds.getCenter().getY())));
		this.child.add(new Node(new Rectangle2f(this.bounds.getUpper().getX(),this.bounds.getUpper().getY(),this.bounds.getCenter().getX(),this.bounds.getCenter().getY())));
		this.child.add(new Node(new Rectangle2f(this.bounds.getLower().getX(),this.bounds.getLower().getY(),this.bounds.getCenter().getX(),this.bounds.getCenter().getY())));
		this.child.add(new Node(new Rectangle2f(this.bounds.getUpper().getX(),this.bounds.getLower().getY(),this.bounds.getCenter().getX(),this.bounds.getCenter().getY())));
		
	}
	
	public synchronized void removeObject(WorldObject o){
		this.listObject.remove(o);
	}
	
}
