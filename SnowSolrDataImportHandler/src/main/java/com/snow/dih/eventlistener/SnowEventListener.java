package com.snow.dih.eventlistener;

import java.lang.invoke.MethodHandles;

import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.handler.dataimport.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnowEventListener implements EventListener {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public void onEvent(Context ctx) {
		LOG.info("inside event listener");
		LOG.info(ctx.getStats().toString());

	}

}
