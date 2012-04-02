package remixlab.bezier;

import java.awt.Color;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import remixlab.proscene.*;

/**
 * A BezierPart is a division of BezierCurve
 * */
public class BezierPart {
	PApplet parent;
	Scene scene;
	InteractiveFrame frame;	// Frame using to draw the part
	PVector ini;			// Initial vector of the part
	PVector fin;			// Final vector of the part
	PVector normalIni;		// Normal vector to a plane between last part and this
	PVector normalFin;		// Normal vector to a plane between this part and next, this vector is defined with the next part
	float length;			// Length of part
	Color color;			// Color of the part
	int detail;				// Detail of the cylinder
	float radius;				// Radius of the cylinder
	
	/**
	 * BezierPart constructor
	 * 	@param vIni: Initial vector of the part
	 *  @param vFin: Final vector of the part
	 *  @param vNormalIni: Normal vector to a plane between last part and this
	 * */
	public BezierPart(PApplet p,Scene sc,PVector vIni,PVector vFin,Color col,int det,float radi){
		parent = p;
		scene = sc;
		frame = new InteractiveFrame(scene);
		frame.removeFromMouseGrabberPool();
		ini=vIni;
		fin=vFin;
		normalIni=ini;
		normalFin=new PVector(0,0,0);
		length=PVector.dist(ini, fin);
		color=col;
		detail=det;
		radius=radi;
		configureFrame();
	}
	
	/**
	 * Configure the frame with the data of the part
	 * */
	private void configureFrame(){
		frame.setPosition(ini);
		PVector to = PVector.sub(fin, frame.position());
		frame.setOrientation(new Quaternion(new PVector(0, 0, 1), to));
	}
	
	/**
	 * Set the normalIni of this part and the normalFin of the parameter part
	 * */
	private void setNormals(BezierPart nextPart){
		//z-vector of this part
		PVector zThis=new PVector(0,0,1);
		//Get z-next vector
		PVector zNext=frame.transformOfFrom(new PVector(0,0,1),nextPart.frame);
		//Sum of both vectors: medium vector in the plane of both vectors
		PVector normal=PVector.add(zThis,zNext);
		//Set the normalIni of the next part and the normalFin of this part
		nextPart.normalIni=nextPart.frame.transformOfFrom(normal,frame);
		normalFin=normal;
	}
	
	/**
	 * Draw the part
	 * @param nextPart to calculate the normal between them
	 * */
	public void draw(BezierPart nextPart){
		parent.pushMatrix();
			parent.pushStyle();
				frame.applyTransformation();
				//scene.drawAxis(10*1.3f);
				//Set the normal between this part and nextPart
				setNormals(nextPart);	
				//Draw the cylinder
				parent.fill(color.getRGB());
				weirdCylinder(detail,radius,length,normalIni,normalFin);
			parent.popStyle();
		parent.popMatrix();
	}
	
	/**
	 * Draw a cylinder cut by the planes given by the vector normal to them.
	 * @author Jean Pierre Charalambos:
	 * @param w is the radius of the cylinder and
	 * @param h is its height.
	 * @param n is the normal of the plane that intersects the cylinder at z=0
	 * @param m is the normal of the plane that intersects the cylinder at z=h
	 * @info Took from: http://en.wikipedia.org/wiki/Line-plane_intersection
	 * */
	private void weirdCylinder(int detail, float w, float h, PVector n, PVector m) {
		PVector Pn0 = new PVector(0, 0, 0);
		PVector l0 = new PVector();
		PVector Pm0 = new PVector(0, 0, h);
		PVector l = new PVector(0, 0, 1);
		PVector pn = new PVector();
		PVector pm = new PVector();
		float x, y, d;

		parent.pushStyle();
		parent.noStroke();
		parent.beginShape(PConstants.QUAD_STRIP);
		for (float t = 0; t <= detail; t++) {
			x = (float) (w * Math.cos(t * (Math.PI*Math.PI) / detail));
			y = (float) (w * Math.sin(t * (Math.PI*Math.PI) / detail));
			l0.set(x, y, 0);

			d = (n.dot(PVector.sub(Pn0, l0))) / (l.dot(n));
			pn = PVector.add(PVector.mult(l, d), l0);
			parent.vertex(pn.x, pn.y, pn.z);

			l0.z = h;
			d = (m.dot(PVector.sub(Pm0, l0))) / (l.dot(m));
			pm = PVector.add(PVector.mult(l, d), l0);
			parent.vertex(pm.x, pm.y, pm.z);
		}
		parent.endShape();
		parent.popStyle();
	}
	
	/**
	 * Return a string with data of the curve
	 * */
	public String toString(){
		String pa="length: "+length+"\n";
		String in="normalIni:("+normalIni.x+","+normalIni.y+","+normalIni.z+")\n";
		String fi="normalFin:("+normalFin.x+","+normalFin.y+","+normalFin.z+")\n";
		return "*****\n"+pa+in+fi+"*****";
	}
}
