package com.scheible.es2020parser.util.module;

import com.scheible.es2020parser.util.SourcePosition;
import com.scheible.es2020parser.JavaScriptParser;
import com.scheible.es2020parser.JavaScriptParserBaseListener;
import static java.util.Collections.unmodifiableSet;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.Token;

/**
 * Uses <code>JavaScriptParser</code> to get the raw module names of all <code>import</code> statements (including
 * dynmaic imports).
 *
 * @author sj
 */
public class ImportParseListener extends JavaScriptParserBaseListener {

	private final Set<ModuleSpecifier> moduleNames = new HashSet<>();

	@Override
	public void exitImportExpression(JavaScriptParser.ImportExpressionContext ctx) {
		moduleNames.add(extract(ctx.singleExpression().getStart(), true));
	}

	@Override
	public void exitImportStatement(JavaScriptParser.ImportStatementContext ctx) {
		moduleNames.add(extract(ctx.importFromBlock().importFrom().StringLiteral().getSymbol(), false));
	}

	private ModuleSpecifier extract(final Token token, final boolean dynamic) {
		final String text = token.getText();
		return new ModuleSpecifier(text.replaceAll(Pattern.quote("\""), "").replaceAll(Pattern.quote("'"), ""),
				new SourcePosition(token.getLine(), token.getCharPositionInLine() + 1,
						token.getCharPositionInLine() + text.length() - 2), dynamic);
	}

	public Set<ModuleSpecifier> getModuleSpecifiers() {
		return unmodifiableSet(moduleNames);
	}
}
