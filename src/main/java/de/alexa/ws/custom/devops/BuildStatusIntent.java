package de.alexa.ws.custom.devops;

import java.io.IOException;

import org.apache.log4j.Logger;

import de.alexa.dto.AllBuildsDTO;
import de.alexa.ws.AlexaCustomResponse;
import de.alexa.ws.custom.util.JenkinsUtil;

public class BuildStatusIntent {

	private static final Logger log = Logger
			.getLogger(BuildStatusIntent.class.getName());

	private JenkinsUtil util = new JenkinsUtil();

	public String followUpText;

	public AlexaCustomResponse handleIntent(String jobName) throws IOException {

		log.info("build status for " + jobName);
		AlexaCustomResponse json;
		AllBuildsDTO b = util.getAllBuilds(jobName);
		String ret;
		if (b != null) {
			String status;
			if (b.lastSuccessfulBuild != null
					&& b.lastBuild.number == b.lastSuccessfulBuild.number) {
				status = "is successful";
			} else if (b.lastFailedBuild != null
					&& b.lastBuild.number == b.lastFailedBuild.number) {
				status = "failed";
			} else if (b.lastUnstableBuild != null
					&& b.lastBuild.number == b.lastUnstableBuild.number) {
				status = "is unstable";
			} else {
				status = "is unknown";
			}
			ret = "The latest build with number " + b.lastBuild.number
					+ " of your application " + jobName + " " + status + ". "
					+ (b.healthReport != null && b.healthReport.size() > 0
							? b.healthReport.get(0).description
							: "");
			json = new AlexaCustomResponse(ret);
		} else {

			ret = "Sorry, the application " + jobName + " does not exist.";
			json = new AlexaCustomResponse(ret);
		}

		return json;
	}

	public String getFollowUpText() {
		return followUpText;
	}
}
