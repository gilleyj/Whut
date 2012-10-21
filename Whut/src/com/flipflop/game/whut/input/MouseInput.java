package com.flipflop.game.whut.input;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;
import javax.vecmath.Vector2d;

import com.flipflop.collections.Shelf;
import com.flipflop.game.whut.input.MouseInput.MouseInfo.PointInfo;

/**
 * This class encompasses all information associated with the mouse, along with
 * some additional analyses and historical tracking of movement and button
 * actions.
 * 
 * @author joma
 * 
 */
public class MouseInput implements InputDevice, MouseListener,
		MouseMotionListener {
	public enum MouseState {
		LEFT_CLICKING, RIGHT_CLICKING, MIDDLE_CLICKING, LEFT_RIGHT_CLICKING, RELEASED
	}

	protected class MouseInfo {
		public static final int MOUSE_POINT_HISTORY_SIZE = 10;
		public static final int MOUSE_CLICK_HISTORY_SIZE = 10;

		protected class PointInfo {
			public Point point;
			public long since;

			public PointInfo(Point point, long time) {
				this.point = point;
				this.since = time;
			}
		}

		protected class ClickInfo {
			public MouseState state;
			public long since;

			public ClickInfo(MouseState state, long time) {
				this.state = state;
				this.since = time;
			}
		}

		// This variable is updated synchronously, and has no danger when
		// iterating.
		protected Shelf<PointInfo> pointHistory;

		// This variable is NOT updated synchronously, and has enormous danger
		// when iterating. ALWAYS syncrhonize.
		protected Shelf<ClickInfo> clickHistory;

		public MouseInfo() {
			this.clickHistory = new Shelf<ClickInfo>(MOUSE_CLICK_HISTORY_SIZE);
			this.pointHistory = new Shelf<PointInfo>(MOUSE_POINT_HISTORY_SIZE);
		}

		public Point getLatestPoint() {
			if (this.pointHistory.size() > 0)
				return this.pointHistory.peekFirst().point;
			else
				return java.awt.MouseInfo.getPointerInfo().getLocation();
		}

		public long getLatestPointTime() {
			if (this.pointHistory.size() > 0)
				return this.pointHistory.peekFirst().since;
			else
				return 0;
		}

		public MouseState getLatestState() {
			synchronized (this.clickHistory) {
				if (this.clickHistory.size() > 0)
					return this.clickHistory.peekFirst().state;
				else
					return MouseState.RELEASED;
			}
		}

		public long getLatestStateTime() {
			synchronized (this.clickHistory) {
				if (this.clickHistory.size() > 0)
					return this.clickHistory.peekFirst().since;
				else
					return 0;
			}
		}

		public void addPointInfo(Point point, long time) {
			this.pointHistory.addFirst(new PointInfo(point, time));
		}

		public void addClickInfo(MouseState state, long time) {
			synchronized (this.clickHistory) {
				this.clickHistory.addFirst(new ClickInfo(state, time));
			}
		}
	}

	protected MouseInfo mouseInfo = new MouseInfo();
	protected Boolean mouseOwned = new Boolean(true);
	protected InputManager im = null;

	public MouseInput(InputManager im) {
		this.im = im;
	}

	@Override
	public void poll() {
		if (this.isMouseOwned()) {
			Point point = java.awt.MouseInfo.getPointerInfo().getLocation();
			SwingUtilities.convertPointFromScreen(point, this.im.watchable);
			this.mouseInfo.addPointInfo(point, System.currentTimeMillis());
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// Srsly, stop it.
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// this.mouseInfo.addPointInfo(e.getPoint(),
		// System.currentTimeMillis());
	}

	public Vector2d getAverageMouseVector() {
		Vector2d avgVec = new Vector2d();
		Vector2d lastVec = null;
		Vector2d currVec = null;
		long lastTime = 0;

		for (PointInfo movement : this.mouseInfo.pointHistory) {
			if (lastVec == null) {
				PointInfo point = this.mouseInfo.pointHistory.peekLast();
				lastVec = new Vector2d(point.point.x, point.point.y);
				lastTime = point.since;
			}
			currVec = new Vector2d(movement.point.x, movement.point.y);
			Vector2d normVec = new Vector2d();
			normVec.sub(currVec, lastVec);
			normVec.scale((movement.since - lastTime)/100);
			avgVec.add(normVec);
			lastVec = currVec;
		}
		return avgVec;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// OMG, NO!!
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (this.mouseInfo.getLatestState() == MouseState.RIGHT_CLICKING) {
				this.mouseInfo.addClickInfo(MouseState.LEFT_RIGHT_CLICKING,
						System.currentTimeMillis());
			} else {
				this.mouseInfo.addClickInfo(MouseState.LEFT_CLICKING,
						System.currentTimeMillis());
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			if (this.mouseInfo.getLatestState() == MouseState.LEFT_CLICKING) {
				this.mouseInfo.addClickInfo(MouseState.LEFT_RIGHT_CLICKING,
						System.currentTimeMillis());
			} else {
				this.mouseInfo.addClickInfo(MouseState.RIGHT_CLICKING,
						System.currentTimeMillis());
			}
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			this.mouseInfo.addClickInfo(MouseState.MIDDLE_CLICKING,
					System.currentTimeMillis());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (this.mouseInfo.getLatestState() == MouseState.LEFT_RIGHT_CLICKING) {
				this.mouseInfo.addClickInfo(MouseState.RIGHT_CLICKING,
						System.currentTimeMillis());
			} else {
				this.mouseInfo.addClickInfo(MouseState.RELEASED,
						System.currentTimeMillis());
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			if (this.mouseInfo.getLatestState() == MouseState.LEFT_RIGHT_CLICKING) {
				this.mouseInfo.addClickInfo(MouseState.LEFT_CLICKING,
						System.currentTimeMillis());
			} else {
				this.mouseInfo.addClickInfo(MouseState.RELEASED,
						System.currentTimeMillis());
			}
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			this.mouseInfo.addClickInfo(MouseState.RELEASED,
					System.currentTimeMillis());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		synchronized (this.mouseOwned) {
			this.mouseOwned = true;
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		synchronized (this.mouseOwned) {
			this.mouseOwned = false;
		}
	}

	public boolean isMouseOwned() {
		synchronized (this.mouseOwned) {
			return this.mouseOwned;
		}
	}

	/**
	 * Return the state of the mouse in a readable String
	 * 
	 * @return The current state of the mouse
	 */
	public String mouseStatus() {
		String mouseState = "";
		switch (this.mouseInfo.getLatestState()) {
		case LEFT_CLICKING:
			mouseState = "LEFT DOWN";
			break;
		case LEFT_RIGHT_CLICKING:
			mouseState = "LEFT AND RIGHT DOWN";
			break;
		case MIDDLE_CLICKING:
			mouseState = "MIDDLE DOWN";
			break;
		case RELEASED:
			mouseState = "NOTHING DOWN";
			break;
		case RIGHT_CLICKING:
			mouseState = "RIGHT DOWN";
			break;
		}
		return mouseState;
	}

}
