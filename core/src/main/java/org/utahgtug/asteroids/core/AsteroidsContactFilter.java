package org.utahgtug.asteroids.core;

import java.util.HashMap;

import org.jbox2d.callbacks.ContactFilter;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import org.utahgtug.asteroids.core.entities.Laser;
import org.utahgtug.asteroids.core.entities.PhysicsEntity;
import org.utahgtug.asteroids.core.entities.Ship;

public class AsteroidsContactFilter extends ContactFilter {

	private HashMap<Body, PhysicsEntity> bodyEntityLUT = null;	
		
	public AsteroidsContactFilter(HashMap<Body, PhysicsEntity> bodyEntityLookupTable) {
		super();
		bodyEntityLUT = bodyEntityLookupTable;
	}

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		PhysicsEntity entityA = bodyEntityLUT.get(fixtureA.getBody());
		PhysicsEntity entityB = bodyEntityLUT.get(fixtureB.getBody());
		
		if((entityA instanceof Ship || entityA instanceof Laser) && (entityB instanceof Ship || entityB instanceof Laser)){
			return false;
		}
		
		return super.shouldCollide(fixtureA, fixtureB);
	}

}
