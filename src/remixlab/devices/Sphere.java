package remixlab.devices;

import java.awt.Color;

import processing.core.PVector;

public class Sphere {
	float radius;
	PVector center;
	Color color;

	public Sphere() {
		radius=10;
		center=new PVector(0,0,0);
		color=new Color(200,100,0);
	}
	public Sphere(float rad,PVector cen,Color col) {
		radius=rad;
		center=cen;
		color=col;
	}
	public float getRadius(){
		return radius;
	}
	public PVector getCenter(){
		return center;
	}
	public Color getColor(){
		return color;
	}
}