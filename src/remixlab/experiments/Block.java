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

public class Block {
	int id;			//Identifier to the block
	State state;		//State of the block
	ArrayList<Trial> trials = new ArrayList<Trial>();
	
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	public Block(int _id){
		id=_id;
		//Set the starting state
		state=State.BEFORE;
	}
	/////////////////////////////////////// GET AND SET ///////////////////////////////////////
	/**
	 * Return the state of the block
	 * */
	public State state(){
		return state;
	}
	/////////////////////////////////////// METHODS ///////////////////////////////////////
	public void draw(){
		if(state==State.RUNNING){
			for(int i=0;i<trials.size();i++){
				trials.get(i).draw();
			}
		}
	}
	public void addTrial(Trial trial){
		trials.add(trial);
	}
}
