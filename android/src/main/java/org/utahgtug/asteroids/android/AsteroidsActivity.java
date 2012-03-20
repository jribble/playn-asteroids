package org.utahgtug.asteroids.android;

import org.utahgtug.asteroids.core.Asteroids;

import playn.android.GameActivity;
import playn.core.PlayN;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

public class AsteroidsActivity extends GameActivity {
	//private AccelerometerControl control = new AccelerometerControl();
	private SensorManager sensorManager;
	private Sensor sensor;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        platform().assets().setPathPrefix("org/utahgtug/asteroids/resources");

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		
        Asteroids asteroids = new Asteroids();
        OnScreenControl control = new OnScreenControl(this);
        asteroids.setControlAdapter(control);
        PlayN.run(asteroids);
        
        
    }
 
	@Override
	protected void onResume() {
		super.onResume();
		//sensorManager.registerListener(control, sensor, SensorManager.SENSOR_DELAY_GAME);
	}
 
	@Override
	protected void onStop() {
		//sensorManager.unregisterListener(control);
		super.onStop();
	}

	@Override
	public void main() {
		// TODO Auto-generated method stub
		
	}
}