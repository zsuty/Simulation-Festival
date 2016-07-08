package simulator.environment;

import java.util.ArrayList;
import java.util.Random;

import simulator.environment.dataStructure.quadTree.QuadTree;
import simulator.environment.movingManager.MovingManager;
import simulator.environment.worldObject.AgentBody;
import simulator.environment.worldObject.Bar;
import simulator.environment.worldObject.MovableObject;
import simulator.environment.worldObject.Scene;
import simulator.environment.worldObject.Toilet;
import simulator.environment.worldObject.WorldObject;
import simulator.linkBodytoAgent.PerceivedObject;
public class Environment {
	private QuadTree tree;
	
	private ArrayList<AgentBody> bodies;
	private ArrayList<Scene> scenes;
	private ArrayList<WorldObject> obs;
	private float time = 0;
	private float timeChange = 120;
	private MovingManager movingManager = new MovingManager();
	public Environment (float width, float height){
		tree = new QuadTree(width,height);
		bodies = new ArrayList<AgentBody> ();
		scenes = new ArrayList<Scene>();
		obs = new ArrayList<WorldObject>();
	}
	
	//TODO
	public void live(float Dt){
		this.movingManager.moveAllWithInitialTest(this.tree,Dt,obs);
		this.changeMusic(Dt);
		
	}
	public void perceive (){
		for(AgentBody b : bodies){
			b.getLinkedAgent().sendPerceptions(this.BodyPerception(b));
		}
	}
	
	
	private ArrayList<PerceivedObject> BodyPerception(AgentBody a){
		ArrayList<WorldObject> tempList = this.tree.getObjectInShape(a.getFieldOfView());
		tempList.remove(a);
		ArrayList<PerceivedObject> perceptObject = new ArrayList<PerceivedObject> ();
		for(WorldObject o : tempList){
				perceptObject.add(new PerceivedObject(o, a.getPosition()));
		}
		
		return perceptObject;
	}
	
	public QuadTree getTree(){
		return this.tree;
	}
	public void addObject(WorldObject o){
		this.tree.insertObject(o);
		this.movingManager.changeMaxRadius(o.getShape().getRadius());
		if(o instanceof MovableObject)
			this.movingManager.add((MovableObject) o);
		if(o instanceof AgentBody)
			this.bodies.add((AgentBody)o);
		if(o instanceof Scene){
			this.scenes.add((Scene) o);
			this.obs.add(o);
		}
		if(o instanceof Bar){
			this.obs.add(o);
		}
		if(o instanceof Toilet){
			this.obs.add(o);
		}
	}
	private synchronized void changeMusic(float dt){
		if(time + dt >= timeChange){
			Random randomGenerator = new Random();
			for(Scene s : this.scenes){
				s.setMusicPlay(randomGenerator.nextInt(3));
			}
		}
		time += dt;
		time %= timeChange + 1;
	}
}
