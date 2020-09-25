package com.scheible.es2020parser.util;

import com.scheible.es2020parser.JavaScriptParserListener;

/**
 *
 * @author sj
 */
public interface ErrorAwareJavaScriptParserListener extends JavaScriptParserListener {
	
	void syntaxError(String description);
}
