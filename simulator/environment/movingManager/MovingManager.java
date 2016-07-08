package simulator.environment.movingManager;

import java.util.ArrayList;

import math.Circle2f;
import math.Vector2f;
import simulator.environment.dataStructure.quadTree.QuadTree;
import simulator.environment.worldObject.Bar;
import simulator.environment.worldObject.MovableObject;
import simulator.environment.worldObject.Scene;
import simulator.environment.worldObject.Toilet;
import simulator.environment.worldObject.WorldObject;

public class MovingManager {

	private ArrayList<MovableObject> movingObject;
	private float maxSpeed = 0; // the maximum speed of the fastest object in movingObject
	private float maxRadius = 0;
	public MovingManager(){
		movingObject = new ArrayList<MovableObject> ();
	}
	public void moveAllWithInitialTest(QuadTree tree, float deltaT,ArrayList<WorldObject> obs){
		if(deltaT > 0){
			boolean initialVOk = true;
			boolean intersect = false;
			for(MovableObject mo : this.movingObject){
				//ArrayList<WorldObject> potentialObstacle = tree.getObjectInShape(new Circle2f(mo.getPosition(), mo.getMaxSpeed() + this.maxSpeed+ 2*maxRadius));
				ArrayList<WorldObject> potentialObstacle = new ArrayList<WorldObject> ();
				potentialObstacle.addAll(obs);
				//potentialObstacle.remove(mo);
				for(WorldObject obstacle : potentialObstacle){
					Vector2f c1c2 = new Vector2f(obstacle.getPosition()).operator_minus(new Vector2f(mo.getPosition()));
					intersect = mo.getShape().intersects((obstacle.getShape()));
					Float dt = mo.collision(obstacle);
					if (dt != null)
						intersect = (intersect || (dt < 0.001f && dt > -0.001f));
					if(intersect){
						initialVOk = false;
						mo.addUnAuthorizedDirection(c1c2);
					}			
				}
			}
			if(!initialVOk){
				for(MovableObject o : this.movingObject){
					o.computeVelocity();
				}
			}
			moveAll(tree, deltaT,obs);
		}
	}
	public void moveAll(QuadTree tree, float deltaT,ArrayList<WorldObject> obs){
		if(deltaT > 0){
			Float dt = null;
			float tempDt = 1.1f;
			for(MovableObject mo : this.movingObject){
				//ArrayList<WorldObject> potentialObstacle = tree.getObjectInShape(new Circle2f(mo.getPosition(), mo.getMaxSpeed() + this.maxSpeed + 2*maxRadius));
				ArrayList<WorldObject> potentialObstacle = new ArrayList<WorldObject>();
				potentialObstacle.addAll(obs);
				//potentialObstacle.remove(mo);
				for(WorldObject obstacle : potentialObstacle){
					dt = mo.collision(obstacle);
					if(dt != null){
						if(dt > 0){
							if (dt < tempDt){
								tempDt = dt;
							}
						}
					}			
				}
			}
			if(tempDt <= deltaT && tempDt > 0.001f){
				for(MovableObject mo : this.movingObject){
						tree.translate(mo.getVelocity().operator_multiply(tempDt),mo);
				}
				this.moveAllWithInitialTest(tree, deltaT - tempDt,obs);
			}
			else {
				for(MovableObject mo : this.movingObject){
					tree.translate(mo.getVelocity().operator_multiply(deltaT),mo);
				}				
			}
		}
			
	}
	
	public void add(MovableObject o){
		if(o.getMaxSpeed() > maxSpeed)
			this.maxSpeed = o.getMaxSpeed();
		this.movingObject.add(o);
	}


	public class Pair<L,R> {
		private L l;
		private R r;
	
		public Pair(L l, R r){
			this.l = l;
			this.r = r;
		}
		@Override
		public boolean equals(Object other){
			if (other == null) return false;
		    if (other == this) return true;
		    if (!(other instanceof Pair))return false;
		    Pair<L,R> o = (Pair<L,R>) other;
			if(l == o.l){
				if(r == o.r){
					return true;
				}
			}
			if(l == o.r){
				if(r == o.l){
					return true;
				}
			}
			return false;
		}
	
	}
	public void changeMaxRadius(float radius){
		if(radius > this.maxRadius){
			this.maxRadius = radius;
		}
	}
}