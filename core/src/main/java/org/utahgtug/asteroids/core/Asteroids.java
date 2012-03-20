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

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;
import org.utahgtug.asteroids.core.entities.Asteroid;
import org.utahgtug.asteroids.core.entities.Entity;
import org.utahgtug.asteroids.core.entities.Ship;

import playn.core.PlayN;
import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import playn.core.ResourceCallback;

public class Asteroids implements Game {

	// scale difference between screen space (pixels) and world space (physics).
	public static float physUnitPerScreenUnit = 1 / 26.666667f;

	// main layer that holds the world. note: this gets scaled to world space
	GroupLayer worldLayer;

	// main world
	AsteroidsWorld world = null;
	boolean worldLoaded = false;
	HasShipControl controlAdapter = null;
	
	public void setControlAdapter(HasShipControl adapter){
		controlAdapter = adapter;
	}

	@Override
	public void init() {
		// load and show our background image
		Image bgImage = assets().getImage("images/black.png");
		ImageLayer bgLayer = graphics().createImageLayer(bgImage);
		//CanvasLayer bgLayer = graphics().createCanvasLayer(48, 36);
		//bgLayer.canvas().setFillColor(0);
		//bgLayer.canvas().fillRect(0, 0, 48, 36);
		graphics().rootLayer().add(bgLayer);

		// create our world layer (scaled to "world space")
		worldLayer = graphics().createGroupLayer();
		worldLayer.setScale(1f / physUnitPerScreenUnit);
		graphics().rootLayer().add(worldLayer);
		
		AsteroidsLoader.CreateWorld(1, worldLayer,
				new ResourceCallback<AsteroidsWorld>() {
					@Override
					public void done(AsteroidsWorld resource) {
						world = resource;
						worldLoaded = true;
						Ship ship = new Ship(world, world.world, graphics().screenWidth() * physUnitPerScreenUnit/2f, graphics().screenHeight() * physUnitPerScreenUnit/2f, 0);
						if(controlAdapter != null){
							controlAdapter.setShipControl(ship);
						}
						else{
							KeyboardControl keyboardControl = new KeyboardControl();
							keyboardControl.setShipControl(ship);
							keyboard().setListener(keyboardControl);
						}
						world.add((Entity) ship);
					}

					@Override
					public void error(Throwable err) {
						PlayN.log().error("Error loading pea world: " + err.getMessage());
					}
				});

		// hook up our pointer listener
		pointer().setListener(new Pointer.Adapter() {
			@Override
			public void onPointerStart(Pointer.Event e) {
				if (worldLoaded) {
					Asteroid asteroid = new Asteroid(world, world.world, physUnitPerScreenUnit * e.x(), physUnitPerScreenUnit * e.y(), 0);
					world.add((Entity) asteroid);
					asteroid.getBody().applyAngularImpulse(.3f);
					asteroid.accelerate(1);
				}
			}
		});
	}

	@Override
	public void paint(float alpha) {
		if (worldLoaded) {
			world.paint(alpha);
		}
	}

	@Override
	public void update(float delta) {
		if (worldLoaded) {
			world.update(delta);
		}
	}

	@Override
	public int updateRate() {
		return 25;
	}

}
