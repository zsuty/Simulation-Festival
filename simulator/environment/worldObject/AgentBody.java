package simulator.environment.worldObject;

import math.Point2f;
import math.Shape2f;
import math.Vector2f;
import simulator.linkBodytoAgent.linkAgentBody;

public abstract class AgentBody extends MovableObject{

	private Shape2f<?> fieldOfView;
	private linkAgentBody linkedAgent;
	
	public AgentBody (linkAgentBody linkedAgent, Point2f position, float radius,float maxSpeed,Vector2f orientation,float maxRotationSpeed, Shape2f<?> fieldOfView){
		super(position, radius, maxSpeed,orientation,maxRotationSpeed);
		this.linkedAgent = linkedAgent;
		this.fieldOfView = fieldOfView;
	}
	
	public linkAgentBody getLinkedAgent(){
		return this.linkedAgent;
	}
	public void setLinkAgentBody(linkAgentBody link){
		this.linkedAgent = link;
	}
	public Shape2f<?> getFieldOfView(){
		return this.fieldOfView;
	}
	
	@Override
	public synchronized void Translate(Vector2f v) {
		super.Translate(v);
		this.fieldOfView = this.fieldOfView.translate(v);
		
	}
}
