package de.alexa.ws.custom.beer;

import de.alexa.ws.AlexaCustomIntent;
import de.alexa.ws.AlexaCustomRequest;
import de.alexa.ws.AlexaCustomResponse;

public class BierIntent implements AlexaCustomIntent {

	@Override
	public AlexaCustomResponse handleIntent(AlexaCustomRequest request) {
		if ("BierIntent".equals(request.request.intent.name)) {
			return handleBierIntent(request);
		} else if ("DrinkIntent".equals(request.request.intent.name)) {
			return handleDrinkIntent(request);
		} else {
			throw new IllegalStateException();
		}
	}

	private AlexaCustomResponse handleBierIntent(AlexaCustomRequest request) {
		AlexaCustomResponse json;
		try {
			String name = request.request.intent.slots.get("Name").get("value").toString();

			String ret = "Sorry " + name + ", ich kenne dich nicht";
			if (name.equalsIgnoreCase("lui") || name.equalsIgnoreCase("louis")) {
				ret = "Lui, natürlich darfst du jetzt Bier trinken. Bier ist lecker und gesund. Ich schicke die Britta gleich mal los Bier und Chips zu besorgen.";
			} else if (name.equalsIgnoreCase("Papa")) {
				ret = "Papa, natürlich darfst du jetzt Bier trinken. Bier ist lecker und gesund. Ich schicke die Britta gleich mal los Bier und Chips zu besorgen.";
			} else if (name.equalsIgnoreCase("Stefan")) {
				ret = "Stefan, du hast am Wochenende in Hamburg soviel gesoffen, so dass du jetzt mal besser eine Pause machst.";
			} else if (name.equalsIgnoreCase("hubi")) {
				ret = "Hubi, du lallst eh schon den ganzen Tag, so dass du mal besser die Finger vom Bier lässt.";
			} else if (name.equalsIgnoreCase("marcus")
					|| name.equalsIgnoreCase("markus")) {
				ret = "Marcus, du säufst ohnehin den ganzen Tag und bleibst besser mal beim Wasser.";
			} else if (name.equalsIgnoreCase("carsten")) {
				ret = "Carsten, mach dich mal zackig auf den Weg zum Lui, da er schon mit großem Durst auf dich wartet.";
			} else if (name.equalsIgnoreCase("thorsten")) {
				ret = "Thorsten, hol mal den Whisky raus.";
			} else if (name.equalsIgnoreCase("lennard")) {
				ret = "Lennard, du bist noch zu jung. Aber du darfst eine Cola";
			}

			json = new AlexaCustomResponse(name, ret);

		} catch (NullPointerException e) {
			json = new AlexaCustomResponse("Ich bin doof");
		}

		return json;

	}

	private AlexaCustomResponse handleDrinkIntent(AlexaCustomRequest request) {

		String name = request.request.intent.slots.get("Drink").get("value").toString();

		String ret = "Sorry " + name + " kenne ich nicht. Das ist bestimmt ungesund.";
		if (name == null) {
			ret = "Ich konnte keinen Namen erkennen";
		} else {
			if (name.equalsIgnoreCase("bier")) {
				ret = "Ja, Bier ist besonders gesund und sollte regelmäßig zur Gesundheitsvorsorge getrunken werden.";
			} else if (name.equalsIgnoreCase("ouzo") || name.equalsIgnoreCase("uso")) {
				ret = "Ouzo trinkt man mit guten Freunden und zwar reichlich.";
			} else if (name.equalsIgnoreCase("whisky")) {
				ret = "Whisky ist die reinste Medizin gegen eine Vielzahl an Krankheiten.";
			} else if (name.equalsIgnoreCase("milch")) {
				ret = "Das beste der Milch ist auch in Schokolade drin. Daher iss lieber Schokolade.";
			} else if (name.equalsIgnoreCase("wasser")) {
				ret = "Wasser ist fast so gesund wie Bier, aber nicht so lecker.";
			} else if (name.equalsIgnoreCase("cola")) {
				ret = "In Cola ist viel zu viel Zucker. Lass lieber die Finger davon und trinke Bier stattdessen.";
			} else if (name.equalsIgnoreCase("fanta")) {
				ret = "In Fanta ist viel zu viel Zucker. Lass lieber die Finger davon und trinke Bier stattdessen.";
			} else if (name.equalsIgnoreCase("apfelsaft") || name.equalsIgnoreCase("apfelschorle")) {
				ret = "Apfelsaft und Apfelschorle sind ok aber nicht so lecker wie Bier.";
			}
		}

		return new AlexaCustomResponse(ret);
	}

	@Override
	public AlexaCustomResponse handleLaunchIntent(AlexaCustomRequest request) {
		return new AlexaCustomResponse(
				"Ich freune mich auf deine Fragen zum Bier trinken.");
	}

}
