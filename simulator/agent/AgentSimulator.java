package simulator.agent;

import java.util.ArrayList;

import simulator.agent.mind.Agent;
public class AgentSimulator {

	ArrayList <Agent> agents;
	
	public AgentSimulator(){
		agents = new ArrayList<Agent> ();
	}
	public void think(){
		for(Agent a : agents){
			a.think();
		}
	}
	public void addAgent(Agent a) {
		this.agents.add(a);
		
	}
	
}
