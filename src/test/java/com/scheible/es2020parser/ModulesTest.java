package com.scheible.es2020parser;

import com.scheible.es2020parser.JavaScriptParser.ImportExpressionContext;
import com.scheible.es2020parser.JavaScriptParser.ImportStatementContext;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class ModulesTest extends AbstractJavaScriptParserTest {

	@Test
	public void testImportedFiles() throws IOException {
		final JavaScriptParser parser = createParser(getClass());

		final Set<String> importFiles = new HashSet<>();

		parser.addParseListener(new JavaScriptParserBaseListener() {
			@Override
			public void exitImportExpression(ImportExpressionContext ctx) {
				importFiles.add(replaceStringQuotes(ctx.singleExpression().getText()));
			}

			@Override
			public void exitImportStatement(ImportStatementContext ctx) {
				importFiles.add(replaceStringQuotes(ctx.importFromBlock().importFrom().StringLiteral().getText()));
			}

			private String replaceStringQuotes(final String input) {
				return input.replaceAll(Pattern.quote("\""), "").replaceAll(Pattern.quote("'"), "");
			}
		});
		parser.program();

		assertThat(importFiles).containsOnly("lib/mathwildcard", "lib/mathmultiple", "lib/mathplusplus", "ccc",
				"runtime_import", "react", "lib/mathmultiline");
	}
}
