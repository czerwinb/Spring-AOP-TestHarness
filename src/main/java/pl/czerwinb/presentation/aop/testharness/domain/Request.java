package pl.czerwinb.presentation.aop.testharness.domain;

import com.google.common.base.Joiner;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;

public final class Request implements Serializable {

	private static final long serialVersionUID = 1L;

	private final UUID userId;
	private final Map<String, String> properties = new HashMap<>();

	private Request(UUID userId) {
		this.userId = userId;
	}

	public static Request forUserId(String userId) {
		return new Request(UUID.fromString(userId));
	}

	public UUID getUserId() {
		return userId;
	}

	public void setProperty(NamedProperty property, String value) {
		setProperty(property.toString(), value);
	}

	private void setProperty(String name, String value) {
		properties.put(name, value);
	}

	@Override
	public String toString() {
		Joiner.MapJoiner joiner = Joiner.on(", ").withKeyValueSeparator(":");
		return format("REQUEST %s: %s", userId, joiner.join(properties));
	}

	public enum NamedProperty {
		USER_EMAIL
	}
}
