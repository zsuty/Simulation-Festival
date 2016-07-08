package simulator.agent.mind;

import java.util.ArrayList;

import math.Vector2f;
import simulator.linkBodytoAgent.PerceivedObject;
import simulator.linkBodytoAgent.linkAgentBody;

public abstract class Agent {
	private linkAgentBody linkedBody;
	
	private ArrayList<PerceivedObject> perceivedObject;
	
	public Agent (linkAgentBody linkedBody){
		this.linkedBody = linkedBody;
	}
	
	public ArrayList<PerceivedObject> getPerceivedObject() {
		return perceivedObject;
	}
	public void setPerceivedObject(ArrayList<PerceivedObject> perceivedObject) {
		this.perceivedObject = perceivedObject;
	}
	
	public linkAgentBody getLinkedBody(){
		return this.linkedBody;
	}
	public void setLinkedBody (linkAgentBody link){
		this.linkedBody = link;
	}
	public abstract void think();
	
}
