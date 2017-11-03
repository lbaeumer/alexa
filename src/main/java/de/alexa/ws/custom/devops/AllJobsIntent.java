package de.alexa.ws.custom.devops;

import java.io.IOException;

import org.apache.log4j.Logger;

import de.alexa.dto.AllJobsDTO;
import de.alexa.dto.AllJobsDTO.JobDTO;
import de.alexa.ws.AlexaCustomResponse;
import de.alexa.ws.custom.util.JenkinsUtil;

public class AllJobsIntent {

	private static final Logger log = Logger
			.getLogger(AllJobsIntent.class.getName());

	private JenkinsUtil util = new JenkinsUtil();

	public String followUpText;

	public AlexaCustomResponse handleIntent() throws IOException {

		log.info("handle");
		AlexaCustomResponse json;
		AllJobsDTO b = util.getAllJobs();
		int success = 0;
		int failed = 0;
		int notbuild = 0;
		int unstable = 0;
		StringBuffer failedStrb = new StringBuffer();
		StringBuffer successStrb = new StringBuffer();
		for (JobDTO j : b.jobs) {
			if ("blue".equals(j.color)) {
				success++;
				if (successStrb.length() > 0) {
					successStrb.append(", ");
				}
				successStrb.append(j.name);
			} else if ("notbuilt".equals(j.color)) {
				notbuild++;
			} else if ("red".equals(j.color)) {
				failed++;
				if (failedStrb.length() > 0) {
					failedStrb.append(", ");
				}
				failedStrb.append(j.name);
			} else if ("yellow".equals(j.color)) {
				unstable++;
			}
		}
		String ret;

		ret = "I found " + (failed + success + notbuild + unstable) + " jobs. "
				+ success + " jobs are successful. " + failed + " jobs failed. "
				+ (notbuild > 0 ? notbuild + " jobs have not been build." : "")
				+ ".";

		followUpText = "The following builds failed: " + failedStrb
				+ ". These jobs succeeded " + successStrb;

		json = new AlexaCustomResponse(ret);
		json.shouldEndSession(false);
		json.setRepromt("Do you want to get more information?");

		return json;
	}

	public String getFollowUpText() {
		return followUpText;
	}
}
