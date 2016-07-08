package simulator.environment.worldObject;

import math.Point2f;

public class Scene extends WorldObject{

	private int musicPlay;
	public Scene(Point2f p, float radius,int musicPlay) {
		super(p, radius);
		this.musicPlay = musicPlay;
	}
	public int getMusicPlay() {
		return musicPlay;
	}
	public void setMusicPlay(int musicPlay) {
		this.musicPlay = musicPlay;
	}
}
