package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.Simulator;

public class Window extends JFrame implements ActionListener, MouseListener {
	
	private static final long serialVersionUID = 7446192599263749847L;
	
	private GraphicalMap gMap;
	
	private JPanel contentPanel;
	private Thread t;
	
	public Window (Simulator sim){
		super();
	
	
		this.setTitle("VI51 Project");
		this.setSize(800,600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		this.gMap = new GraphicalMap(sim.getTree());
		this.gMap.addMouseListener(this);
		this.gMap.setPreferredSize(new Dimension(this.getWidth(), 1000));
		
		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		
		
		contentPanel.add(gMap);
		this.setContentPane(contentPanel);
		
		
		t = new Thread(new Animation());
		t.start();
		sim.start();
	}
	
	/* Thread and repaint function */
	private void go(){
		while(true){
			this.gMap.setVariable();
			this.gMap.repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	class Animation implements Runnable {
		public void run() {
			go();
		}
	}
	/* -----------------------------*/
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
