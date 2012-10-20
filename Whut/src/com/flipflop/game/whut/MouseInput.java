package com.flipflop.game.whut;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.vecmath.Vector2d;

public class MouseInput implements MouseListener, MouseMotionListener {
	public enum MouseState {
		LEFT_CLICKING, RIGHT_CLICKING, MIDDLE_CLICKING, LEFT_RIGHT_CLICKING, RELEASED
	}

	public class MouseInfo {
		public static final int MOUSE_POINT_HISTORY_SIZE = 100;
		public static final int MOUSE_CLICK_HISTORY_SIZE = 10;

		public class PointInfo {
			public Point point;
			public long since;

			public PointInfo(Point point, long time) {
				this.point = point;
				this.since = time;
			}
		}
		
		public class ClickInfo {
			public MouseState state;
			public long since;
			
			public ClickInfo(MouseState state, long time) {
				this.state = state;
				this.since = time;
			}
		}

		public Shelf<PointInfo> pointHistory;
		public Shelf<ClickInfo> clickHistory;
		public Vector2d velocity;

		public MouseInfo() {
			this.clickHistory = new Shelf<ClickInfo>(MOUSE_CLICK_HISTORY_SIZE);
			this.pointHistory = new Shelf<PointInfo>(MOUSE_POINT_HISTORY_SIZE);
			this.velocity = new Vector2d(0, 0);
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
			if (this.clickHistory.size() > 0)
				return this.clickHistory.peekFirst().state;
			else 
				return MouseState.RELEASED;
		}
		
		public long getLatestStateTime() {
			if (this.clickHistory.size() > 0) 
				return this.clickHistory.peekFirst().since;
			else
				return 0;
		}

		public void addPointInfo(Point point, long time) {
			this.pointHistory.addFirst(new PointInfo(point, time));
		}
		
		public void addClickInfo(MouseState state, long time) {
			this.clickHistory.addFirst(new ClickInfo(state, time));
		}
	}

	public MouseInfo mouseInfo = new MouseInfo();

	@Override
	public void mouseDragged(MouseEvent e) {
		// Srsly, stop it.
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point oldPoint = this.mouseInfo.getLatestPoint();
		long oldTime = this.mouseInfo.getLatestPointTime();
		this.mouseInfo.addPointInfo(e.getPoint(), System.currentTimeMillis());
		this.mouseInfo.velocity = getMouseVector(
				this.mouseInfo.getLatestPoint(), oldPoint,
				this.mouseInfo.getLatestPointTime() - oldTime);
	}

	public Vector2d getMouseVector(Point p1, Point p2, long delta) {
		double distance = Point.distance(p1.x, p1.y, p2.x, p2.y);
		double speed = distance * 1000 / delta;
		Vector2d direction = new Vector2d(p1.x - p2.x, p1.y - p2.y);
		direction.scale(speed);
		return direction;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// OMG, NO!!
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (this.mouseInfo.getLatestState() == MouseState.RIGHT_CLICKING) {
				this.mouseInfo.addClickInfo(MouseState.LEFT_RIGHT_CLICKING, System.currentTimeMillis());
			} else {
				this.mouseInfo.addClickInfo(MouseState.LEFT_CLICKING, System.currentTimeMillis());
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			if (this.mouseInfo.getLatestState() == MouseState.LEFT_CLICKING) {
				this.mouseInfo.addClickInfo(MouseState.LEFT_RIGHT_CLICKING, System.currentTimeMillis());
			} else {
				this.mouseInfo.addClickInfo(MouseState.RIGHT_CLICKING, System.currentTimeMillis());
			}
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			this.mouseInfo.addClickInfo(MouseState.MIDDLE_CLICKING, System.currentTimeMillis());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (this.mouseInfo.getLatestState() == MouseState.LEFT_RIGHT_CLICKING) {
				this.mouseInfo.addClickInfo(MouseState.RIGHT_CLICKING, System.currentTimeMillis());
			} else {
				this.mouseInfo.addClickInfo(MouseState.RELEASED, System.currentTimeMillis());
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			if (this.mouseInfo.getLatestState() == MouseState.LEFT_RIGHT_CLICKING) {
				this.mouseInfo.addClickInfo(MouseState.LEFT_CLICKING, System.currentTimeMillis());
			} else {
				this.mouseInfo.addClickInfo(MouseState.RELEASED, System.currentTimeMillis());
			}
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			this.mouseInfo.addClickInfo(MouseState.RELEASED, System.currentTimeMillis());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Return the state of the mouse in a readable String
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
