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
package org.utahgtug.asteroids.core.entities;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import org.utahgtug.asteroids.core.AsteroidsWorld;

public class Laser extends DynamicPhysicsEntity implements PhysicsEntity.HasContactListener {

	public static String TYPE = "Laser";
	
	private float ttl = 600f;
	private boolean destroying = false;
	
	public Laser(AsteroidsWorld asteroidWorld, World world, float x, float y,
			float angle) {
		super(asteroidWorld, world, x, y, angle);
	}

	@Override
	Body initPhysicsBody(World world, float x, float y, float angle) {
		FixtureDef fixtureDef = new FixtureDef();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position = new Vec2(0, 0);
		Body body = world.createBody(bodyDef);

		CircleShape circleShape = new CircleShape();
		circleShape.m_radius = getRadius();
		fixtureDef.shape = circleShape;
		fixtureDef.density = 0.4f;
		fixtureDef.friction = 0.1f;
		fixtureDef.restitution = 0.35f;
		circleShape.m_p.set(0, 0);
		body.createFixture(fixtureDef);
		body.setLinearDamping(0.0f);
		body.setTransform(new Vec2(x, y), angle);
		float magnitude = 20f;
		float vy = (float) (-magnitude * Math.cos(angle));
		float vx = (float) (magnitude * Math.sin(angle));
		body.setLinearVelocity(new Vec2(vx, vy));
		body.setBullet(true);
		return body;
	}

	@Override
	float getWidth() {
		return 2 * getRadius();
	}

	@Override
	float getHeight() {
		return 2 * getRadius();
	}

	float getRadius() {
		// return 1.50f;
		return 0.05f;
	}

	@Override
	public String getImagePath() {
		// return "images/chrome.png";
		return "images/laser.png";
	}

	@Override
	public void contact(PhysicsEntity other) {
		if(other instanceof Wall || other instanceof Ship) return;
		
		destroying = true;	
	}
	
	@Override
	public boolean update(float delta) {
		super.update(delta);
		ttl = ttl - delta;
		if(ttl <= 0 || destroying) {
			getWorld().getWorld().destroyBody(getBody());
			destroyImageLayer();
			return true;
		}
		return false;
	}
}
