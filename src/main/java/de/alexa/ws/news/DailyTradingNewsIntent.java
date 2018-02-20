package de.alexa.ws.news;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import de.alexa.ws.AlexaNewsIntent;
import de.alexa.ws.AlexaNewsResponse;

public class DailyTradingNewsIntent implements AlexaNewsIntent {

	private static final Logger log = Logger
			.getLogger(DailyTradingNewsIntent.class.getName());

	@Override
	public List<AlexaNewsResponse> handleNewsIntent() {
		log.info("news intent");
		List<AlexaNewsResponse> l = new ArrayList<>();

		AlexaNewsResponse res = new AlexaNewsResponse();
		res.mainText = "Seit dem Scheitern an den historischen Hochständen aus dem April 2015 bei 12.391 Punkten befindet sich der DAX in einer korrektiven Phase. Dabei sucht das Aktienbarometer aktuell nach Orientierung, ...";
		res.redirectionUrl = "https://www.hsbc-zertifikate.de/pdfs/archive/dailytrading/20170413_HSBC_TuB_DailyTrading.pdf";
		res.titleText = "DailyTrading Newsletter „swing low“ lässt hoffen";
		res.uid = res.titleText.hashCode() + ":" + System.currentTimeMillis()%1000;
		res.updateDate = new Date();
		l.add(res);

		res = new AlexaNewsResponse();
		res.mainText = "Seit Mitte März befindet sich der Euro-BUND-Future im Rally-Modus. Ob die Aufwärtsbewegung dabei so munter wie bisher weitergeht, dürfte sich in Kürze zeigen... ";
		res.redirectionUrl = "https://www.hsbc-zertifikate.de/pdfs/archive/dailytrading/20170413_HSBC_TuB_DailyTrading.pdf";
		res.titleText = "Hohe Hürden voraus";
		res.uid = res.titleText.hashCode() + ":" + System.currentTimeMillis()%1000;
		res.updateDate = new Date();
		l.add(res);

		return l;
	}
}
