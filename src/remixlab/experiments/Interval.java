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
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import remixlab.experiments.Experiment.State;
import remixlab.proscene.*;

/**
 * Interval to check the pass of the camera, draws a sphere at the end point
 * */
public class Interval {
	PApplet p;
	Sketch sketch;
	State state;				//State of the trial
	InteractiveFrame frame;		// Frame using to draw the Interval
	Quaternion orientation;		// Orientation of the Frame
	Color color;				// Color of Interval
	int detail;					// Detail
	
	public PVector ini;				// Position of the last interval sphere
	public PVector end;				// Position vector of the Frame
	public PVector insideInterval;		// Point where inside the camera in Interval frame coordinates
	public PVector insideWorld;		// Point where inside the camera in World frame
	public float D;					// Distance between ini and end
	public float W;					// Diameter of the end-sphere
	public float ID;				// Index of Difficulty to this interval
	public float insideTime;		// Time since the applet starts to the camera inside
	
	float radius;					// Radius=W/2
	
	public ArrayList<PVector> cameraPoints = new ArrayList<PVector>();//Collect points of the camera
	
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	public Interval(PApplet parent,Sketch s,PVector _ini,PVector _end,float _D,float _W,float _ID,Color _color){
		p = parent;
		sketch = s;
		ini=_ini;
		end=_end;
		D=_D;
		W=_W;
		ID=_ID;
		color=_color;
		detail=20;
		radius=W/2;
		configureFrame();
		//Set the starting state
		state=State.BEFORE;
		insideInterval=new PVector(0,0,0);
		insideWorld=new PVector(0,0,0);
	}
	
	/////////////////////////////////////// GET AND SET ///////////////////////////////////////
	public void setOrientation(Quaternion orient){
		orientation=orient;
	}
	/**
	 * Return the state of the trial
	 * */
	public State state(){
		return state;
	}
	/////////////////////////////////////// METHODS ///////////////////////////////////////
	public void init(){
		state=State.RUNNING;
	}
	/**
	 * Configure the frame with the data of the part
	 * */
	private void configureFrame(){
		frame = new InteractiveFrame(sketch);
		frame.removeFromMouseGrabberPool();	
		frame.setPosition(end);
		PVector to = PVector.sub(ini,frame.position());
		frame.setOrientation(new Quaternion(new PVector(0, 0, 1), to));
	}	
	/**
	 * Draw the Interval
	 * */
	public void draw(PVector camera){
		if(state!=State.AFTER){
			if(state==State.INSIDE){
				state=State.AFTER;
			}else{
				if(PVector.dist(end,camera)<=radius){
					insideTime=p.millis();
					insideInterval=frame.coordinatesOf(camera);
					insideWorld=camera;
					state=State.INSIDE;
				}
				//Take some points of the camera
				if(p.millis()%20==0){
					cameraPoints.add(camera);
				}
			}
		}else{
			color=new Color(170,170,170);
		}
		p.pushMatrix();
		p.pushStyle();
			p.stroke(color.getRGB());
			p.line(ini.x,ini.y,ini.z,end.x,end.y,end.z);
			frame.applyTransformation();
			p.stroke(color.getRGB());
			p.noFill();
			p.sphere(radius);
		p.popStyle();
		p.popMatrix();
	}
	public void reinit(){
		state=State.BEFORE;
		insideInterval=new PVector();
		insideWorld=new PVector();
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
