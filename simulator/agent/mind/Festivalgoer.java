package simulator.agent.mind;

import java.util.ArrayList;
import java.util.Random;

import math.Point2f;
import math.Vector2f;
import simulator.linkBodytoAgent.PerceivedObject;
import simulator.linkBodytoAgent.linkAgentBody;

public class Festivalgoer extends Agent{
	
	private final int musicLike;
	private final int musiDislike;
	
	private ArrayList<PerceivedObject> scenes;
	private ArrayList<PerceivedObject> toilets;
	private ArrayList<PerceivedObject> bars;
	
	private float needBar = 100;// form 0 to 100, 100 mean no need
	private float decreaseBar = -0.1f;
	private float needToilet = 100;// form 0 to 100, 100 mean no need
	private float decreaseToilet = -0.1f;
	private float speed = 0.5f;
	
	public Festivalgoer (linkAgentBody linkedBody, int musicLike,int musicDislike,ArrayList<PerceivedObject> scenes,ArrayList<PerceivedObject> toilets,ArrayList<PerceivedObject> bars){
		super(linkedBody);
		this.musicLike = musicLike;
		this.musiDislike = musicDislike;
		this.scenes = scenes;
		this.bars = bars;
		this.toilets = toilets;
		Random randomGenerator = new Random();
		this.needToilet = randomGenerator.nextFloat() * 100;
		this.needBar = randomGenerator.nextFloat() * 100;
	}
	
	@Override
	public void think() {
		Vector2f influence = chooseAction(); 
		if(!influence.epsilonNul(0.001f))
			influence = influence.operator_plus(this.getRepulsionForce());
		this.getLinkedBody().aplyInfluence(influence);
	}
	//  from 0 to 100, 0 mean it's boring
	private float enjoy(){
		PerceivedObject tempScene = this.getClosestScene();
		if(tempScene == null){
			return 0;
		}
		float base = 30;
		if(this.isScenePerceived())
			base = 50;
		float distanceFromScene = ((new Vector2f(tempScene.getPosition())).operator_minus(new Vector2f(this.getLinkedBody().getPosition()))).length();
		distanceFromScene -= tempScene.getShape().getRadius();
		distanceFromScene /= 2.0;
		float coef = 0;
		if(tempScene.getMusicPlay() == this.musicLike)
			coef = 50;
		if(tempScene.getMusicPlay() == this.musiDislike)
			coef = -50;
		float enjoy = base + coef/(1.0f + distanceFromScene);
		return enjoy;
	}
	private float simulEnjoy(Point2f position){
		PerceivedObject tempScene = this.getClosestScene();
		if(tempScene == null){
			return 0;
		}
		float base = 50;
		float distanceFromScene = ((new Vector2f(tempScene.getPosition())).operator_minus(new Vector2f(position))).length();
		distanceFromScene -= tempScene.getShape().getRadius();
		distanceFromScene /= 2.0;
		float coef = 0;
		if(tempScene.getMusicPlay() == this.musicLike)
			coef = 50;
		if(tempScene.getMusicPlay() == this.musiDislike)
			coef = -50;
		float enjoy = base + coef/(1.0f + distanceFromScene);
		return enjoy;
	}
	private void decreaseNeeds(){
		this.needBar += this.decreaseBar;
		if(this.needBar < 0)
			this.needBar = 0;
		this.needToilet += this.decreaseToilet;
		if(this.needToilet < 0)
			this.needToilet = 0;
	}
	
	public int getMusicLike() {
		return musicLike;
	}

	public int getMusiDislike() {
		return musiDislike;
	}
	private PerceivedObject getClosestScene(){
		float d = 1000000;
		PerceivedObject tempS = null;
		for(PerceivedObject s : this.scenes){
			Vector2f tempV2 = (new Vector2f(s.getPosition())).operator_minus(new Vector2f(this.getLinkedBody().getPosition()));
			if(tempV2.length() < d){
				d = tempV2.length();
				tempS = s;
			}
		}
		return tempS;
	}
	private PerceivedObject getClosestToilet(){
		float d = 1000000;
		PerceivedObject tempT = null;
		for(PerceivedObject t : this.toilets){
			Vector2f tempV2 = (new Vector2f(t.getPosition())).operator_minus(new Vector2f(this.getLinkedBody().getPosition()));
			if(tempV2.length() < d){
				d = tempV2.length();
				tempT = t;
			}
		}
		return tempT;
	}
	private PerceivedObject getClosestBar(){
		float d = 1000000;
		PerceivedObject tempB = null;
		for(PerceivedObject b : this.bars){
			Vector2f tempV2 = (new Vector2f(b.getPosition())).operator_minus(new Vector2f(this.getLinkedBody().getPosition()));
			if(tempV2.length() < d){
				d = tempV2.length();
				tempB = b;
			}
		}
		return tempB;
	}
	private PerceivedObject getClosestSceneLike(){
		float d = 1000000;
		PerceivedObject tempS = null;
		for(PerceivedObject s : this.scenes){
			if(s.getMusicPlay() == this.musicLike){
				Vector2f tempV2 = (new Vector2f(s.getPosition())).operator_minus(new Vector2f(this.getLinkedBody().getPosition()));
				if(tempV2.length() < d){
					d = tempV2.length();
					tempS = s;
				}
			}
		}
		return tempS;
	}
	private boolean isScenePerceived(){
		for(PerceivedObject o : this.getPerceivedObject()){
			if(o.isScene()){
				return true;
			}
		}
		return false;
	}
	private Vector2f getRepulsionForce(){
		Vector2f repulsion = new Vector2f(0,0);
		
		for(PerceivedObject o : this.getPerceivedObject()){
			Vector2f tempV2 = new Vector2f(this.getLinkedBody().getPosition()).operator_minus(new Vector2f(o.getPosition()));
			tempV2.setLength(1/(tempV2.length()*tempV2.length()));
			repulsion = repulsion.operator_plus(tempV2);
		}
		return repulsion;
	}
	private Vector2f chooseAction(){
		simulNeeds sim = new simulNeeds(this.needToilet,this.needBar,this.decreaseToilet,this.decreaseBar,new Point2f(this.getLinkedBody().getPosition()));
		float min =Math.min(Math.min(this.needBar , this.needToilet) , enjoy());
		int a = 0;
		Vector2f tempV;
		/******* chose action ********/
		tempV = simulGoToilet(sim);
		sim.position.add(tempV);
		if(!tempV.epsilonNul(0.001f))
			simulGoToilet(sim);
		float tempMin = Math.min(Math.min(sim.needT , sim.needB), simulEnjoy(sim.position));
		if(tempMin >= min){
			a = 1;
			min = tempMin;
		}
			
		
		sim = new simulNeeds(this.needToilet,this.needBar,this.decreaseToilet,this.decreaseBar,new Point2f(this.getLinkedBody().getPosition()));
		tempV = simulGoBar(sim);
		sim.position.add(tempV);
		if(!tempV.epsilonNul(0.001f))
			simulGoBar(sim);
		tempMin = Math.min(Math.min(sim.needT , sim.needB), simulEnjoy(sim.position));
		if(tempMin >= min){
			a = 2;
			min = tempMin;
		}
		
		sim = new simulNeeds(this.needToilet,this.needBar,this.decreaseToilet,this.decreaseBar,new Point2f(this.getLinkedBody().getPosition()));
		tempV = simulGoLikeScene(sim);
		sim.position.add(tempV);
		tempMin = Math.min(Math.min(sim.needT , sim.needB), simulEnjoy(sim.position));
		if(tempMin >= min){
			a = 3;
			min = tempMin;
		}
		
		/*sim = new simulNeeds(this.needToilet,this.needBar,this.decreaseToilet,this.decreaseBar,new Point2f(this.getLinkedBody().getPosition()));
		tempV = simulGoAwayFromScene(sim);
		sim.position.add(tempV);
		tempMin = Math.min(Math.min(sim.needT , sim.needB), simulEnjoy(sim.position));
		if(tempMin >= min){
			a = 4;
			min = tempMin;
		}*/
		
		/******* apply action ********/
		switch(a){
		case 1:
			return goToilet();
		case 2:
			return goBar();
		case 3: 
			return goLikeScene();
		/*case 4:
			return goAwayFromScene();*/
		default:
			this.decreaseNeeds();
			return new Vector2f(0,0);
		}
	}
	/****** simulation go Toilet *******/
	private Vector2f simulGoToilet(simulNeeds sim){
		Vector2f tempV2 = new Vector2f(0,0);
		if(!simulUseToilet(sim)){
			PerceivedObject tempT= this.getClosestToilet();
			tempV2 = (new Vector2f(tempT.getPosition()).operator_minus(new Vector2f(sim.position)));
			tempV2.setLength(tempV2.length() - tempT.getShape().getRadius() - 0.5f);
			sim.needT += sim.decreaseT * ((tempV2.length() / this.speed) * 0.5f);
			if(sim.needT < 0)
				sim.needT = 0f;
			sim.needB += sim.decreaseB * ((tempV2.length() / this.speed) * 0.5f);
			if(sim.needB < 0)
				sim.needB = 0f;
		}
		return tempV2;
	}
	private boolean simulUseToilet(simulNeeds sim){
		PerceivedObject tempT = getClosestToilet();
		if(new Vector2f(tempT.getPosition()).operator_minus(new Vector2f(sim.position)).length() - tempT.getShape().getRadius() >= 1)
			return false;
		sim.needT += 100;
		if(sim.needT > 100)
			sim.needT = (float) 100;
		sim.decreaseT = -0.1f;
		return true;
	}
	private Vector2f goToilet(){
		Vector2f tempV2 = new Vector2f(0,0);
		if(!useToilet()){
			PerceivedObject tempT= this.getClosestToilet();
			tempV2 = (new Vector2f(tempT.getPosition()).operator_minus(new Vector2f(this.getLinkedBody().getPosition())));
			tempV2.setLength(tempV2.length() - tempT.getShape().getRadius() - 0.5f);
		}
		this.decreaseNeeds();
		return tempV2;
	}
	private boolean useToilet(){
		PerceivedObject tempT = getClosestToilet();
		if(new Vector2f(tempT.getPosition()).operator_minus(new Vector2f(this.getLinkedBody().getPosition())).length() - tempT.getShape().getRadius() >= 1)
			return false;
		this.needToilet += 70;
		if(this.needToilet > 100)
			this.needToilet = 100;
		this.decreaseToilet = -0.1f;
		return true;
	}
	/*********************************/
	
	/****** simulation go Bar *******/
	private Vector2f simulGoBar(simulNeeds sim){
		Vector2f tempV2 = new Vector2f(0,0);
		if(!simulEat(sim)){
			PerceivedObject tempB = this.getClosestBar();
			tempV2 = (new Vector2f(tempB.getPosition()).operator_minus(new Vector2f(sim.position)));
			tempV2.setLength(tempV2.length() - tempB.getShape().getRadius() - 0.5f);
			sim.needT += sim.decreaseT * ((tempV2.length() / this.speed) * 0.5f);
			if(sim.needT < 0)
				sim.needT = 0f;
			sim.needB += sim.decreaseB * ((tempV2.length() / this.speed) * 0.5f);
			if(sim.needB < 0)
				sim.needB = 0f;
		}
		return tempV2;
	}
	private boolean simulEat(simulNeeds sim){
		PerceivedObject tempB = getClosestBar();
		if(new Vector2f(tempB.getPosition()).operator_minus(new Vector2f(sim.position)).length() - tempB.getShape().getRadius() > 1)
			return false;
		sim.needB += 70;
		if(sim.needB > 100)
			sim.needB = 100f;
		sim.decreaseT -= 0.1f;
		return true;
	}
	private Vector2f goBar(){
		Vector2f tempV2 = new Vector2f(0,0);
		if(!eat()){
			PerceivedObject tempB= this.getClosestBar();
			tempV2 = (new Vector2f(tempB.getPosition()).operator_minus(new Vector2f(this.getLinkedBody().getPosition())));
			tempV2.setLength(tempV2.length() - tempB.getShape().getRadius() - 0.5f);
		}
		this.decreaseNeeds();
		return tempV2;
	}
	private boolean eat(){
		PerceivedObject tempB = getClosestBar();
		if(new Vector2f(tempB.getPosition()).operator_minus(new Vector2f(this.getLinkedBody().getPosition())).length() - tempB.getShape().getRadius() > 1)
			return false;
		this.needBar += 70;
		if(this.needBar > 100)
			this.needBar = 100;
		this.decreaseToilet -= 0.1f;
		return true;
	}
	/*********************************/
	/****** simulation go to like scene *******/
	private Vector2f simulGoLikeScene(simulNeeds sim){
		Vector2f tempV2 = new Vector2f(0,0);
		PerceivedObject tempS= this.getClosestSceneLike();
		if(tempS != null){
			tempV2 = (new Vector2f(tempS.getPosition()).operator_minus(new Vector2f(sim.position)));
			tempV2.setLength(tempV2.length() - tempS.getShape().getRadius() - 0.5f);
			sim.needT +=sim. decreaseT * ((tempV2.length() / this.speed) * 0.5f);
			if(sim.needT < 0)
				sim.needT = 0f;
			sim.needB +=sim. decreaseB * ((tempV2.length() / this.speed) * 0.5f);
			if(sim.needB < 0)
				sim.needB = 0f;
		}
		return tempV2;
	}
	private Vector2f goLikeScene(){
		Vector2f tempV2 = new Vector2f(0,0);
		PerceivedObject tempS= this.getClosestSceneLike();
		if(tempS != null){
			tempV2 = (new Vector2f(tempS.getPosition()).operator_minus(new Vector2f(this.getLinkedBody().getPosition())));
			tempV2.setLength(tempV2.length() - tempS.getShape().getRadius() - 0.5f);
		}
		this.decreaseNeeds();
		return tempV2;
	}
	/*********************************/
	/****** simulation go away from closest scene *******/
	private Vector2f simulGoAwayFromScene(simulNeeds sim){
		Vector2f tempV2 = new Vector2f(0,0);
		PerceivedObject tempS= this.getClosestScene();
		if(tempS != null){
			if(tempS.getMusicPlay() == this.musiDislike){
				tempV2 = (new Vector2f(sim.position).operator_minus(new Vector2f(tempS.getPosition())));
				tempV2.setLength(tempV2.length() - tempS.getShape().getRadius() - 0.5f);
				sim.needT += sim.decreaseT * ((tempV2.length() / this.speed) * 0.5f);
				if(sim.needT < 0)
					sim.needT = 0f;
				sim.needB += sim.decreaseB * ((tempV2.length() / this.speed) * 0.5f);
				if(sim.needB < 0)
					sim.needB = 0f;
			}
		}
		return tempV2;
	}
	private Vector2f goAwayFromScene(){
		Vector2f tempV2 = new Vector2f(0,0);
		PerceivedObject tempS= this.getClosestScene();
		if(tempS != null){
			if(tempS.getMusicPlay() == this.musiDislike){
				tempV2 = (new Vector2f(this.getLinkedBody().getPosition()).operator_minus(new Vector2f(tempS.getPosition())));
				tempV2.setLength(tempV2.length() - tempS.getShape().getRadius() - 0.5f);
				tempV2.setLength(1/(tempV2.length() * tempV2.length()));
			}
		}
		this.decreaseNeeds();
		return tempV2;
	}
	/*********************************/
	public class simulNeeds{
		public float needT;
		public float needB;
		public float decreaseT;
		public float decreaseB;
		public Point2f position;
		
		public simulNeeds(float needT,float needB,float decreaseT, float decreaseB,Point2f position){
			this.needB = needB;
			this.needT = needT;
			this.decreaseB = decreaseB;
			this.decreaseT = decreaseT;
			this.position = position;
		}
	}
}


