package de.lennard.ws.custom;

import java.util.Random;

import de.lennard.ws.AlexaCustomIntent;
import de.lennard.ws.AlexaCustomRequest;
import de.lennard.ws.AlexaCustomResponse;

public class PupsieIntent implements AlexaCustomIntent {

	@Override
	public AlexaCustomResponse handleIntent(AlexaCustomRequest request) {
		AlexaCustomResponse json = null;

		if ("PupsiIntent".equals(request.request.intent.name)) {
			try {
				String name = request.request.intent.slots.get("Name")
						.get("value").toString();

				String ret = "Sorry " + name + ", ich kenne dich nicht";
				if (name.equalsIgnoreCase("lui")) {
					ret = "Lui, du bist ein Held.";

				} else if (name.equalsIgnoreCase("lennard")) {
					ret = "Lennard, du bist ein Pupsie";
				}

				json = new AlexaCustomResponse(ret);

			} catch (NullPointerException e) {
				json = new AlexaCustomResponse("Ich bin doof");
			}

		} else if ("OsterhaseIntent".equals(request.request.intent.name)) {

			String a[] = new String[]{
					"Eine Anleitung findest du unter dem DVD Spieler",
					"Augen auf und dann mal suchen",
					"Schau doch mal im Computer nach",
					"Accio Harry Potter Anleitung",
					"Eine Anleitung liegt auf dem Schrank bei den Whiskys",
					"Streng dich mal an und suche selber"};

			json = new AlexaCustomResponse(a[new Random().nextInt(a.length)]);
		}

		return json;
	}

	@Override
	public AlexaCustomResponse handleLaunchIntent(AlexaCustomRequest request) {
		return new AlexaCustomResponse(
				"Ich freune mich auf deine Fragen zu den Pupsies");
	}

}
