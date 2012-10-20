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

		public class PointInfo {
			public Point point;
			public long since;

			public PointInfo(Point point, long time) {
				this.point = point;
				this.since = time;
			}
		}

		public MouseState state;
		public Shelf<PointInfo> pointHistory;
		public Vector2d velocity;

		public MouseInfo() {
			this.state = MouseState.RELEASED;
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

		public void addPointInfo(Point point, long time) {
			this.pointHistory.addFirst(new PointInfo(point, time));
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
		// OMG NO!!
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (this.mouseInfo.state == MouseState.RIGHT_CLICKING) {
				this.mouseInfo.state = MouseState.LEFT_RIGHT_CLICKING;
			} else {
				this.mouseInfo.state = MouseState.LEFT_CLICKING;
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			if (this.mouseInfo.state == MouseState.LEFT_CLICKING) {
				this.mouseInfo.state = MouseState.LEFT_RIGHT_CLICKING;
			} else {
				this.mouseInfo.state = MouseState.RIGHT_CLICKING;
			}
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			this.mouseInfo.state = MouseState.MIDDLE_CLICKING;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (this.mouseInfo.state == MouseState.LEFT_RIGHT_CLICKING) {
				this.mouseInfo.state = MouseState.RIGHT_CLICKING;
			} else {
				this.mouseInfo.state = MouseState.RELEASED;
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			if (this.mouseInfo.state == MouseState.LEFT_RIGHT_CLICKING) {
				this.mouseInfo.state = MouseState.LEFT_CLICKING;
			} else {
				this.mouseInfo.state = MouseState.RELEASED;
			}
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			this.mouseInfo.state = MouseState.RELEASED;
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

}
