package com.asiainfo.ocmanager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author zhaoyim
 *
 */
public class DateTimeUtil {

	private static final Logger LOG = LoggerFactory.getLogger(DateTimeUtil.class);

	public static String formatDateTime(String inDateTime) {

		if (inDateTime.equals("") || inDateTime == null) {
			LOG.debug("DateTimeUtil inDateTime is empty return directly.");
			return null;
		}

		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			inDateTime = inDateTime.replace("Z", " UTC");
			SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
			Date outDateTime = inFormat.parse(inDateTime);
			return outFormat.format(outDateTime);
		} catch (ParseException e) {
			LOG.debug("DateTimeUtil hit exception {}.", e);
			//e.printStackTrace();
		}
		LOG.debug("DateTimeUtil set outDateTime to null.");
		return null;
	}

}

