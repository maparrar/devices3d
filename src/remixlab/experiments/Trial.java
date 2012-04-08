/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Trial Class
 * */
package remixlab.experiments;

import java.util.ArrayList;
import remixlab.experiments.Experiment.State;

public class Trial {
	int id;			//Identifier to the trial
	State state;		//State of the trial
	ArrayList<Interval> intervals = new ArrayList<Interval>();
	
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	public Trial(int _id){
		id=_id;
		//Set the starting state
		state=State.BEFORE;
	}
	/////////////////////////////////////// GET AND SET ///////////////////////////////////////
	/**
	 * Return the state of the trial
	 * */
	public State state(){
		return state;
	}
	
	/////////////////////////////////////// METHODS ///////////////////////////////////////
	public void draw(){
		if(state==State.RUNNING){
			for(int i=0;i<intervals.size();i++){
				intervals.get(i).draw();
			}
		}
	}
	public void addInterval(Interval interval){
		intervals.add(interval);
	}
}
