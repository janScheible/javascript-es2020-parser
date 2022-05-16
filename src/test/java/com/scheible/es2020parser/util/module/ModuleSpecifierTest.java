package com.scheible.es2020parser.util.module;

import com.scheible.es2020parser.util.SourcePosition;
import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class ModuleSpecifierTest {

	@Test
	public void testHasExtension() {
		assertThat(specifier("./test").hasExtension()).isFalse();
		assertThat(specifier("~/test/foo").hasExtension()).isFalse();

		assertThat(specifier("./test.js").hasExtension()).isTrue();
		assertThat(specifier("../test/foo.js").hasExtension()).isTrue();
		assertThat(specifier("~/test/foo.js").hasExtension()).isTrue();
	}

	@Test
	public void testValueWithoutExtension() {
		assertThat(specifier("./test").getValueWithoutExtension()).isEqualTo("./test");
		assertThat(specifier("./test.js").getValueWithoutExtension()).isEqualTo("./test");
	}

	@Test
	public void testResolveUri() {
		assertThat(specifier("~/page/page.js").resolve(URI.create("/"), URI.create("/src/app.js")))
				.isEqualTo(URI.create("/page/page"));
		assertThat(specifier("~/page/page.js").resolve(URI.create("/src/"), URI.create("/src/app.js")))
				.isEqualTo(URI.create("/src/page/page"));
		assertThat(specifier("./page.js").resolve(URI.create("/src/"), URI.create("/src/app.js")))
				.isEqualTo(URI.create("/src/page"));
		assertThat(specifier("/page/page.js").resolve(URI.create("/"), URI.create("/src/app.js")))
				.isEqualTo(URI.create("/page/page"));
		assertThat(specifier("/page/page.js").resolve(URI.create("/src/"), URI.create("/src/app.js")))
				.isEqualTo(URI.create("/page/page"));
	}

	@Test
	public void testResolvePath() {
		final String prefix = File.separatorChar == '/' ? "/" : "c:/";
		final String filePrefix = File.separatorChar == '/' ? "file:/" : "file:/c:/";

		assertThat(specifier("./page.js").resolve(Paths.get(prefix + "project/src"), Paths.get(prefix + "project/src/app.js")))
				.isEqualTo(Paths.get(prefix + "project/src/page"));
		assertThat(specifier("./page.js").resolve(Paths.get(prefix + "project/src"), Paths.get("./app.js")))
				.isEqualTo(Paths.get(prefix + "project/src/page"));
		assertThat(specifier("~/app.js").resolve(Paths.get(prefix + "project/src"), Paths.get("./page/page.js")))
				.isEqualTo(Paths.get(prefix + "project/src/app"));
		assertThat(specifier(filePrefix + "src/app.js").resolve(Paths.get(prefix + "project/src"), Paths.get("./page/page.js")))
				.isEqualTo(Paths.get(prefix + "src/app"));
	}

	private static ModuleSpecifier specifier(final String specifier) {
		return new ModuleSpecifier(specifier, new SourcePosition(0, 0, 0), false);
	}
}
