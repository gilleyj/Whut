package com.flipflop.collections;

import java.util.Collection;
import java.util.LinkedList;

public class Shelf<E> extends LinkedList<E> {
	private static final long serialVersionUID = -2667570869563862093L;
	protected int width;

	public Shelf(int width) {
		super();
		this.width = width;
	}

	@Override
	public boolean add(E e) {
		super.addFirst(e);
		while (this.size() > this.width) {
			super.removeLast();
		}
		return true;
	}

	@Override
	public void add(int index, E element) {
		if (index < this.width) {
			super.add(index, element);
			while (this.size() > this.width) {
				super.removeLast();
			}
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (addAll(0, c)) {
			while (this.size() > this.width) {
				super.removeLast();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if (super.addAll(index, c)) {
			while (this.size() > this.width) {
				this.removeLast();
			}
			return true;
		}
		return false;
	}

	@Override
	public void addFirst(E e) {
		super.addFirst(e);
		while (this.size() > this.width) {
			this.removeLast();
		}
	}

	@Override
	public void addLast(E e) {
		if (this.size() < this.width) {
			super.addLast(e);
			while (this.size() > this.width) {
				this.removeLast();
			}
		}
	}

}
