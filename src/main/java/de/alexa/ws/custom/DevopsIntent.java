package de.alexa.ws.custom;

import java.util.Random;

import de.alexa.ws.AlexaCustomIntent;
import de.alexa.ws.AlexaCustomRequest;
import de.alexa.ws.AlexaCustomResponse;

public class DevopsIntent implements AlexaCustomIntent {

	@Override
	public AlexaCustomResponse handleIntent(AlexaCustomRequest request) {
		AlexaCustomResponse json = null;

		if ("DeployIntent".equals(request.request.intent.name)) {
			try {
				String name = request.request.intent.slots.get("Name")
						.get("value").toString();

				String ret = "Sorry " + name + ", ich kenne dich nicht";
				if (name.equalsIgnoreCase("cobam")) {
					ret = "I'm happy to deploy application " + name + " for you.";

				} else if (name.equalsIgnoreCase("test")) {
					ret = "I'm happy to deploy application " + name + " for you.";
				}

				json = new AlexaCustomResponse(ret);

			} catch (NullPointerException e) {
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
