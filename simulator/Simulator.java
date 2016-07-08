
package simulator;

import java.util.ArrayList;
import java.util.Random;

import math.Circle2f;
import math.Point2f;
import math.Vector2f;
import simulator.agent.AgentSimulator;
import simulator.agent.mind.Agent;
import simulator.agent.mind.Festivalgoer;
import simulator.environment.Environment;
import simulator.environment.dataStructure.quadTree.QuadTree;
import simulator.environment.worldObject.AgentBody;
import simulator.environment.worldObject.Bar;
import simulator.environment.worldObject.FestivalgoerBody;
import simulator.environment.worldObject.Scene;
import simulator.environment.worldObject.Toilet;
import simulator.linkBodytoAgent.PerceivedObject;
import simulator.linkBodytoAgent.linkAgentBody;

public class Simulator extends Thread{
	private Environment environment;
	private AgentSimulator agentSim;
	
	private boolean pause = false;
	private boolean close = false;
	
	
	public Simulator(float width,float height){
		environment = new Environment(width,height);
		agentSim = new AgentSimulator();
		ArrayList<PerceivedObject> scenes = new ArrayList<PerceivedObject> ();
		ArrayList<PerceivedObject> toilets = new ArrayList<PerceivedObject> ();
		ArrayList<PerceivedObject> bars = new ArrayList<PerceivedObject> ();
		createWorld(scenes,toilets,bars);
		
		createFestivalgoers(500, 50, scenes, toilets, bars, new Point2f(-25,25), 0.5f, 1,20);
	}
	@Override
	public void run() {
		while(!close){
			if(!pause){
				environment.perceive();
				agentSim.think();
				environment.live(0.5f);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public synchronized void changePause(){
		this.pause = !this.pause;
	}
	public synchronized void end(){
		this.close = true;
	}
	public QuadTree getTree(){
		return environment.getTree();
	}
	
	private void createWorld(ArrayList<PerceivedObject> scenes, ArrayList<PerceivedObject> toilets, ArrayList<PerceivedObject> bars){
		Scene tempScene = new Scene(new Point2f(-50,0), 10,0);
		environment.addObject(tempScene);
		scenes.add(new PerceivedObject(tempScene, new Point2f(0,0)));

		tempScene = new Scene(new Point2f(50,0), 10,1);
		environment.addObject(tempScene);
		scenes.add(new PerceivedObject(tempScene, new Point2f(0,0)));

		tempScene = new Scene(new Point2f(0,-50), 10,2);
		environment.addObject(tempScene);
		scenes.add(new PerceivedObject(tempScene, new Point2f(0,0)));
		
		Toilet tempT =new Toilet(new Point2f(-30,30),1);
		environment.addObject(tempT);
		toilets.add(new PerceivedObject(tempT, new Point2f(0,0)));

		tempT =new Toilet(new Point2f(30,30), 1);
		environment.addObject(tempT);
		toilets.add(new PerceivedObject(tempT, new Point2f(0,0)));
		
		tempT =new Toilet(new Point2f(-30,-30), 1);
		environment.addObject(tempT);
		toilets.add(new PerceivedObject(tempT, new Point2f(0,0)));
		
		tempT =new Toilet(new Point2f(30,-30), 1);
		environment.addObject(tempT);
		toilets.add(new PerceivedObject(tempT, new Point2f(0,0)));
		

		Bar tempB =new Bar(new Point2f(0,30), 3);
		environment.addObject(tempB);
		bars.add(new PerceivedObject(tempB, new Point2f(0,0)));
		
		/*tempB =new Bar(new Point2f(-25,-25), 3);
		environment.addObject(tempB);
		bars.add(new PerceivedObject(tempB, new Point2f(0,0)));
		
		tempB =new Bar(new Point2f(25,-25), 3);
		environment.addObject(tempB);
		bars.add(new PerceivedObject(tempB, new Point2f(0,0)));*/
	}
	private void createFestivalgoers(int nb, int nbColumn,ArrayList<PerceivedObject> scenes, ArrayList<PerceivedObject> toilets, ArrayList<PerceivedObject> bars,Point2f centre, float radius,float maxSpeed,float radiusView){
		Random randomGenerator = new Random();
		int l,c,music;
		for(int i = 0; i < nb ; ++i){
			music =randomGenerator.nextInt(3);
			Agent a = new Festivalgoer(null,music , (music+1)%3,scenes,toilets,bars);
			l = i/nbColumn;
			c = i%nbColumn;
			Point2f position = centre.operator_plus(new Vector2f(3*radius*c,-3*radius*l));
			AgentBody body= new FestivalgoerBody(null, position, radius,maxSpeed, new Vector2f(0,1), 0.5f, new Circle2f(position, radiusView));
			linkAgentBody lab = new linkAgentBody(body, a);
			
			a.setLinkedBody(lab);
			body.setLinkAgentBody(lab);
			
			environment.addObject(body);
			agentSim.addAgent(a);
		}
	}
}
