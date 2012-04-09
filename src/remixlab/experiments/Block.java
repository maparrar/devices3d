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
import processing.core.PVector;

import remixlab.experiments.Experiment.State;

public class Block {
	Sketch sketch;
	int id;			//Identifier to the block
	State state;	//State of the block
	ArrayList<Trial> trials = new ArrayList<Trial>();
	int currentTrial;//Current trial
	
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	public Block(Sketch s,int _id){
		sketch = s;
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
	public void init(){
		state=State.RUNNING;
		currentTrial=0;
	}
	public void draw(PVector camera){
		//Show the current trial
		if(currentTrial<trials.size()){
			if(trials.get(currentTrial).state()==State.BEFORE){
				trials.get(currentTrial).init();
			}else if(trials.get(currentTrial).state()==State.RUNNING){
				trials.get(currentTrial).draw(camera);
			}else{
				sketch.restartAvatar();
				currentTrial++;
			}
		}else{
			state=State.AFTER;
		}
	}
	public void reinit(){
		state=State.BEFORE;
		currentTrial=0;
		for(int i=0;i<trials.size();i++){
			trials.get(i).reinit();
		}
	}
	public void addTrial(Trial trial){
		trials.add(trial);
	}
}
