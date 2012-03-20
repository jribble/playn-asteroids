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

import static playn.core.PlayN.assets;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import playn.core.Image;
import playn.core.Sound;

import org.utahgtug.asteroids.core.AsteroidsWorld;

public class Ship extends DynamicPhysicsEntity implements ShipControl {
	public static String TYPE = "Ship";
	
	private AsteroidsWorld world;
	private Image shipImage;
	private Image accelImage;
	private Sound accelSound;
	private Sound fireSound;
	
	public Ship(AsteroidsWorld asteroidWorld, World world, float x, float y,
			float angle) {
		super(asteroidWorld, world, x, y, angle);
		this.world = asteroidWorld;
		shipImage = layer.image();
		accelImage = assets().getImage("images/shipaccel.png");
		accelSound = assets().getSound("audio/thrust");
		accelSound.setLooping(true);
		fireSound = assets().getSound("audio/fire");
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
		body.setLinearDamping(0.2f);
		body.setTransform(new Vec2(x, y), angle);
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
		return 0.5f;
	}
	
	Vec2 getLaserOrigin() {
		Vec2 t = polarToCartesian(getBody().getAngle(), getRadius());
		Vec2 l = new Vec2(getBody().getWorldCenter());		
		l.x = l.x + t.x;
		l.y = l.y + t.y;
		return l;
	}

	@Override
	public String getImagePath() {
		return "images/ship.png";
	}

	@Override
	public void fire() {
		Vec2 laserOrigin = getLaserOrigin();
		Laser laser = new Laser ( world, world.getWorld(), laserOrigin.x, laserOrigin.y, getBody().getAngle() );
		world.add((Entity)laser);
		fireSound.play();		
	}
	
	@Override
	public void accelerate(float magnitude) {
		super.accelerate(magnitude);
		if(magnitude == 0f){
			layer.setImage(shipImage);
			accelSound.stop();			
		}
		else{
			layer.setImage(accelImage);
			if(!accelSound.isPlaying()) accelSound.play();
		}
	}
}
