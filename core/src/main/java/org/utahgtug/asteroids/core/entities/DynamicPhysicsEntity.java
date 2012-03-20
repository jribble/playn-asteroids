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

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import org.utahgtug.asteroids.core.AsteroidsWorld;

public abstract class DynamicPhysicsEntity extends Entity implements
		PhysicsEntity {
	// for calculating interpolation
	private float prevX, prevY, prevA;
	private Body body;
	private AsteroidsWorld world;

	public DynamicPhysicsEntity(AsteroidsWorld asteroidWorld, World world, float x,
			float y, float angle) {
		super(asteroidWorld, x, y, angle);
		this.world = asteroidWorld;
		body = initPhysicsBody(world, x, y, angle);
		setPos(x, y);
		setAngle(angle);
	}

	abstract Body initPhysicsBody(World world, float x, float y, float angle);

	@Override
	public void paint(float alpha) {
		// interpolate based on previous state
		float x = (body.getPosition().x * alpha) + (prevX * (1f - alpha));
		float y = (body.getPosition().y * alpha) + (prevY * (1f - alpha));
		float a = (body.getAngle() * alpha) + (prevA * (1f - alpha));
		layer.setTranslation(x, y);
		layer.setRotation(a);
	}

	@Override
	public boolean update(float delta) {
		// store state for interpolation in paint()
		prevX = body.getPosition().x;
		prevY = body.getPosition().y;
		prevA = body.getAngle();
		return false;
	}

	public void initPreLoad(final AsteroidsWorld peaWorld) {
		// attach our layer to the dynamic layer
		peaWorld.dynamicLayer.add(layer);
	}

	public void initPostLoad(final AsteroidsWorld peaWorld) {
	}

	public void setLinearVelocity(float x, float y) {
		body.setLinearVelocity(new Vec2(x, y));
	}

	public void setAngularVelocity(float w) {
		body.setAngularVelocity(w);
	}
	
	public Vec2 polarToCartesian(float angle, float magnitude)
	{
		float y = (float) (-magnitude * Math.cos(angle));
		float x = (float) (magnitude * Math.sin(angle));
		return new Vec2(x, y);		
	}

	public void accelerate(float magnitude) {
		float angle = body.getAngle();
		float y = (float) (-magnitude * Math.cos(angle));
		float x = (float) (magnitude * Math.sin(angle));
		body.applyLinearImpulse(new Vec2 (x, y), body.getWorldCenter());
	}

	@Override
	public void setPos(float x, float y) {
		super.setPos(x, y);
		getBody().setTransform(new Vec2(x, y), getBody().getAngle());
		prevX = x;
		prevY = y;
	}

	@Override
	public void setAngle(float a) {
		super.setAngle(a);
		getBody().setTransform(getBody().getPosition(), a);
		prevA = a;
	}

	@Override
	public Body getBody() {
		return body;
	}
	
	public AsteroidsWorld getWorld() {
		return world;
	}
}
