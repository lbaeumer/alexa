package de.alexa.ws.news;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import de.alexa.ws.AlexaNewsIntent;
import de.alexa.ws.AlexaNewsResponse;

public class HSBCNewsIntent implements AlexaNewsIntent {

	private static final Logger log = Logger
			.getLogger(HSBCNewsIntent.class.getName());

	@Override
	public List<AlexaNewsResponse> handleNewsIntent() {
		log.info("news intent");

		AlexaNewsResponse res = new AlexaNewsResponse();
		res.mainText = "Die Dividendensaison kommt allmählich ins Rollen. Auch in diesem Jahr wollen Unternehmen Investoren an den Gewinnen teilhaben lassen und schütten mehr Geld an die Aktionäre aus als jemals zuvor....";
		res.redirectionUrl = "https://www.hsbc-zertifikate.de/home/news/DAX_Investoren_freuen_sich_auf_Rekordausschuettungen.html";
		res.titleText = "DAX-Investoren freuen sich auf Rekordausschüttungen";
		res.uid = res.titleText.hashCode() + ":" + System.currentTimeMillis()%1000;
		res.updateDate = new Date();
		return Collections.singletonList(res);
	}
}
