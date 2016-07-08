package gui.drawingTool;

import java.awt.Color;

public class DrawingObject{
	private int centreX, centreY;
	private int width,height;
	private Color c;
	
	public DrawingObject(int centreX,int centreY, int width,int height,Color c){
		this.centreX = centreX;
		this.centreY = centreY;
		this.width = width;
		this.height = height;
		this.c = c;
	}
	
	public int getCentreX() {
		return centreX;
	}
	public void setCentreX(int centreX) {
		this.centreX = centreX;
	}
	public int getCentreY() {
		return centreY;
	}
	public void setCentreY(int centreY) {
		this.centreY = centreY;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public Color getC() {
		return c;
	}

	public void setC(Color c) {
		this.c = c;
	}
	
	
}
