package com.scheible.es2020parser.util.module;

import com.scheible.es2020parser.util.SourcePosition;
import com.scheible.es2020parser.util.ListeningJavaScriptParser;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class ImportParseListenerTest {

	@Test
	public void testImportModuleSpecifierExtraction() {
		final Set<ModuleSpecifier> moduleNames = ListeningJavaScriptParser.parse(""
				+ "import * as math from \"lib/mathwildcard\"\n"
				+ "\n"
				+ "import('runtime_import');",
				new ImportParseListener()).getModuleSpecifiers();

		assertThat(moduleNames).containsOnly(
				new ModuleSpecifier("lib/mathwildcard", new SourcePosition(1, 23, 38), false),
				new ModuleSpecifier("runtime_import", new SourcePosition(3, 8, 21), true));
	}
}
