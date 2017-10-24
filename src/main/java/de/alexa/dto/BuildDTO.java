package de.alexa.dto;

public class BuildDTO {

	public boolean building;
	public int number;
	public String id, result;

	@Override
	public String toString() {
		return "BuildDTO [building=" + building + ", number=" + number + ", id="
				+ id + ", result=" + result + "]";
	}
}
