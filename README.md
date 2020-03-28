# javascript-es2020-parser

ANTLR v4 parser based on https://github.com/antlr/grammars-v4/tree/master/javascript/javascript (MIT license).
Revision f5a2ff3e8fc570a7d5d282842c96e3ca5d93afd3 was used.
The grammar follows a rather pragmatic approach in terms of ECMAScript compatibility.
es2020 compatibility should be more or less given.

Unit tests only for `import` keyword for now. More will perhaps follow later.

Usage of the parser:

```java
final JavaScriptLexer lexer = new JavaScriptLexer(CharStreams.fromString("class MyClass { }"));
final JavaScriptParser parser = new JavaScriptParser(new CommonTokenStream(lexer));

parser.addParseListener(new JavaScriptParserBaseListener() {
	@Override
	public void exitClassDeclaration(ClassDeclarationContext ctx) {
		assertThat(ctx.identifier().getText()).isEqualTo("MyClass");
	}
});
parser.program();
```

Maven artifact (is also available via [![](https://jitpack.io/v/janScheible/javascript-es2020-parser.svg)](https://jitpack.io/#janScheible/javascript-es2020-parser)):

```xml
<dependency>
	<groupId>com.scheible</groupId>
	<artifactId>javascript-es2020-parser</artifactId>
	<version>0.1.1</version>
</dependency>
```

Java >= 8 is required (was tested with Java 8 and 11).
