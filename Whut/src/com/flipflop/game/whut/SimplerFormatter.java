package com.flipflop.game.whut;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SimplerFormatter extends Formatter {
	private static final DateFormat df = new SimpleDateFormat("h:mm:ss:S");
	@Override
	public String format(LogRecord record) {
		return df.format(new Date(record.getMillis())) + " - " + record.getMessage() + "\n";
	}

}
