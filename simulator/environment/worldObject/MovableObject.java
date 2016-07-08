package simulator.environment.worldObject;

import java.util.ArrayList;

import math.Point2f;
import math.Vector2f;

public abstract class MovableObject extends WorldObject{
	private Vector2f velocity;
	private final float maxSpeed;
	
	private Vector2f orientation;
	private float rotationSpeed;
	private final float maxRotationSpeed;
	
	private ArrayList<Vector2f> unAuthorizeDirections = new ArrayList<Vector2f> ();
	
	public MovableObject(Point2f position, float radius,float maxSpeed,Vector2f orientation,float maxRotationSpeed){
		super(position, radius);
		this.maxSpeed = maxSpeed;
		this.velocity = new Vector2f(0,0);
		this.orientation = new Vector2f(orientation);
		this.maxRotationSpeed = maxRotationSpeed;
		this.rotationSpeed = 0;
	}
	
	public float getMaxSpeed (){
		return this.maxSpeed;
	}
	public synchronized Vector2f getVelocity() {
		return velocity;
	}
	public synchronized void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
		if(velocity.length() > maxSpeed)
			this.velocity.setLength(this.maxSpeed);
	}

	/*
	 * return the minimun dt if there is a collision 
	 * return null if there is no collision
	 */
	public Float collision(WorldObject obstacle) {
		/*
		 * all object have cicrle box
		 * 3 possibility : no collision, 1 dt, 2dt
		 */
		float r,a,b,c,discriminant;
		Vector2f v,d;
		if(obstacle instanceof MovableObject)
			v = (this.velocity.operator_minus(((MovableObject)obstacle).velocity));
		else
			v = this.velocity;
		if(v.isEmpty())
			return null;
		//v = v.operator_multiply(deltaT);
		d = this.getPosition().operator_minus(obstacle.getPosition());
		r = this.getShape().getRadius() + obstacle.getShape().getRadius();
		
		a = v.lengthSquared();
		b = 2*(v.dot(d));
		c = d.lengthSquared() - (r*r);
		discriminant = b*b - 4*(a*c);
		if(discriminant < 0){
			return null;
		}
		else if(discriminant > 0){
			float dt1 = (-b + (float)(Math.sqrt(discriminant)))/(2*a);
			float dt2 = (-b - (float) (Math.sqrt(discriminant)))/(2*a);
			return new Float(Math.min(dt1, dt2));
		}
		else{
			return new Float(-b/(2*a));
		}
	}

	public Vector2f getOrientation() {
		return orientation;
	}

	public void setOrientation(Vector2f orientation) {
		this.orientation = orientation;
	}

	public float getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(float rotationSpeed) {
		if(rotationSpeed < 0){
			if(rotationSpeed > -this.maxRotationSpeed){
				this.rotationSpeed = rotationSpeed;
			}
			else{
				this.rotationSpeed = -this.maxRotationSpeed;
			}
		}
		else{
			if(rotationSpeed < this.maxRotationSpeed){
				this.rotationSpeed = rotationSpeed;
			}
			else{
				this.rotationSpeed = this.maxRotationSpeed;
			}
		}
	}

	public float getMaxRotationSpeed() {
		return maxRotationSpeed;
	}
	
	public void addUnAuthorizedDirection(Vector2f direction){
		this.unAuthorizeDirections.add(direction);
	}
	
	public void computeVelocity(){
		Vector2f tempV = new Vector2f(this.velocity);
		
		if(!this.unAuthorizeDirections.isEmpty()){
			if(this.unAuthorizeDirections.size() == 1){
				Vector2f d = this.unAuthorizeDirections.get(0);
				float dotP = d.dot(tempV);
				if(dotP > 0)
					tempV =tempV.operator_minus(d.operator_multiply(dotP / d.lengthSquared()));
			}
			else{
				for(Vector2f d : this.unAuthorizeDirections){
					float dotP = d.dot(tempV);
					if(dotP > 0)
						tempV =tempV.operator_minus(d.operator_multiply(dotP / d.lengthSquared()));
				}
			}
		}
		this.setVelocity(tempV);
		this.unAuthorizeDirections.clear();
	}
	
}
