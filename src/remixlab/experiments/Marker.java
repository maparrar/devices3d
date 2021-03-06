package remixlab.experiments;

import java.awt.Color;
import processing.core.PApplet;
import processing.core.PVector;
import remixlab.proscene.*;

/**
 * Marker to check the pass of the camera
 * */
public class Marker {
	PApplet parent;
	Scene scene;
	InteractiveFrame frame;		// Frame using to draw the marker
	PVector position;			// Position vector of the Frame
	Quaternion orientation;		// Orientation of the Frame
	float radius;				// Radius of the marker
	Color color;				// Color of marker
	int detail;					// Detail of the cylinder
	
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	public Marker(PApplet p,Scene sc){
		this(p,sc,new PVector(p.random(100),p.random(100),p.random(100)),new Quaternion(),10,new Color((int)p.random(255),(int)p.random(255),(int)p.random(255)));
	}
	public Marker(PApplet p,Scene sc,PVector pos){
		this(p,sc,pos,new Quaternion(),10,new Color((int)p.random(255),(int)p.random(255),(int)p.random(255)));
	}
	public Marker(PApplet p,Scene sc,PVector pos,Color col){
		this(p,sc,pos,new Quaternion(),10,col);
	}
	public Marker(PApplet p,Scene sc,PVector pos,float _radius,Color col){
		this(p,sc,pos,new Quaternion(),_radius,col);
	}
	public Marker(PApplet p,Scene sc,PVector pos,Quaternion orient){
		this(p,sc,pos,orient,10,new Color((int)p.random(255),(int)p.random(255),(int)p.random(255)));
	}
	public Marker(PApplet p,Scene sc,PVector pos,Quaternion orient,float radiusM,Color col){
		parent = p;
		scene = sc;
		orientation=orient;
		position=pos;
		radius=radiusM;
		color=col;
		detail=20;
		configureFrame();
	}	
	/**
	 * Configure the frame with the data of the part
	 * */
	private void configureFrame(){
		frame = new InteractiveFrame(scene);
		frame.removeFromMouseGrabberPool();	
		frame.setPosition(position);
		frame.setOrientation(orientation);
	}	
	/**
	 * Draw the marker
	 * */
	public void draw(){
		parent.pushMatrix();
			parent.pushStyle();
				frame.applyTransformation();
//				parent.noStroke();
//				parent.fill(color.getRGB());
//				parent.ellipse(0,0,10,10);
				parent.stroke(color.getRGB());
				parent.noFill();
				parent.sphere(radius);
			parent.popStyle();
		parent.popMatrix();
	}
	/**
	 * Return a string with data of the curve
	 * */
	public String toString(){
		String data1="radius: "+radius+"\n";
		String data2="position:("+position.x+","+position.y+","+position.z+")\n";
		return "*****\n"+data1+data2+"*****";
	}
}
