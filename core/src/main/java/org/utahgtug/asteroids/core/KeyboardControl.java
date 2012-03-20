package org.utahgtug.asteroids.core;

import org.utahgtug.asteroids.core.entities.ShipControl;

import playn.core.Keyboard.Event;
import playn.core.Keyboard.Listener;
import playn.core.Keyboard.TypedEvent;

public class KeyboardControl implements HasShipControl, Listener {
	private ShipControl ship;

	@Override
	public void onKeyDown(Event event) {
		switch(event.key()){
		case LEFT:
			ship.setAngularVelocity(-10);
			break;
		case RIGHT:
			ship.setAngularVelocity(10);
			break;
		case UP:
			// accelerate in direction
			ship.accelerate(1);
			break;
		case DOWN:
			// accelerate in direction
			//accelerate(-1);
			break;
		case SPACE:
			ship.fire();
		}
	}

	@Override
	public void onKeyUp(Event event) {
		switch(event.key()){
		case LEFT:
		case RIGHT:
			ship.setAngularVelocity(0);
			break;
		case UP:
			// accelerate in direction
			ship.accelerate(0);
			break;
		}
	}

	@Override
	public void setShipControl(ShipControl control) {
		this.ship = control;
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
		// TODO Auto-generated method stub
		
	}

}
