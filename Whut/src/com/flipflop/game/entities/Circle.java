package com.flipflop.game.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Circle implements Shape {

	protected Color fillColor = Color.white;
	protected Color strokeColor = Color.black;
	protected float strokeWidth = 2.F;
	protected BasicStroke stroke = new BasicStroke(this.strokeWidth);
	protected double radius = 50.F;
	protected double x = 0;
	protected double y = 0;
	
	public Circle(double radius, double x, double y) {
		this.radius = radius;
		this.x = x;
		this.y = y;
	}
	
	public Circle(float radius) {
		this.radius = radius;
	}

	@Override
	public void render(Graphics g) {
		int radiusTwice = (int) (this.radius * 2);
		int radiusHalf = (int) (this.radius * 0.5);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(this.fillColor);
		g2.fillOval((int) (this.x - radiusHalf), 
				(int) (this.y - radiusHalf), 
				radiusTwice, 
				radiusTwice);
		g2.setColor(this.strokeColor);
		g2.setStroke(this.stroke);
		g2.drawOval((int) (this.x - radiusHalf), 
				(int) (this.y - radiusHalf), 
				radiusTwice, 
				radiusTwice);
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}

	public float getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(float strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	public BasicStroke getStroke() {
		return stroke;
	}

	public void setStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
}
