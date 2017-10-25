package de.alexa.dto;

import java.util.List;

public class AllBuildsDTO {

	public String description, displayName, url, name, color;

	public List<BuildDTO> builds;
	public BuildDTO firstBuild, lastBuild, lastFailedBuild, lastStableBuild,
			lastSuccessfulBuild, lastUnstableBuild, lastUnsuccessfulBuild;
	public List<HealthReportDTO> healthReport;

	public static class BuildDTO {
		public int number;
		public String url;
	}
	
	public static class HealthReportDTO {
		public String description;
		public int score;
	}
}
