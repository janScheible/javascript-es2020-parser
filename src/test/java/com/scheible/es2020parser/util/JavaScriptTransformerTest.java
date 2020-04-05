package com.scheible.es2020parser.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Supplier;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static com.scheible.es2020parser.util.JavaScriptTransformer.transform;


/**
 *
 * @author sj
 */
public class JavaScriptTransformerTest {
	
	@Test
	public void testReplacePositionables() throws IOException {
		final Supplier<InputStream> inputStreamSupplier = () -> new ByteArrayInputStream(
				("" + "// comment\n" + "import * from 'test';\n" + "const value = 42;").getBytes(UTF_8));

		final Positionable positionable = mock(Positionable.class);
		doReturn(new SourcePosition(2, 15, 18)).when(positionable).getPosition();

		assertThat(transform(inputStreamSupplier,
				javaScriptSource -> new HashSet<>(Arrays.asList(positionable)), moduleSpecifier -> ":-)"))
						.contains("import * from ':-)';");
	}	
}
