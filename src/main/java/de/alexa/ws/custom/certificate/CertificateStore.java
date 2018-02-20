package de.alexa.ws.custom.certificate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

public class CertificateStore {

	private static final Logger log = Logger
			.getLogger(CertificateStore.class.getName());

	private List<CertificateDTO> certificates;
	private static CertificateStore instance;

	public static CertificateStore getInstance() {
		if (instance == null) {
			instance = new CertificateStore();
		}
		return instance;
	}

	private CertificateStore() {
		certificates = new ArrayList<>();
		InputStream in = getClass().getResourceAsStream("/zertifikate.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			SimpleDateFormat f = new SimpleDateFormat("dd.MM.yy");
			NumberFormat df = NumberFormat.getInstance(Locale.GERMAN);
			int cnt = 0;
			while ((line = reader.readLine()) != null) {
				cnt++;
				if (cnt == 1) {
					continue;
				}
				String[] a = line.split(";");
				CertificateDTO c = new CertificateDTO();
				int i = 0;
				c.base = a[i++];
				c.wkn = a[i++];
				i++;
				c.type = a[i++];
				c.date = f.parse(a[i++]);
				c.price = df.parse(a[i++]).floatValue();
				certificates.add(c);
			}
		} catch (Exception e) {
			log.warn("failed", e);
			e.printStackTrace();
		}
		log.info("found " + certificates.size() + " certificates");
	}

	public List<CertificateDTO> getAll() {
		return certificates;
	}

	public List<CertificateDTO> getFilter(String name, String type, Date from,
			Date to) {
		log.info("getFilter(" + name + ", " + type + ", " + from + ", " + to
				+ ")");
		List<CertificateDTO> result = new ArrayList<>();
		for (CertificateDTO c : certificates) {
			if (name != null && !c.base.equalsIgnoreCase(name)) {
				continue;
			}
			if (type != null && !c.type.equalsIgnoreCase(type)) {
				continue;
			}
			if (from != null && !from.before(c.date)) {
				continue;
			}
			if (to != null && !to.after(c.date)) {
				continue;
			}
			result.add(c);
		}
		return result;
	}

	public static class CertificateDTO {
		public String base, wkn;
		public String type;
		public Date date;
		public float price;
	}
}
