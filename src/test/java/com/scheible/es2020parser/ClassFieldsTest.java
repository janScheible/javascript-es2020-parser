package com.scheible.es2020parser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class ClassFieldsTest extends AbstractJavaScriptParserTest {

	static class ClassField {

		final String name;
		final boolean isStatic;
		final boolean isPrivate;

		public ClassField(final String name, final boolean isStatic, final boolean isPrivate) {
			this.name = name;
			this.isStatic = isStatic;
			this.isPrivate = isPrivate;
		}

		@Override
		public int hashCode() {
			return Objects.hash(name, isStatic);
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == this) {
				return true;
			} else if (obj != null && getClass().equals(obj.getClass())) {
				final ClassField other = (ClassField) obj;
				return Objects.equals(name, other.name) && Objects.equals(isStatic, other.isStatic)
						&& Objects.equals(isPrivate, other.isPrivate);
			} else {
				return false;
			}
		}

		@Override
		public String toString() {
			return (isStatic ? "static " : "") + (isPrivate ? "#" : "") + name;
		}
	}

	@Test
	public void testImportedFiles() throws IOException {
		final JavaScriptParser parser = createParser(getClass());

		final Set<ClassField> classFields = new HashSet<>();

		parser.addParseListener(new JavaScriptParserBaseListener() {
			@Override
			public void exitClassElement(JavaScriptParser.ClassElementContext ctx) {
				if (ctx.propertyName() != null) {
					final String name = ctx.propertyName().identifierName().getText();
					final boolean isStatic = !ctx.Static().isEmpty();
					final boolean isPrivate = ctx.Hashtag() != null;
					classFields.add(new ClassField(name, isStatic, isPrivate));
				}
			}
		});
		parser.program();

		assertThat(classFields).containsOnly(new ClassField("NAME", true, false), new ClassField("VALUE", false, false),
				new ClassField("PRIVATE_NAME", true, true), new ClassField("PRIVATE_VALUE", false, true));
	}
}
