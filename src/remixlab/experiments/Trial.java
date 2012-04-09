/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Trial Class
 * */
package remixlab.experiments;

import java.awt.Color;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import remixlab.experiments.Experiment.State;

public class Trial {
	PApplet p;
	Sketch sketch;
	int id;			//Identifier to the trial
	State state;	//State of the trial
	ArrayList<Interval> intervals = new ArrayList<Interval>();
	Interval start;	//First interval, to start the movement
	int currentInterval;
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	public Trial(PApplet parent,Sketch s,int _id){
		p = parent;
		sketch = s;
		id=_id;
		//Set the starting state
		state=State.BEFORE;
		start=new Interval(p,sketch,new PVector(10,10,150),new PVector(150,150,150),0,40,0,new Color(0,255,0));
	}
	/////////////////////////////////////// GET AND SET ///////////////////////////////////////
	/**
	 * Return the state of the trial
	 * */
	public State state(){
		return state;
	}
	/////////////////////////////////////// METHODS ///////////////////////////////////////
	public void init(){
		state=State.RUNNING;
		for(int i=0;i<intervals.size();i++){
			intervals.get(i).init();
		}
	}
	public void draw(PVector camera){
		if(intervals.get(intervals.size()-1).state()!=State.AFTER){
			start.draw(camera);
			for(int i=0;i<intervals.size();i++){
				if(intervals.get(i).state()!=State.AFTER){
					intervals.get(i).draw(camera);
				}
			}
		}else{
			state=State.AFTER;
		}
	}
	public void reinit(){
		state=State.BEFORE;
		for(int i=0;i<intervals.size();i++){
			intervals.get(i).reinit();
		}
		start=new Interval(p,sketch,new PVector(10,10,150),new PVector(150,150,150),0,40,0,new Color(0,255,0));
	}
	public void addInterval(Interval interval){
		intervals.add(interval);
	}
}
