package com.scheible.es2020parser.util;

import com.scheible.es2020parser.JavaScriptParserBaseListener;

/**
 *
 * @author sj
 */
public class ErrorAwareJavaScriptParserBaseListener extends JavaScriptParserBaseListener implements ErrorAwareJavaScriptParserListener {

	@Override
	public void syntaxError(String description) {
	}	
}
