import gui.Window;
import simulator.Simulator;

public class Launcher {

	public static void main(String[] args) {
		Simulator sim = new Simulator(400, 400);
		Window w = new Window(sim);
		w.setVisible(true);

	}

}
