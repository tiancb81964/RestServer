package com.asiainfo.ocmanager.utils;

import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * To parse quota string
 * @author Ethan
 *
 */
public class QuotaParser {

	public static Table<String, String, Number> parse(String quotaString){
		Table<String, String, Number> table = HashBasedTable.create();
		JsonElement quota = new JsonParser().parse(quotaString);
		Set<Entry<String, JsonElement>> quotas = quota.getAsJsonObject().entrySet();
		quotas.forEach(e -> {
			Set<Entry<String, JsonElement>> keys = e.getValue().getAsJsonObject().entrySet();
			keys.forEach(k -> {
				table.put(e.getKey(), k.getKey(), k.getValue().getAsNumber());
			});
		});
		return table;
	}
	
	public static String toString(Table<String, String, Number> quotas) {
		JsonObject out = new JsonObject();
		quotas.rowMap().entrySet().forEach(e -> {
			JsonObject jsonobj = new JsonObject();
			e.getValue().forEach((k,v) -> {
				jsonobj.addProperty(k, v);
			});
			out.add(e.getKey(), jsonobj);
		});
		return out.toString();
	}
	
}
