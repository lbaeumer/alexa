package de.alexa.ws.custom.certificate;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import de.alexa.ws.AlexaCustomIntent;
import de.alexa.ws.AlexaCustomRequest;
import de.alexa.ws.AlexaCustomResponse;
import de.alexa.ws.custom.certificate.CertificateStore.CertificateDTO;

public class HSBCZertifikateIntent implements AlexaCustomIntent {

	private static final Logger log = Logger
			.getLogger(HSBCZertifikateIntent.class.getName());

	private String name = null;
	private String type = null;
	private Date from, to;

	private List<CertificateDTO> filteredCertificates;
	private CertificateDTO certificateToBuy;

	private static HSBCZertifikateIntent instance;

	public static HSBCZertifikateIntent getInstance() {
		if (instance == null) {
			instance = new HSBCZertifikateIntent();
		}
		return instance;
	}

	private HSBCZertifikateIntent() {
		// filteredCertificates = CertificateStore.getInstance().getAll();
		// log.info("found " + filteredCertificates.size() + " certificates");
	}

	@Override
	public AlexaCustomResponse handleIntent(AlexaCustomRequest request) {
		log.info("intent=" + request.request.intent.name);

		if ("BuyIntent".equals(request.request.intent.name)) {
			return handleBuyIntent(request);
		} else if ("BuyYesNoIntent".equals(request.request.intent.name)) {
			return handleBuyYesNoConfirmIntent(request);
		} else if ("BuyCountIntent".equals(request.request.intent.name)) {
			return handleBuyCountIntent(request);
		} else if ("PriceIntent".equals(request.request.intent.name)) {
			return handlePriceIntent(request);

		} else if ("AMAZON.HelpIntent".equals(request.request.intent.name)) {
			return handleHelpIntent(request);
		} else if ("AMAZON.StopIntent".equals(request.request.intent.name)) {
			return handleStopIntent(request);
		} else if (request.request.intent.name.endsWith("Intent")) {
			return handleFilterIntent(request);
		} else {
			throw new IllegalStateException();
		}
	}

	private AlexaCustomResponse handleFilterIntent(AlexaCustomRequest request) {

		AlexaCustomResponse json = null;
		try {

			setFilter(request);

			log.info("found " + name + "; type=" + type + "; dateFrom=" + from
					+ "; dateTo=" + to);
			filteredCertificates = CertificateStore.getInstance()
					.getFilter(name, type, from, to);
			log.info("found " + filteredCertificates.size() + " certificates");

			// new filter criteria defined
			String ret = null;
			if (name == null && type == null && from == null && to == null) {
				ret = "Willkommen bei HSBC Zertifikate. " + " Eine Auswahl von "
						+ CertificateStore.getInstance().getAll().size()
						+ " Optionen steht zur Verfügung. ";
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("d M yyyy");
				if (filteredCertificates.size() > 0) {
					ret = "Ich habe " + filteredCertificates.size()
							+ " Optionen"
							+ (name != null ? " zum Basiswert " + name : "")
							+ (type != null ? " vom Typ " + type : "")
							+ (from != null && to != null
									? " mit Fälligkeitsdatum zwischen "
											+ sdf.format(from) + " und "
											+ sdf.format(to)
									: "")
							+ (from != null && to == null
									? " mit Fälligkeitsdatum ab "
											+ sdf.format(from)
									: "")
							+ (from == null && to != null
									? " mit Fälligkeitsdatum bis "
											+ sdf.format(to)
									: "")
							+ " gefunden. ";
				} else {
					ret = "Ich habe keine Optionen"
							+ (name != null ? " zum Basiswert " + name : "")
							+ (type != null ? " vom Typ " + type : "")
							+ (from != null && to != null
									? " mit Fälligkeitsdatum zwischen "
											+ sdf.format(from) + " und "
											+ sdf.format(to)
									: "")
							+ (from != null && to == null
									? " mit Fälligkeitsdatum ab "
											+ sdf.format(from)
									: "")
							+ (from == null && to != null
									? " mit Fälligkeitsdatum bis "
											+ sdf.format(to)
									: "")
							+ " gefunden. ";

				}
			}

			if (name == null) {
				json = new AlexaCustomResponse(ret);
				json.shouldEndSession(false);
				json.setRepromt(
						"Auf welchem Basiswert möchtest du Optionen handeln?");
			} else if (type == null) {
				json = new AlexaCustomResponse(
						ret);
				json.shouldEndSession(false);
				json.setRepromt("Möchtest du put oder call optionen kaufen?");
			} else if (from == null) {
				json = new AlexaCustomResponse(
						ret);
				json.shouldEndSession(false);
				json.setRepromt("Ab wann sollen die Optionen fällig werden?");
			} else if (to == null) {
				json = new AlexaCustomResponse(
						ret);
				json.shouldEndSession(false);
				json.setRepromt("Bis wann sollen die Optionen fällig werden?");
			} else {
				if (filteredCertificates.size() > 0) {
					json = new AlexaCustomResponse(
							ret);
					json.shouldEndSession(false);
					json.setRepromt(
							"Soll ich eine geeignete Option auswählen?");
				} else {
					json = new AlexaCustomResponse(ret);
				}
			}

		} catch (NullPointerException e) {
			log.info("Hoppla, ", e);
			json = new AlexaCustomResponse("Hoppla, hier ist etwas kaputt.");
		}

		return json;

	}

	private boolean setFilter(AlexaCustomRequest request) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		boolean found = false;
		Map<String, Map<String, Object>> slots = (request.request.intent != null
				&& request.request.intent.slots != null
						? request.request.intent.slots
						: new HashMap<String, Map<String, Object>>());

		// Basiswert provided
		if (slots.get("Basiswert") != null
				&& slots.get("Basiswert").get("value") != null) {
			name = slots.get("Basiswert").get("value").toString();
			found = true;
		}

		// Typ provided
		if (slots.get("Typ") != null && slots.get("Typ").get("value") != null) {
			type = slots.get("Typ").get("value").toString();
			if (type.equals("puth")) {
				type = "put";
			}
			found = true;
		}

		// DateFrom provided
		if (slots.get("DateFrom") != null
				&& slots.get("DateFrom").get("value") != null) {
			String dateFroms = slots.get("DateFrom").get("value").toString();
			if (dateFroms != null) {
				try {
					from = df.parse(dateFroms);
					found = true;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// DateTo provided
		if (slots.get("DateTo") != null
				&& slots.get("DateTo").get("value") != null) {
			String dateTos = slots.get("DateTo").get("value").toString();
			if (dateTos != null) {
				try {
					to = df.parse(dateTos);
					found = true;
				} catch (ParseException e) {
					log.warn("failed", e);
					e.printStackTrace();
				}
			}
		}

		return found;
	}

	@Override
	public AlexaCustomResponse handleLaunchIntent(AlexaCustomRequest request) {
		return handleFilterIntent(request);
	}

	private AlexaCustomResponse handleHelpIntent(AlexaCustomRequest request) {
		return new AlexaCustomResponse(
				"Um die Auswahl an Zertifikaten einzuschränken, stelle mir bitte Fragen, wie z.B."
						+ "Alexa, frage HSBC Zertifikate auf Basis DAX."
						+ " Danach kannst du die Auswahl weiter einschränken z.B. mit "
						+ "Alexa, frage HSBC Zertifikate, zeige nur Typ Put oder "
						+ "Alexa, frage HSBC Zertifikate, zeige nur Typ Put oder ");
	}

	private AlexaCustomResponse handleStopIntent(AlexaCustomRequest request) {
		name = null;
		type = null;
		from = null;
		to = null;
		return new AlexaCustomResponse(
				"Ich wünsche dir noch einen schönen Tag.");
	}

	private AlexaCustomResponse handleBuyIntent(AlexaCustomRequest request) {
		if (filteredCertificates.size() == 0) {
			AlexaCustomResponse a = new AlexaCustomResponse(
					"Es sind keine Optionen in der Auswahl.");

			return a;
		} else {
			AlexaCustomResponse a = new AlexaCustomResponse(
					"Es stehen insgesamt " + filteredCertificates.size()
							+ " Optionen zur Auswahl.");
			a.setRepromt("Soll ich eine geeignete Option auswählen?");
			a.shouldEndSession(false);
			return a;
		}
	}

	private AlexaCustomResponse handleBuyYesNoConfirmIntent(
			AlexaCustomRequest request) {
		// soll ich kaufen?
		Map<String, Map<String, Object>> slots = request.request.intent.slots;
		if (slots.get("YesNo") != null) {
			String yesNo = slots.get("YesNo").get("value").toString();
			log.info("found " + yesNo);

			if ("Ja".equalsIgnoreCase(yesNo)) {
				certificateToBuy = filteredCertificates
						.get(new Random().nextInt(filteredCertificates.size()));

				AlexaCustomResponse a = new AlexaCustomResponse("Ich habe eine "
						+ certificateToBuy.type + " Option mit der WKN "
						+ certificateToBuy.wkn + " auf Basis "
						+ certificateToBuy.base + " zum Basispreis "
						+ certificateToBuy.price + "Euro ausgewählt. "
						+ "Wieviele Optionen willst du kaufen?");
				a.setRepromt("Wieviele Optionen willst du kaufen?");
				a.shouldEndSession(false);

				return a;
			} else if ("Nein".equalsIgnoreCase(yesNo)) {
				certificateToBuy = null;
				return new AlexaCustomResponse(
						"Na, dann halt nicht. Bis zum nächsten Mal.");
			} else {
				return new AlexaCustomResponse(
						"Sorry, ich habe dich nicht verstanden.");
			}
		}

		return new AlexaCustomResponse("Sorry, das habe ich nicht verstanden.");
	}

	private AlexaCustomResponse handleBuyCountIntent(
			AlexaCustomRequest request) {
		Map<String, Map<String, Object>> slots = request.request.intent.slots;
		if (slots.get("Count") != null) {
			Object cnt = slots.get("Count").get("value");

			this.from = null;
			this.to = null;
			this.name = null;
			this.type = null;

			NumberFormat df = DecimalFormat.getInstance(Locale.GERMANY);
			return new AlexaCustomResponse(cnt + " Optionen gekauft.",
					"Die Order über " + cnt + " " + certificateToBuy.type
							+ " Optionen mit der WKN " + certificateToBuy.wkn
							+ " auf Basis " + certificateToBuy.base
							+ " zum Basispreis "
							+ df.format(certificateToBuy.price)
							+ "Euro wurde ausgeführt. ");
		}

		return new AlexaCustomResponse("Sorry, das habe ich nicht verstanden.");
	}

	private AlexaCustomResponse handlePriceIntent(
			AlexaCustomRequest request) {
		Map<String, Map<String, Object>> slots = request.request.intent.slots;
		if (slots.get("Basiswert") != null) {
			String cnt = slots.get("Basiswert").get("value").toString();
			log.info("found cnt=" + cnt);

			try {
			Object resolutions = slots.get("Basiswert").get("resolutions");
			log.info("***" + resolutions.getClass() + ";" + resolutions);
			if (resolutions instanceof Map) {
				Object res1 = ((Map) resolutions).get("resolutionsPerAuthority");
				log.info("***" + res1.getClass() + ";" + res1);
				List cnl = (List) res1;
				Object vals = ((Map) cnl.get(0)).get("values");
				if (vals instanceof List) {
					List l = (List) vals;
					Map m1 = (Map) l.get(0);
					String cntxx = (String) ((Map) m1.get("value")).get("name");
					if (cntxx != null && !cntxx.equalsIgnoreCase(cnt)) {
						log.warn("set new value " + cntxx);
						cnt = cntxx;
					}
				}
			}
			} catch (Exception e) {
				log.warn("failed", e);
			}

			AlexaCustomResponse res = null;
			if (cnt == null) {
				res =  new AlexaCustomResponse("Sorry, das habe ich nicht verstanden.");
			} else if (cnt.equalsIgnoreCase("dax")) {
				res =  new AlexaCustomResponse("Der " + cnt + " steht aktuell bei 12804 Punkten.");
				res.response.card = new HashMap<>();
				res.response.card.put("type", "Standard");
				res.response.card.put("title", "Intraday DAX");
				res.response.card.put("text", "Intraday-Chart 16.05.2017 17:53:52\n"
						+ "Kurs: 12804,53\n"
						+ "MAX: 12841,66\n"
						+ "MIN: 12776,02");

				Map<String, String> image = new HashMap<>();
				res.response.card.put("image", image);
				image.put("largeImageUrl", "https://luitest123.appspot.com/images/dax2.png");
				//image.put("smallImageUrl", "https://luitest123.appspot.com/images/daxs.png");
			} else if (cnt.equalsIgnoreCase("tec dax")) {
				res =  new AlexaCustomResponse("Der " + cnt + " steht aktuell bei 2231 Punkten.");
				res.response.card = new HashMap<>();
				res.response.card.put("type", "Standard");
				res.response.card.put("title", "Intraday TecDAX");
				res.response.card.put("text", "Intraday Verlauf des TecDAX");

				Map<String, String> image = new HashMap<>();
				res.response.card.put("image", image);
				image.put("largeImageUrl", "https://luitest123.appspot.com/images/mdax.png");
				image.put("smallImageUrl", "https://luitest123.appspot.com/images/mdaxs.png");
			} else if (cnt.equalsIgnoreCase("s dax")) {
				res =  new AlexaCustomResponse("Der " + cnt + " steht aktuell bei 11022 Punkten.");
				res.response.card = new HashMap<>();
				res.response.card.put("type", "Standard");
				res.response.card.put("title", "Intraday SDAX");
				res.response.card.put("text", "Intraday Verlauf des SDAX");

				Map<String, String> image = new HashMap<>();
				res.response.card.put("image", image);
				image.put("largeImageUrl", "https://luitest123.appspot.com/images/mdax.png");
				image.put("smallImageUrl", "https://luitest123.appspot.com/images/mdaxs.png");
			} else if (cnt.equalsIgnoreCase("m dax")) {
				res =  new AlexaCustomResponse("Der " + cnt + " steht aktuell bei 25102 Punkten.");
				res.response.card = new HashMap<>();
				res.response.card.put("type", "Standard");
				res.response.card.put("title", "Intraday MDAX");
				res.response.card.put("text", "Intraday Verlauf des MDAX");

				Map<String, String> image = new HashMap<>();
				res.response.card.put("image", image);
				image.put("largeImageUrl", "https://luitest123.appspot.com/images/mdax.png");
				image.put("smallImageUrl", "https://luitest123.appspot.com/images/mdaxs.png");
			} else {

				res =  new AlexaCustomResponse("Sorry, ich kenne "+ cnt + " nicht.");
			}
			return res;
		}

		return new AlexaCustomResponse("Sorry, das habe ich nicht verstanden.");
	}
}
