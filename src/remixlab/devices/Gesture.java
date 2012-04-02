/**
 * devices3d - Library to test devices in 3D (https://github.com/maparrar/devices3d)
 * National University of Colombia - Remixlab
 * @author Miguel Alejandro Parra Romero [maparrar@unal.edu.co]
 * @author Jean Pierre Charalambos [jpcharalambosh@unal.edu.co]
 * 
 * Gesture Class, by now for two hands
 * */
package remixlab.devices;

import processing.core.PVector;

public class Gesture {
	PVector left;		//Left hand position
	PVector right;		//Right hand position
	
	public Gesture(){
		left=new PVector(0,0,0);
		right=new PVector(0,0,0);
	}
	
	
	
	
	
	
//	// 1. Almacenar los últimos n puntos de la mano
//			// 2. Hacer una operación para determinar el vector de cambio
//			// El vector de cambio es un vector tridimensional que apunta hacia
//			// donde se hizo el movimiento
//			// la longitud del vector representa la velocidad de cambio
//			// 3. Se unen los dos vectores de cambio que aparecen en la pantalla
//			// 4. Se comparan los valores de los vectores de cambio con algunos
//			// almacenados
//			// 5. Si corresponden a alguno en particular, se ejecuta el gesto
//
//			// println(colorIndex+":: "+firstVec);
//
//			// PRUEBA DE GESTOS
//			// Mover hacia el frente
//			if (Math.abs(auxiliar.z - handPoint.z) > 50) {
//				PApplet.println("hand: " + handId + " -> click");
//			}
//			// Mover a la derecha
//			if ((handPoint.x - auxiliar.x) > 50) {
//				PApplet.println("hand: " + handId + " -> derecha");
//			}
//			// Mover hacia arriba
//			if ((handPoint.y - auxiliar.y) > 50) {
//				PApplet.println("hand: " + handId + " -> arriba");
//			}
//
//			auxiliar = handPoint;
}
