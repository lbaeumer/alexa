package de.alexa.ws.news;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import de.alexa.ws.AlexaNewsIntent;
import de.alexa.ws.AlexaNewsResponse;

public class NewsIntent implements AlexaNewsIntent {

	private static final Logger log = Logger
			.getLogger(NewsIntent.class.getName());

	@Override
	public List<AlexaNewsResponse> handleNewsIntent() {
		log.info("news intent");
		List<AlexaNewsResponse> l = new ArrayList<>();

		AlexaNewsResponse res = new AlexaNewsResponse();
		res.mainText = "Der Lennard ist ein Pupsie, ...";
		res.redirectionUrl = "https://www.hsbc-zertifikate.de/pdfs/archive/dailytrading/20170413_HSBC_TuB_DailyTrading.pdf";
		res.titleText = "Lennrad pupst den gazen Tag";
		res.uid = res.titleText.hashCode() + ":"
				+ System.currentTimeMillis() % 1000;
		res.updateDate = new Date();
		l.add(res);

		return l;
	}
}
