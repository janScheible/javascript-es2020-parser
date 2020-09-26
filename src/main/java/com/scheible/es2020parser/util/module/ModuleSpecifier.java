package com.scheible.es2020parser.util.module;

import com.scheible.es2020parser.util.Positionable;
import com.scheible.es2020parser.util.SourcePosition;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Module specifier extracted from JavaScript source with position and type.
 *
 * @author sj
 */
public class ModuleSpecifier implements Positionable {

	private final String value;
	private final SourcePosition position;
	private final boolean isDynamic;

	public ModuleSpecifier(final String value, final SourcePosition position, final boolean isDynamic) {
		this.value = value;
		this.position = position;
		this.isDynamic = isDynamic;
	}

	public String getValue() {
		return value;
	}

	public String getValueWithoutExtension() {
		return hasExtension() ? value.substring(0, value.lastIndexOf('.')) : value;
	}

	@Override
	public SourcePosition getPosition() {
		return position;
	}

	public boolean isDynamic() {
		return isDynamic;
	}

	public boolean hasExtension() {
		return value.lastIndexOf(".") > value.lastIndexOf("/");
	}
	
	public boolean isSourceRootRelative() {
		return value.startsWith("~");
	}
	
	public boolean isRelative() {
		return value.startsWith(".");
	}
	
	public Path resolve(final Path sourceRootDir, final Path sourceJavaScriptFile) {
		if (!sourceRootDir.isAbsolute()) {
			throw new IllegalArgumentException(String.format("The source root dir '%s' has to be absolute!", 
					sourceRootDir.toString()));
		}

		final URI sourceJavaScriptFileUri = (sourceJavaScriptFile.isAbsolute() 
				? sourceJavaScriptFile : sourceRootDir.resolve(sourceJavaScriptFile)).toUri();
		return Paths.get(resolve(URI.create(sourceRootDir.toUri() + "/"), sourceJavaScriptFileUri));
	}

	public URI resolve(final URI sourceRootDir, final URI sourceJavaScriptFile) {
		if (isSourceRootRelative()) {
			return sourceRootDir.resolve("." + getValueWithoutExtension().substring(1)).normalize();
		} else if (isRelative()) {
			return sourceJavaScriptFile.resolve(".").normalize().resolve(getValueWithoutExtension()).normalize();
		}

		return URI.create(getValueWithoutExtension());
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, position, isDynamic);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj != null && getClass().equals(obj.getClass())) {
			final ModuleSpecifier other = (ModuleSpecifier) obj;
			return Objects.equals(value, other.value) && Objects.equals(position, other.position)
					&& Objects.equals(isDynamic, other.isDynamic);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return (isDynamic ? "dynamic " : "") + "'" + value + "' at " + position;
	}
}
