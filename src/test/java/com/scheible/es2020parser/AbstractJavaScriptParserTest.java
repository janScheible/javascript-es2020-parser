package com.scheible.es2020parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 *
 * @author sj
 */
public abstract class AbstractJavaScriptParserTest {

	private static final int INPUT_BUFFER_LENGTH = 8192;

	protected JavaScriptParser createParser(final Class<?> testClass) {
		final String javaScriptName = testClass.getSimpleName()
				.substring(0, testClass.getSimpleName().length() - "Test".length()) + ".js";

		try (InputStream inputStream = getClass().getResourceAsStream(javaScriptName)) {
			final String javaScriptSource = new String(readAllBytes(inputStream), StandardCharsets.UTF_8);
			return createParser(javaScriptSource);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	private static byte[] readAllBytes(final InputStream inputStream) throws IOException {
		final byte[] buffer = new byte[INPUT_BUFFER_LENGTH];
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int numberOfReadBytes;

		while ((numberOfReadBytes = inputStream.read(buffer, 0, INPUT_BUFFER_LENGTH)) != -1) {
			outputStream.write(buffer, 0, numberOfReadBytes);
		}

		return outputStream.toByteArray();
	}

	protected JavaScriptParser createParser(final String javaScriptSource) {
		final JavaScriptLexer lexer = new JavaScriptLexer(CharStreams.fromString(javaScriptSource));
		lexer.setUseStrictDefault(true);

		final JavaScriptParser parser = new JavaScriptParser(new CommonTokenStream(lexer));

		return parser;
	}
}
