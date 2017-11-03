package de.alexa.ws.custom.devops;

import java.io.IOException;

import org.apache.log4j.Logger;

import de.alexa.dto.AllBuildsDTO;
import de.alexa.ws.AlexaCustomResponse;
import de.alexa.ws.custom.util.JenkinsUtil;

public class ExecuteJobIntent {

	private static final Logger log = Logger
			.getLogger(ExecuteJobIntent.class.getName());

	private JenkinsUtil util = new JenkinsUtil();

	public String followUpText;

	public AlexaCustomResponse handleStart(String yesNo) throws IOException {
		return null;
	}

	public AlexaCustomResponse handleIntent(String jobName) throws IOException {

		log.info("execute job " + jobName);
		AlexaCustomResponse json;

		AllBuildsDTO rc = util.getAllBuilds(jobName);

		String ret;
		if (rc != null) {
			ret = "I'm happy to start job " + jobName + " for you. ";
			json = new AlexaCustomResponse(ret);
			json.setRepromt("Shall I execute job " + jobName + " now?");
			json.shouldEndSession(false);

			followUpText = "I just started job " + jobName + " for you.";

		} else {
			ret = "Sorry, I cannot start job " + jobName
					+ ". It does not exist.";
			json = new AlexaCustomResponse(ret);
		}

		return json;
	}

	public String getFollowUpText() {
		return followUpText;
	}
}
