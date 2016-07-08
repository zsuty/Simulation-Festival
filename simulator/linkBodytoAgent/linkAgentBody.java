package simulator.linkBodytoAgent;

import java.util.ArrayList;

import math.Point2f;
import math.Vector2f;
import simulator.agent.mind.Agent;
import simulator.environment.worldObject.AgentBody;

public class linkAgentBody {
	private AgentBody agentBody;
	private Agent agentMind;
	
	public linkAgentBody(AgentBody agentBody, Agent agentMind){
		this.agentBody = agentBody;
		this.agentMind = agentMind;
	}
	
	public void sendPerceptions(ArrayList <PerceivedObject> perceivedObject){
		this.agentMind.setPerceivedObject(perceivedObject);
	}
	public void aplyInfluence(Vector2f velocity){
		this.agentBody.setVelocity(velocity);
	}
	
	public Agent getAgentMind(){
		return this.agentMind;
	}
	public Point2f getPosition(){
		return new Point2f(agentBody.getPosition());
	}
}
