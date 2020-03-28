package com.scheible.es2020parser;

import java.io.IOException;
import java.io.UncheckedIOException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 *
 * @author sj
 */
public abstract class AbstractJavaScriptParserTest {

	protected JavaScriptParser createParser(final Class<?> testClass) {
		try {
			final String javaScriptName = testClass.getSimpleName()
					.substring(0, testClass.getSimpleName().length() - "Test".length()) + ".js";
			final String javaScriptSource = new String(getClass().getResourceAsStream(javaScriptName).readAllBytes());

			return createParser(javaScriptSource);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	protected JavaScriptParser createParser(final String javaScriptSource) {
		final JavaScriptLexer lexer = new JavaScriptLexer(CharStreams.fromString(javaScriptSource));
		final JavaScriptParser parser = new JavaScriptParser(new CommonTokenStream(lexer));
		
		return parser;
	}
}
