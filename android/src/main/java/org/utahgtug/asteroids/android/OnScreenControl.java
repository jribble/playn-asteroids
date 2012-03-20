package org.utahgtug.asteroids.android;

import org.utahgtug.asteroids.core.HasShipControl;
import org.utahgtug.asteroids.core.entities.ShipControl;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class OnScreenControl implements HasShipControl {
	private ShipControl ship;
	private Button leftButton;
	private Button rightButton;
	private Button accelButton;
	private Button fireButton;
	
	public OnScreenControl(Activity activity) {        
        Context context = activity.getApplicationContext();
        LinearLayout lll = new LinearLayout(context);
        leftButton = new Button(context);
        leftButton.setWidth(100);
        leftButton.setHeight(100);
        leftButton.setText("Left");
        lll.addView(leftButton);
        leftButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					ship.setAngularVelocity(-10);
					break;
				case MotionEvent.ACTION_UP:
					ship.setAngularVelocity(0);
					break;
				}
				return true;
			}
        	
        });
        rightButton = new Button(context);
        rightButton.setText("Right");
        rightButton.setWidth(100);
        rightButton.setHeight(100);
        lll.addView(rightButton);
        rightButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					ship.setAngularVelocity(10);
					break;
				case MotionEvent.ACTION_UP:
					ship.setAngularVelocity(0);
					break;
				}
				return true;
			}
        	
        });
        lll.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        activity.addContentView(lll, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        

        LinearLayout rll = new LinearLayout(context);
        fireButton = new Button(context);
        fireButton.setText("Fire");
        fireButton.setWidth(100);
        fireButton.setHeight(100);
        rll.addView(fireButton);
        fireButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					ship.fire();
					break;
				}
				return true;
			}
        	
        });
        accelButton = new Button(context);
        accelButton.setText("Go!");
        accelButton.setWidth(100);
        accelButton.setHeight(100);
        rll.addView(accelButton);
        accelButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					ship.accelerate(1);
					break;
				case MotionEvent.ACTION_UP:
					ship.accelerate(0);
					break;
				}
				return true;
			}
        	
        });
        rll.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        activity.addContentView(rll, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	@Override
	public void setShipControl(ShipControl control) {
		this.ship = control;
	}

}
