package remixlab.experiments;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PVector;
import remixlab.proscene.Scene;

public class Trial {
	PApplet parent;
	Scene scene;
	PVector start;
	PVector end;
	
	Marker target;
	Marker[] markers;
	
	
	/////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////
	public Trial(PApplet p,Scene sc,int numMarkers){
		parent = p;
		scene = sc;
		
		
		
		
		
		//Create the markers and the target
		Color color=new Color(255,0,0);
		float radiusTarget=20;
		target=new Marker(parent,scene,new PVector(1000-radiusTarget,radiusTarget,radiusTarget),radiusTarget,color);
				
				
		markers=new Marker[numMarkers];
		float radius=20;
		float min=radius,max=1000;
		for(int i=0;i<markers.length;i++){
			Color colorM=new Color((int)parent.random(255),(int)parent.random(255),(int)parent.random(255));
			markers[i]=new Marker(parent,scene,new PVector(parent.random(min,max),parent.random(min,max),parent.random(min,100)),radius,colorM);
		}
	}
	/////////////////////////////////////// GET AND SET ///////////////////////////////////////
	/////////////////////////////////////// METHODS ///////////////////////////////////////
	
	public void draw(){
		//Draw the markers and the target
		target.draw();
		for(int i=0;i<markers.length;i++){
			markers[i].draw();
		}
	}
}
