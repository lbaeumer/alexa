package de.alexa.ws.custom;

import java.util.Map;

import org.apache.log4j.Logger;

import de.alexa.dto.AllBuildsDTO;
import de.alexa.dto.AllJobsDTO;
import de.alexa.dto.AllJobsDTO.JobDTO;
import de.alexa.ws.AlexaCustomIntent;
import de.alexa.ws.AlexaCustomRequest;
import de.alexa.ws.AlexaCustomResponse;

public class DevopsIntent implements AlexaCustomIntent {

	private static final Logger log = Logger
			.getLogger(DevopsIntent.class.getName());

	private JenkinsUtil util = new JenkinsUtil();

	@Override
	public AlexaCustomResponse handleIntent(AlexaCustomRequest request) {
		AlexaCustomResponse json = null;

		log.info("going with " + request.request.intent.name);
		if ("StartBuildIntent".equals(request.request.intent.name)) {
			try {
				Map<String, Map<String, Object>> slots = request.request.intent.slots;
				String name = slots.get("application").get("value").toString();

				int rc = util.startBuild(name,
						"payload=hallo&repositoryUrl=http://myurl");

				String ret;
				if (rc == 201) {
					ret = "I'm happy to deploy application " + name
							+ " for you. ";
				} else if (rc == 404) {
					ret = "Sorry, I don't know appliction " + name + ".";
				} else {
					ret = "Sorry, something is wrong here.";
				}

				json = new AlexaCustomResponse(ret);

			} catch (Exception e) {
				log.fatal("failed", e);
				json = new AlexaCustomResponse(
						"Hups, there is a problem with starting the application.");
			}

		} else if ("BuildStatusIntent".equals(request.request.intent.name)) {
			try {
				Map<String, Map<String, Object>> slots = request.request.intent.slots;
				String name = slots.get("application").get("value").toString();

				AllBuildsDTO b = util.getAllBuilds(name);
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
							+ " of your application " + name + " " + status
							+ ". "
							+ (b.healthReport != null && b.healthReport.size() > 0
							? b.healthReport.get(0).description : "");
				} else {
					ret = "Sorry, the application " + name + " does not exist.";
				}

				json = new AlexaCustomResponse(ret);

			} catch (Exception e) {
				log.fatal("failed", e);
				json = new AlexaCustomResponse(
						"Hups, there is a problem with the build status.");
			}

		} else if ("AllJobsIntent".equals(request.request.intent.name)) {
			try {

				AllJobsDTO b = util.getAllJobs();
				int success = 0;
				int failed = 0;
				int notbuild = 0;
				int unstable = 0;
				StringBuffer strb = new StringBuffer();
				for (JobDTO j : b.jobs) {
					if ("blue".equals(j.color)) {
						success++;
					} else if ("notbuilt".equals(j.color)) {
						notbuild++;
					} else if ("red".equals(j.color)) {
						failed++;
						if (strb.length() > 0) {
							strb.append(", ");
						}
						strb.append(j.name);
					} else if ("yellow".equals(j.color)) {
						unstable++;
					}
				}
				String ret;

				ret = "I found " + (failed + success + notbuild + unstable)
						+ " jobs. " + success + " jobs are successful. "
						+ failed + " jobs failed. "
						+ (notbuild > 0
								? notbuild + " jobs have not been build."
								: "")
						+ ". The following builds failed: " + strb + ".";

				json = new AlexaCustomResponse(ret);

			} catch (Exception e) {
				log.fatal("failed", e);
				json = new AlexaCustomResponse(
						"Hups, there is a problem getting the jobs.");
			}
		}

		return json;
	}

	@Override
	public AlexaCustomResponse handleLaunchIntent(AlexaCustomRequest request) {
		return new AlexaCustomResponse(
				"I'm happy to serve your DevOps requests.");
	}
}
