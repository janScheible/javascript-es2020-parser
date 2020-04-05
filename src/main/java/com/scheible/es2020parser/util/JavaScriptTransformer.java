package com.scheible.es2020parser.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author sj
 */
public class JavaScriptTransformer {

	/**
	 * Allows to transform all passed <code>Positionable</code> instances in the JavaScript source retrieved from 
	 * the <code>InputStream</code>.
	 */
	public static <T extends Positionable> String transform(final Supplier<InputStream> inputStreamSupplier,
			final Function<String, Set<T>> positionableSupplier, final Function<T, String> transformerFunction)
			throws IOException {
		final StringBuilder javaScriptSourceBuilder = new StringBuilder();
		String line;
		int lineNumber = 0;

		final List<Integer> lineLengths = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStreamSupplier.get(), UTF_8))) {
			while ((line = reader.readLine()) != null) {
				lineNumber++;
				lineLengths.add(line.length());
				javaScriptSourceBuilder.append(line).append('\n');
			}
		}

		final Map<Integer, List<T>> lineNumberSourcePositionMapping = new HashMap<>();
		for (final T positionable : positionableSupplier.apply(javaScriptSourceBuilder.toString())) {
			lineNumberSourcePositionMapping
					.computeIfAbsent(positionable.getPosition().getLine(), key -> new ArrayList<>()).add(positionable);
		}

		int position = javaScriptSourceBuilder.length();
		for (int i = lineLengths.size() - 1; i >= 0; i--) {
			final List<T> linePositionables = lineNumberSourcePositionMapping.get(lineNumber);
			if (linePositionables != null) {
				Collections.sort(linePositionables, (first, second) -> Integer.compare(second.getPosition().getStart(),
						first.getPosition().getStart()));

				for (final T positionable : linePositionables) {
					final int start = position - lineLengths.get(i) + positionable.getPosition().getStart() - 1;
					final int end = position - lineLengths.get(i) + positionable.getPosition().getEnd();

					javaScriptSourceBuilder.replace(start, end,
							transformerFunction.apply(positionable));
				}
			}

			position -= lineLengths.get(i) + 1;
			lineNumber--;
		}

		return javaScriptSourceBuilder.toString();
	}
}
