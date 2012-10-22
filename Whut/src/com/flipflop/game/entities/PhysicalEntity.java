package com.flipflop.game.entities;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2d;

import com.flipflop.game.world.World;

public abstract class PhysicalEntity extends BaseEntity {
	protected double mass = 1.D;
	protected Vector2d velocity = new Vector2d();
	protected List<Vector2d> forces = new ArrayList<Vector2d>();

	public PhysicalEntity(World world) {
		super(world);
		super.width = 1;
		super.height = 1;
	}

	public Vector2d collideWith(PhysicalEntity doucheBag) {
		return null;
	}

	public List<Vector2d> getForces() {
		return this.forces;
	}

	public void addForce(Vector2d force) {
		this.forces.add(force);
	}

	@Override
	public void update(long tm) {
		super.update(tm);
		if (tm > 0) {
			double ts = (double) tm / 1000; // Time in seconds.
			double tc = 0.5 * (Math.pow(ts, 2)); // Time constant for
													// multiplication efficiency
			Vector2d di = new Vector2d(); // Initial displacement.
			di.scale(ts, this.velocity);

			// Will represent the final distance
			Vector2d displacement = new Vector2d();
			// Used to determine final velocity
			Vector2d finalVelo = new Vector2d();
			
			// Add up all the accelerations
			for (Vector2d force : this.forces) {
				displacement.scaleAdd(1.D / this.mass, force);
			}
			// At this point displacement vector is acceleration vector...
			finalVelo.set(displacement);
			// Then get final velocity and add initial velocity.
			finalVelo.scaleAdd(ts, this.velocity);
			// And save it.
			this.velocity.set(finalVelo);
			// Multiply by 1/2 * t^2 to get displacement
			displacement.scale(tc);
			// Add initial displacement due to original velocity
			displacement.add(di);
			// Update velocity with new velocity.
			this.moveTo((int) (displacement.x + this.xPos),
					(int) (displacement.y + this.yPos));
		}
	}
}
