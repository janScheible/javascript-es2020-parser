package com.scheible.es2020parser;

import com.scheible.es2020parser.JavaScriptParser.ClassDeclarationContext;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class JavaScriptParserTest {

	@Test
	public void basicParseTest() {
		final JavaScriptLexer lexer = new JavaScriptLexer(CharStreams.fromString("class MyClass { }"));
		lexer.setUseStrictDefault(true);
		final JavaScriptParser parser = new JavaScriptParser(new CommonTokenStream(lexer));

		parser.addParseListener(new JavaScriptParserBaseListener() {
			@Override
			public void exitClassDeclaration(ClassDeclarationContext ctx) {
				assertThat(ctx.identifier().getText()).isEqualTo("MyClass");
			}
		});
		parser.program();
	}
}
