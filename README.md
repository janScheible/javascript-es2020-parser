# javascript-es2020-parser

ANTLR v4 parser based on https://github.com/antlr/grammars-v4/tree/master/javascript/javascript (MIT license).
Revision f5a2ff3e8fc570a7d5d282842c96e3ca5d93afd3 was used.
The grammar follows a rather pragmatic approach in terms of ECMAScript compatibility.
es2020 compatibility should be more or less given.

Unit tests only for `import` keyword and class fields for now. More will perhaps follow later.

## Usage of the parser

Either with direct instantiation

```java
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
```

or via one of the convenience methods of `ListeningJavaScriptParser` like

```java
ListeningJavaScriptParser.parse("class MyClass { }", new ErrorAwareJavaScriptParserBaseListener() {
	@Override
	public void exitClassDeclaration(ClassDeclarationContext ctx) {
		assertThat(ctx.identifier().getText()).isEqualTo("MyClass");
	}
	
	@Override
	public void syntaxError(String description) {
		System.out.println(description);
	}
});
```

together with an implementation of a `JavaScriptParserBaseListener` or `ErrorAwareJavaScriptParserBaseListener`.

### Module specifier support

Starting with 0.3.0 there is explicit support of module specifiers extracted from the JavaScript sources (static and dynamic imports).
The `ImportParseListener` can be used together with the `ListeningJavaScriptParser`.
The `ModuleSpecifier` class allows for example to strip off the file extension or to resolve the module specifier to a `Path` or `URI`.

Furthermore the class `JavaScriptTransformer` can be used to transform the returned module specifier.

## Maven artifact

Maven artifact (is also available via [![](https://jitpack.io/v/janScheible/javascript-es2020-parser.svg)](https://jitpack.io/#janScheible/javascript-es2020-parser)):

```xml
<dependency>
	<groupId>com.scheible</groupId>
	<artifactId>javascript-es2020-parser</artifactId>
	<version>0.5.0</version>
</dependency>
```

Java >= 8 is required (was tested with Java 8 and 11).
