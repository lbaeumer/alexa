package de.alexa.dto;

import java.util.List;

public class AllJobsDTO {

	public List<JobDTO> jobs;
	
	public static class JobDTO {

		public String name, url, color;
	}

}
