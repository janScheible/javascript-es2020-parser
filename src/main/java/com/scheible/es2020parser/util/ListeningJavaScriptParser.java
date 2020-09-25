package com.scheible.es2020parser.util;

import com.scheible.es2020parser.JavaScriptLexer;
import com.scheible.es2020parser.JavaScriptParser;
import com.scheible.es2020parser.JavaScriptParserListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import static java.nio.charset.StandardCharsets.UTF_8;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 *
 * @author sj
 */
public class ListeningJavaScriptParser {

	private static void parse(final CharStream charStream, final JavaScriptParserListener... listeners) {
		final JavaScriptLexer lexer = new JavaScriptLexer(charStream);
		lexer.setUseStrictDefault(true);

		final JavaScriptParser parser = new JavaScriptParser(new CommonTokenStream(lexer));
		for (final JavaScriptParserListener listener : listeners) {
			parser.addParseListener(listener);
			
			if(listener instanceof ErrorAwareJavaScriptParserListener) {
				parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
				
				parser.addErrorListener(new BaseErrorListener() {
					@Override
					public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol, 
							final int line, final int charPositionInLine, final String msg, final RecognitionException e) {
						((ErrorAwareJavaScriptParserListener)listener).syntaxError("at " + line + ":" 
								+ charPositionInLine + " " + msg);
					}
				});
			}
		}

		parser.program();
	}

	public static void parse(final InputStream inputStream, final JavaScriptParserListener... listeners) {
		try {
			parse(CharStreams.fromStream(inputStream, UTF_8), listeners);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public static <T extends JavaScriptParserListener> T parse(final InputStream inputStream, final T listener) {
		parse(inputStream, new JavaScriptParserListener[]{listener});
		return listener;
	}

	public static void parse(final String javaScriptSource, final JavaScriptParserListener... listeners) {
		parse(CharStreams.fromString(javaScriptSource), listeners);
	}

	public static <T extends JavaScriptParserListener> T parse(final String javaScriptSource, final T listener) {
		parse(javaScriptSource, new JavaScriptParserListener[]{listener});
		return listener;
	}
}
