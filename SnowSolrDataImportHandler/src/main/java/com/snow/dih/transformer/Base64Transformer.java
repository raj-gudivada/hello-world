package com.snow.dih.transformer;

import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.handler.dataimport.DataImporter;
import org.apache.solr.handler.dataimport.Transformer;

public class Base64Transformer extends Transformer {

	@Override
	public Object transformRow(Map<String, Object> row, Context context) {
		List<Map<String, String>> fields = context.getAllEntityFields();
		for (Map<String, String> field : fields) {
			String decode = field.get("decode");
			if ("true".equals(decode)) {
				String columnName = field.get(DataImporter.COLUMN);
				Object value = row.get(columnName);
				row.put(columnName, new String(Base64.decodeBase64(value.toString())));
			}
		}
		return row;
	}
}
