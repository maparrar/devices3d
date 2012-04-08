/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Interval Class
 * */
package remixlab.experiments;

import java.awt.Color;
import processing.core.PApplet;
import processing.core.PVector;
import remixlab.proscene.*;

/**
 * Interval to check the pass of the camera, draws a sphere at the end point
 * */
public class Interval {
	PApplet parent;
	Scene scene;
	InteractiveFrame frame;		// Frame using to draw the Interval
	Quaternion orientation;		// Orientation of the Frame
	Color color;				// Color of Interval
	int detail;					// Detail
	
	PVector ini;				// Position of the last interval sphere
	PVector end;				// Position vector of the Frame
	float D;					// Distance between ini and end
	float W;					// Diameter of the end-sphere
	float ID;					// Index of Difficulty to this interval
	
	float radius;				// Radius=W/2
	
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	public Interval(PApplet p,Scene sc,PVector _ini,PVector _end,float _D,float _W,float _ID,Color _color){
		parent = p;
		scene = sc;
		ini=_ini;
		end=_end;
		D=_D;
		W=_W;
		ID=_ID;
		color=_color;
		detail=20;
		radius=W/2;
		configureFrame();
	}
	
	/////////////////////////////////////// GET AND SET ///////////////////////////////////////
	public void setOrientation(Quaternion orient){
		orientation=orient;
	}
	
	/////////////////////////////////////// METHODS ///////////////////////////////////////
	/**
	 * Configure the frame with the data of the part
	 * */
	private void configureFrame(){
		frame = new InteractiveFrame(scene);
		frame.removeFromMouseGrabberPool();	
		frame.setPosition(end);
		frame.setOrientation(orientation);
	}	
	/**
	 * Draw the Interval
	 * */
	public void draw(){
		parent.pushMatrix();
			parent.pushStyle();
				frame.applyTransformation();
				parent.stroke(color.getRGB());
				parent.noFill();
				parent.sphere(radius);
			parent.popStyle();
		parent.popMatrix();
	}
	/**
	 * Return a string with data of the Interval
	 * */
	public String toString(){
		String data1="radius: "+radius+"\n";
		String data2="ini:("+ini.x+","+ini.y+","+ini.z+")\n";
		String data3="end:("+end.x+","+end.y+","+end.z+")\n";
		return "*****\n"+data1+data2+data3+"*****";
	}
}
