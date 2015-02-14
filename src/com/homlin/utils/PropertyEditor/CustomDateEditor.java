package com.homlin.utils.PropertyEditor;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.util.StringUtils;

public class CustomDateEditor extends PropertyEditorSupport {
	private final String[] parsePatterns;
	private final boolean allowEmpty;
	private final int exactDateLength;

	public CustomDateEditor(String[] parsePatterns, boolean allowEmpty) {
		this.parsePatterns = parsePatterns;
		this.allowEmpty = allowEmpty;
		this.exactDateLength = -1;
	}

	public CustomDateEditor(String[] parsePatterns, boolean allowEmpty, int exactDateLength) {
		this.parsePatterns = parsePatterns;
		this.allowEmpty = allowEmpty;
		this.exactDateLength = exactDateLength;
	}

	public void setAsText(String text) throws IllegalArgumentException {
		if ((this.allowEmpty) && (!StringUtils.hasText(text))) {
			setValue(null);
		} else {
			if ((text != null) && (this.exactDateLength >= 0) && (text.length() != this.exactDateLength)) {
				throw new IllegalArgumentException("Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
			}

			try {
				setValue(DateUtils.parseDateStrictly(text, parsePatterns));
			} catch (ParseException ex) {
				throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
			}
		}
	}

	// public String getAsText() {
	// Date value = (Date) getValue();
	// if (value != null) {
	// for (int i = 0; i < parsePatterns.length; i++) {
	// try {
	// String pattern = parsePatterns[i];
	// SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
	// return dateFormat.format(value);
	// } catch (Exception e) {
	// }
	// }
	// }
	// return "";
	// }
}