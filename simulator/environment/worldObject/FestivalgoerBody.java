package simulator.environment.worldObject;

import math.Point2f;
import math.Shape2f;
import math.Vector2f;
import simulator.linkBodytoAgent.linkAgentBody;

public class FestivalgoerBody extends AgentBody{

	public FestivalgoerBody(linkAgentBody linkedAgent, Point2f position, float radius, float maxSpeed,
			Vector2f orientation, float maxRotationSpeed, Shape2f<?> fieldOfView) {
		super(linkedAgent, position, radius, maxSpeed, orientation, maxRotationSpeed, fieldOfView);
	}

}
