/**
 * Copyright 2011 The ForPlay Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.utahgtug.asteroids.core;

import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import org.utahgtug.asteroids.core.entities.Entity;
import org.utahgtug.asteroids.core.entities.PhysicsEntity;
import org.utahgtug.asteroids.core.entities.Wall;
import playn.core.CanvasLayer;
import playn.core.DebugDrawBox2D;
import playn.core.GroupLayer;

public class AsteroidsWorld implements ContactListener {
	public GroupLayer staticLayerBack;
	public GroupLayer dynamicLayer;
	public GroupLayer staticLayerFront;

	// size of world
	//private static int width = 24;
	//private static int height = 18;

	// box2d object containing physics world
	protected World world;

	private List<Entity> entities = new ArrayList<Entity>(0);
	private HashMap<Body, PhysicsEntity> bodyEntityLUT = new HashMap<Body, PhysicsEntity>();
	private Stack<Contact> contacts = new Stack<Contact>();

	private static boolean showDebugDraw = false;
	private DebugDrawBox2D debugDraw;

	public AsteroidsWorld(GroupLayer scaledLayer) {
		staticLayerBack = graphics().createGroupLayer();
		scaledLayer.add(staticLayerBack);
		dynamicLayer = graphics().createGroupLayer();
		scaledLayer.add(dynamicLayer);
		staticLayerFront = graphics().createGroupLayer();
		scaledLayer.add(staticLayerFront);

		// create the physics world
		Vec2 gravity = new Vec2(0.0f, 0.0f);
		world = new World(gravity, true);
		world.setWarmStarting(true);
		world.setAutoClearForces(true);
		world.setContactFilter(new AsteroidsContactFilter(bodyEntityLUT));
		world.setContactListener(this);
		
		float width = graphics().screenWidth()/scaledLayer.transform().scaleX();
		float height = graphics().screenHeight()/scaledLayer.transform().scaleY();

		// create the ground
		add((PhysicsEntity)new Wall(this, this.world, new Vec2(0, height), new Vec2(width, height), null, new Float(1)));

		// create the ceiling
		add((PhysicsEntity)new Wall(this, this.world, new Vec2(0, 0), new Vec2(width, 0), null, new Float(height-1)));

		// create the walls
		add((PhysicsEntity)new Wall(this, this.world, new Vec2(0, 0), new Vec2(0, height), new Float(width-1), null));
		add((PhysicsEntity)new Wall(this, this.world, new Vec2(width, 0), new Vec2(width, height), new Float(1), null));

		if (showDebugDraw) {
			CanvasLayer canvasLayer = graphics().createCanvasLayer(
					(int) (width / Asteroids.physUnitPerScreenUnit),
					(int) (height / Asteroids.physUnitPerScreenUnit));
			graphics().rootLayer().add(canvasLayer);
			debugDraw = new DebugDrawBox2D();
			debugDraw.setCanvas(canvasLayer);
			debugDraw.setFlipY(false);
			debugDraw.setStrokeAlpha(150);
			debugDraw.setFillAlpha(75);
			debugDraw.setStrokeWidth(2.0f);
			debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_jointBit
					| DebugDraw.e_aabbBit);
			debugDraw.setCamera(0, 0, 1f / Asteroids.physUnitPerScreenUnit);
			world.setDebugDraw(debugDraw);
		}
	}

	public void update(float delta) {
		Iterator<Entity> entIt = entities.iterator();
		while(entIt.hasNext()) {
			Entity e = entIt.next();
			if(e.update(delta)) entIt.remove();
		}
		// the step delta is fixed so box2d isn't affected by framerate
		world.step(0.033f, 10, 10);
		processContacts();
	}

	public void paint(float delta) {
		if (showDebugDraw) {
			debugDraw.getCanvas().canvas().clear();
			world.drawDebugData();
		}
		for (Entity e : entities) {
			e.paint(delta);
		}
	}

	public void add(Entity entity) {
		entities.add(entity);
		if (entity instanceof PhysicsEntity) {
			add((PhysicsEntity) entity);
			// PhysicsEntity physicsEntity = (PhysicsEntity) entity;
			// bodyEntityLUT.put(physicsEntity.getBody(), physicsEntity);
		}
	}

	public void add(PhysicsEntity entity) {
		PhysicsEntity physicsEntity = (PhysicsEntity) entity;
		bodyEntityLUT.put(physicsEntity.getBody(), physicsEntity);
	}

	public void remove(Entity entity) {
		entities.remove(entity);
		if (entity instanceof PhysicsEntity) {
			remove((PhysicsEntity) entity);
			// PhysicsEntity physicsEntity = (PhysicsEntity) entity;
			// bodyEntityLUT.put(physicsEntity.getBody(), physicsEntity);
		}
	}

	public void remove(PhysicsEntity entity) {
		PhysicsEntity physicsEntity = (PhysicsEntity) entity;
		bodyEntityLUT.remove(physicsEntity.getBody());
	}

	// handle contacts out of physics loop
	public void processContacts() {
		while (!contacts.isEmpty()) {
			Contact contact = contacts.pop();

			// handle collision
			PhysicsEntity entityA = bodyEntityLUT
					.get(contact.m_fixtureA.m_body);
			PhysicsEntity entityB = bodyEntityLUT
					.get(contact.m_fixtureB.m_body);

			if (entityA != null && entityB != null) {
				if (entityA instanceof PhysicsEntity.HasContactListener) {
					((PhysicsEntity.HasContactListener) entityA)
							.contact(entityB);
				}
				if (entityB instanceof PhysicsEntity.HasContactListener) {
					((PhysicsEntity.HasContactListener) entityB)
							.contact(entityA);
				}
			}
		}
	}

	// Box2d's begin contact
	@Override
	public void beginContact(Contact contact) {
		contacts.push(contact);
	}

	// Box2d's end contact
	@Override
	public void endContact(Contact contact) {
	}

	// Box2d's pre solve
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	// Box2d's post solve
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
	
	public World getWorld ( ) {
		return world;
	}
}
