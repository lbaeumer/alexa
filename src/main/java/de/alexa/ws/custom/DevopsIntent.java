package de.alexa.ws.custom;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import de.alexa.ws.AlexaCustomIntent;
import de.alexa.ws.AlexaCustomRequest;
import de.alexa.ws.AlexaCustomResponse;
import de.alexa.ws.custom.devops.AllJobsIntent;
import de.alexa.ws.custom.devops.BuildStatusIntent;
import de.alexa.ws.custom.devops.ExecuteJobIntent;
import de.alexa.ws.custom.util.JenkinsUtil;

public class DevopsIntent implements AlexaCustomIntent {

	private static final Logger log = Logger
			.getLogger(DevopsIntent.class.getName());

	public static Map<String, SessionDTO> map = new HashMap<String, SessionDTO>();

	@Override
	public AlexaCustomResponse handleIntent(AlexaCustomRequest request) {
		AlexaCustomResponse json = null;

		final String sessionId = (request.session != null
				? request.session.sessionId
				: null);

		log.info("going with " + request.request.intent.name + " and session "
				+ sessionId);
		if ("ExecuteJobIntent".equals(request.request.intent.name)) {
			try {
				Map<String, Map<String, Object>> slots = request.request.intent.slots;
				Object jobName = slots.get("job").get("value");

				ExecuteJobIntent intent = new ExecuteJobIntent();
				json = intent.handleIntent("" + jobName);

				if (intent.getFollowUpText() != null) {
					SessionDTO session = new SessionDTO(
							request.request.intent.name,
							intent.getFollowUpText(), "" + jobName);
					map.put(sessionId, session);
				}

			} catch (Exception e) {
				log.fatal("failed", e);
				json = new AlexaCustomResponse(
						"Hups, there is a problem with starting the application.");
			}

		} else if ("BuildStatusIntent".equals(request.request.intent.name)) {
			try {
				Map<String, Map<String, Object>> slots = request.request.intent.slots;
				Object jobName = slots.get("job").get("value");

				BuildStatusIntent intent = new BuildStatusIntent();
				json = intent.handleIntent("" + jobName);

			} catch (Exception e) {
				log.fatal("failed", e);
				json = new AlexaCustomResponse(
						"Hups, there is a problem with the build status.");
			}

		} else if ("AllJobsIntent".equals(request.request.intent.name)) {
			try {
				AllJobsIntent intent = new AllJobsIntent();
				json = intent.handleIntent();

				SessionDTO session = new SessionDTO(request.request.intent.name,
						intent.getFollowUpText());
				map.put(sessionId, session);

			} catch (Exception e) {
				log.fatal("failed", e);
				json = new AlexaCustomResponse(
						"Hups, there is a problem getting the jobs.");
			}
		} else if ("YesNoIntent".equals(request.request.intent.name)) {
			try {
				Map<String, Map<String, Object>> slots = request.request.intent.slots;
				Object answer = slots.get("YesNo").get("value");
				if (answer == null) {
					answer = "no";
				}

				log.info("you said " + answer + "; sessionId=" + sessionId);
				SessionDTO session = map.get(sessionId);
				log.info("session=" + sessionId + ";" + map);
				map.remove(sessionId);

				if (session != null
						&& session.intentName.equals("AllJobsIntent")) {

					if ("yes".equalsIgnoreCase(answer.toString())) {
						json = new AlexaCustomResponse(session.text.toString());
					} else {
						json = new AlexaCustomResponse("");
					}
				}
				if (session != null
						&& session.intentName.equals("ExecuteJobIntent")) {

					if ("yes".equalsIgnoreCase(answer.toString())) {
						json = new AlexaCustomResponse(session.text.toString());

						JenkinsUtil util = new JenkinsUtil();
						util.startBuild(session.jobName, "");
					} else {
						json = new AlexaCustomResponse("Ok, goodbye.");
					}
				}

			} catch (Exception e) {
				log.fatal("failed", e);
				json = new AlexaCustomResponse(
						"Hups, there is a problem with starting the application.");
			}
		}

		return json;
	}

	public static class SessionDTO {

		public String intentName;
		public String text;
		public String jobName;

		public SessionDTO(String intentName, String text) {
			this.intentName = intentName;
			this.text = text;
		}
		public SessionDTO(String intentName, String text, String jobName) {
			this.intentName = intentName;
			this.text = text;
			this.jobName = jobName;
		}
		@Override
		public String toString() {
			return "SessionDTO [intentName=" + intentName + ", text=" + text
					+ ", jobName=" + jobName + "]";
		}
	}

	@Override
	public AlexaCustomResponse handleLaunchIntent(AlexaCustomRequest request) {
		return new AlexaCustomResponse(
				"I'm happy to serve your DevOps requests. You can ask me about jenkins jobs, the build status and you can start builds.");
	}
}
