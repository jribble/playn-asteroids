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

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import org.utahgtug.asteroids.core.AsteroidsWorld;

public class Wall implements PhysicsEntity, PhysicsEntity.HasContactListener {
	public static String TYPE = "Portal";
	
	Float contactX = null;
	Float contactY = null;
	
	private Body body = null;

	public Wall(AsteroidsWorld asteroidsWorld, World world, Vec2 edgePt1, Vec2 edgePt2, Float contactX, Float contactY) {
		body = world.createBody(new BodyDef());
		PolygonShape groundShape = new PolygonShape();
		groundShape.setAsEdge(edgePt1, edgePt2);
		body.createFixture(groundShape, 0.0f);
		this.contactX = contactX;
		this.contactY = contactY;
	}

	// Handle portal event
	@Override
	public void contact(PhysicsEntity contactEntity) {
		Vec2 pos = contactEntity.getBody().getPosition();
		if ( contactX != null ){
			pos.x = contactX.floatValue();
		}
		if ( contactY != null ){
			pos.y = contactY.floatValue();
		}
		contactEntity.getBody().setTransform(pos, contactEntity.getBody().getAngle());
	}

	@Override
	public Body getBody() {
		return body;
	}
}
