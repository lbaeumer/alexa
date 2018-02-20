package de.alexa.ws.custom;

import java.util.Random;

import de.alexa.ws.AlexaCustomIntent;
import de.alexa.ws.AlexaCustomRequest;
import de.alexa.ws.AlexaCustomResponse;

public class KollegenIntent implements AlexaCustomIntent {

	@Override
	public AlexaCustomResponse handleIntent(AlexaCustomRequest request) {
		AlexaCustomResponse json;
		try {
			String name = request.request.intent.slots.get("Name").get("value").toString();

			String ret = "Sorry " + name + ", ich kenne dich nicht";
			if (name.equalsIgnoreCase("lui")
					|| name.equalsIgnoreCase("juicy")
					|| name.equalsIgnoreCase("louis")) {
				String[] r = {
						"Lui, du bist ein leuchtender Stern am Himmel der Eiti Geeks. Deine Genialität kennt keine Grenzen.",
						"Lui, du bist ein genialer Alexa Bezwinger. Nicht nur Alexa sondern auch alle anderen Mädels liegen dir zu Füßen."};
				ret = r[new Random().nextInt() % r.length];
			} else if (name.equalsIgnoreCase("lutz")) {
				String[] r = {
						"Lutz, du bist ein hirnloser versoffener Schwachkopf.",
						"Lutz, als Vorsitzender des Primitivitätsförderungsvereins machst du deiner Rolle alle Ehre. An Primitivität bist du nicht mehr zu übertreffen."};
				ret = r[new Random().nextInt() % r.length];
			} else if (name.equalsIgnoreCase("klaudius")
					|| name.equalsIgnoreCase("claudius")) {
				String[] r = {
						"Hallo Klaudius, du bist ein Schwachkopf mit einem kleinen Pimmäl. Kauf dir mal ein Fahrrad."};
				ret = r[new Random().nextInt() % r.length];
			} else if (name.equalsIgnoreCase("Claus")) {
				ret = "Claus, du bist eine schwer gestörte Nuttä, die sich am liebsten in den Kölner Schwulenclubs herum treibt. Poppst du eigentlich noch den Reza?";
			} else if (name.equalsIgnoreCase("Olli")) {
				ret = "Olli, du bist nur nicht der größte Schwachkopf der alten Software Architektur, sondern du stichst auch in dem neuen Laden durch Stumpfsinn hervor.";
			} else if (name.startsWith("fu")
					|| name.equalsIgnoreCase("Four")
					|| name.equalsIgnoreCase("foo")) {
				String[] r = {
						"Fuh, du bist ein verstrahlter Holzkopf mit Pimmälpiercing.",
						"Fuh, wo ist denn dein Borat?"};
				ret = r[new Random().nextInt() % r.length];
			}

			json = new AlexaCustomResponse(ret);

		} catch (NullPointerException e) {
			json = new AlexaCustomResponse("Hier ist etwas kaputt");
		}

		return json;

	}

	@Override
	public AlexaCustomResponse handleLaunchIntent(AlexaCustomRequest request) {
		return new AlexaCustomResponse(
				"Ich freune mich auf deine Fragen zu deinen Kollegen.");
	}

}
