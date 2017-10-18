package de.alexa.ws.custom;

import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import de.alexa.ws.AlexaCustomIntent;
import de.alexa.ws.AlexaCustomRequest;
import de.alexa.ws.AlexaCustomResponse;
import de.alexa.ws.DispatcherEndpoint;

public class DevopsIntent implements AlexaCustomIntent {

	private static final Logger log = Logger
			.getLogger(DevopsIntent.class.getName());

	@Override
	public AlexaCustomResponse handleIntent(AlexaCustomRequest request) {
		AlexaCustomResponse json = null;

		log.info("going with " + request.request.intent.name);
		if ("DeployIntent".equals(request.request.intent.name)) {
			try {
				Map<String, Map<String, Object>> slots = request.request.intent.slots; 
				String name = slots.get("application")
						.get("value").toString();

				String ret = "Sorry " + name + ", ich kenne dich nicht";
				if (name.equalsIgnoreCase("cobam")) {
					ret = "I'm happy to deploy application " + name + " for you.";

				} else if (name.equalsIgnoreCase("test")) {
					ret = "I'm happy to deploy application " + name + " for you.";
				}

				json = new AlexaCustomResponse(ret);

			} catch (Exception e) {
				log.fatal("failed", e);
				json = new AlexaCustomResponse("Ich bin doof");
			}

		}

		return json;
	}

	@Override
	public AlexaCustomResponse handleLaunchIntent(AlexaCustomRequest request) {
		return new AlexaCustomResponse(
				"Ich freune mich auf deine Fragen zu den Pupsies");
	}

}
