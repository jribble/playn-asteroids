package org.utahgtug.asteroids.android;

import org.utahgtug.asteroids.core.HasShipControl;
import org.utahgtug.asteroids.core.entities.ShipControl;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class AccelerometerControl implements SensorEventListener, HasShipControl {

	private ShipControl listener;
	
	@Override
	public void setShipControl(ShipControl listener){
		this.listener = listener;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		//float z = event.values[2];
		
		if(listener != null){
			if(x>1.5f){
				listener.setAngularVelocity(x-1.5f);
			}
			else if(x<-1.5f){
				listener.setAngularVelocity(x+1.5f);
			}
			else{
				listener.setAngularVelocity(0f);
			}
			if(y>0f){
				listener.accelerate(y/2f);
			}
		}
		
	}
	
	
}
