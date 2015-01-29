package pl.czerwinb.presentation.aop.testharness.services;

import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;

import java.util.UUID;

import static org.apache.directory.api.ldap.model.message.SearchScope.SUBTREE;

public class LdapUsersCatalog implements UsersCatalog {

	private static final String SEARCH_DN = "ou=catalog,dc=pl,dc=czerwinb";
	private static final String SEARCH_FILTER = "(&(objectclass=email)(objectcategory=person))";
	private final LdapConnection connection;

	public LdapUsersCatalog(String serverAddress, int serverPort) {
		connection = new LdapNetworkConnection(serverAddress, serverPort);
	}

	public void connect(String nameDn, String credentials) {
		try {
			connection.bind(nameDn, credentials);
		} catch (LdapException e) {
			throw new RuntimeException(e);
		}
	}

	public void disconnect() {
		try {
			connection.unBind();
		} catch (LdapException e) {
			throw new RuntimeException();
		}
	}

	@Override
	public String getUserEmail(UUID userId) {
		String userEmail = null;
		try {
			EntryCursor cursor = connection.search(SEARCH_DN, SEARCH_FILTER, SUBTREE, userId.toString());
			cursor.next();
			throwAnExceptionIfMoreThanOneEntriesFound(cursor);
			userEmail = cursor.get().toString();
		} catch (LdapException | CursorException e) {
			throw new RuntimeException();
		}
		return userEmail;
	}

	private static void throwAnExceptionIfMoreThanOneEntriesFound(EntryCursor cursor) {
		if (cursor.available() && !cursor.isLast()) {
			throw new RuntimeException("More than one entries found");
		}
	}
}
