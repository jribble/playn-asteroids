package org.utahgtug.asteroids.core.entities;

public interface ShipControl {
	public void setAngularVelocity(float velocity);
	
	public void accelerate(float magnitude);
	
	public void fire();
}
