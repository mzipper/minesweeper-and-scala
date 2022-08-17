package model;

import java.awt.Point;

public class Cell {
	private Point location;
	
	private CellStatus cellStatus = CellStatus.BLANK;
	private int number = 0;
	
	private RightClickStatus rightClickStatus = RightClickStatus.BLANK;
	
	
	public Cell(Point location) {
		this.location = location;
	}
	
	
	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public CellStatus getCellStatus() {
		return cellStatus;
	}

	public void setCellStatus(CellStatus cellStatus) {
		this.cellStatus = cellStatus;
	}

	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}

	public RightClickStatus getRightClickStatus() {
		return rightClickStatus;
	}

	public void setRightClickStatus(RightClickStatus rightClickStatus) {
		this.rightClickStatus = rightClickStatus;
	}
	
}
