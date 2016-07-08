package simulator.linkBodytoAgent;

import math.Circle2f;
import math.Point2f;
import simulator.environment.worldObject.AgentBody;
import simulator.environment.worldObject.Bar;
import simulator.environment.worldObject.Scene;
import simulator.environment.worldObject.Toilet;
import simulator.environment.worldObject.WorldObject;

public class PerceivedObject {
	private WorldObject object;
	public PerceivedObject (WorldObject o, Point2f repere){
		this.object = o;
		//this.object.Translate(new Vector2f(repere).operator_multiply(-1));
	}
	
	public Point2f getPosition (){
		return this.object.getPosition();
	}
	public Circle2f getShape (){
		return this.object.getShape();
	}
	
	public boolean isScene(){
		if(object instanceof Scene)
			return true;
		return false;
	}
	public boolean isBar(){
		if(object instanceof Bar)
			return true;
		return false;
	}
	public boolean isToilet(){
		if(object instanceof Toilet)
			return true;
		return false;
	}
	public boolean isAgentBody(){
		if(object instanceof AgentBody)
			return true;
		return false;
	}
	
	public Integer getMusicPlay(){
		if(this.isScene()){
			Scene tempScene = (Scene)object;
			return new Integer(tempScene.getMusicPlay());
		}
		return null;
	}
}
