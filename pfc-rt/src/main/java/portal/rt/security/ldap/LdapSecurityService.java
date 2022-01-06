/*
 * Copyright 2000 - 2004,  Bavo De Ridder
 * 
 * This file is part of Portal Foundation Classes.
 *
 * Portal Foundation Classes is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * Portal Foundation Classes is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Portal Foundation Classes; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston
 * 
 * http://www.gnu.org/licenses/gpl.html
 */

package portal.rt.security.ldap;

import java.security.Principal;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.security.ISecurityService;
import portal.services.registry.BooleanKey;
import portal.services.registry.RegistryException;
import portal.services.registry.StringKey;

/**
 * <p>This class uses the following registry keys and default values:</p>
 * <table border="1" cellspacing="3" cellpadding="2">
 * <tr>
 *   <td><b>Name</b></td>
 *   <td><b>Type</b></td>
 *   <td><b>Default</b></td>
 *   <td><b>Description</b></td>
 * </tr>
 * <tr>
 *   <td>portal.security.sp.ldap.ProviderUrl</td>
 *   <td>String</td>
 *   <td></td>
 *   <td>The provider url to use when connecting to the LDAP directory e.g. <i>ldap://10.0.0.1:389/</i></td>
 * </tr>
 * <tr>
 *   <td>portal.security.sp.ldap.ConnectionName</td>
 *   <td>String</td>
 *   <td></td>
 *   <td>The DN to use when binding to the LDAP directory</td>
 * </tr>
 * <tr>
 *   <td>portal.security.sp.ldap.ConnectionPassword</td>
 *   <td>String</td>
 *   <td></td>
 *   <td>The password to use when binding to the LDAP directory</td>
 * </tr>
 * <tr>
 *   <td>portal.security.sp.ldap.UserBase</td>
 *   <td>String</td>
 *   <td></td>
 *   <td>Where to look for users, e.g. <i>ou=Users,o=ACME</i></td>
 * </tr>
 * <tr>
 *   <td>portal.security.sp.ldap.UserSubtree</td>
 *   <td>Boolean</td>
 *   <td></td>
 *   <td>Indicates if subtree scope (value true) or one level (value false) should be used when searching for users.</td>
 * </tr>
 * <tr>
 *   <td>portal.security.sp.ldap.UserAttribute</td>
 *   <td>String</td>
 *   <td>uid</td>
 *   <td>The attribute of the user object that contains the username</td>
 * </tr>
 * <tr>
 *   <td>portal.security.sp.ldap.RoleBase</td>
 *   <td>String</td>
 *   <td></td>
 *   <td>Where to look for groups, e.g. <i>ou=Groups,o=ACME</i></td>
 * </tr>
 * <tr>
 *   <td>portal.security.sp.ldap.RoleSubtree</td>
 *   <td>Boolean</td>
 *   <td></td>
 *   <td>Indicates if subtree scope (value true) or one level (value false) should be used when searching for groups.</td>
 * </tr>
 * <tr>
 *   <td>portal.security.sp.ldap.RoleNameAttribute</td>
 *   <td>String</td>
 *   <td>cn</td>
 *   <td>The attribute of the group object that contains the group name</td>
 * </tr>
 * <tr>
 *   <td>portal.security.sp.ldap.RoleMemberAttribute</td>
 *   <td>String</td>
 *   <td>memberUid</td>
 *   <td>The attribute of the group object that contains the user members</td>
 * </tr>
 * </table>
 * 
 * @author Bavo De Ridder <http://bavo.coderspotting.org/)
 */
public class LdapSecurityService implements ISecurityService
{
	private static final Log LOGGER = LogFactory.getLog(LdapSecurityService.class);

	private static final String REG_DEFAULT_USER_ATTRIBUTE = "uid";
	private static final String REG_KEY_PROVIDER_URL = "portal.security.sp.ldap.ProviderUrl";
	private static final String REG_KEY_USER_ATTRIBUTE = "portal.security.sp.ldap.UserAttribute";
	private static final String REG_KEY_USER_SUBTREE = "portal.security.sp.ldap.UserSubtree";
	private static final String REG_KEY_USER_BASE = "portal.security.sp.ldap.UserBase";
	private static final String REG_KEY_CONNECTION_PASSWORD = "portal.security.sp.ldap.ConnectionPassword";
	private static final String REG_KEY_CONNECTION_NAME = "portal.security.sp.ldap.ConnectionName";

	/*	
    private static final String REG_DEFAULT_ROLE_MEMBER_ATTRIBUTE = "memberUid";
	private static final String REG_DEFAULT_ROLE_NAME_ATTRIBUTE = "cn";
	private static final String REG_KEY_ROLE_MEMBER_ATTRIBUTE = "portal.security.sp.ldap.RoleMemberAttribute";
	private static final String REG_KEY_ROLE_NAME_ATTRIBUTE = "portal.security.sp.ldap.RoleNameAttribute";
	private static final String REG_KEY_ROLE_SUBTREE = "portal.security.sp.ldap.RoleSubtree";
	private static final String REG_KEY_ROLE_BASE = "portal.security.sp.ldap.RoleBase";
	*/
	
	public LdapSecurityService()
	{
	}

	public boolean verify(String username, String password)
			throws SecurityException
	{
		InitialLdapContext context = null;

		try
		{
			context = getInitialLdapContext(getConnectionName(),
					getConnectionPassword());

			return verifyCredentials(searchUserDN(context, username), password);
		}
		catch (Exception e)
		{
			LOGGER.warn("Could not verify credentials", e);

			throw new SecurityException("Could not verify credentials: "
					+ e.getMessage());
		}
		finally
		{
			closeInitialLdapContext(context);
		}
	}

	public Principal login(String username, String password)
			throws SecurityException
	{
		if (!verify(username, password))
		{
			throw new SecurityException("User not known or wrong password.");
		}

		return new LDAPPrincipal(username);
	}

	private boolean verifyCredentials(String userDN, String password)
	{
		LOGGER.info("Checking password for: " + userDN);

		InitialLdapContext context = null;

		try
		{
			context = getInitialLdapContext(userDN, password);

			return true;
		}
		catch (Exception e)
		{
			LOGGER.error(
					"Could not check user credentials, possibly wrong password",
					e);
		}
		finally
		{
			closeInitialLdapContext(context);
		}

		return false;
	}

	private String searchUserDN(InitialLdapContext context, String username)
			throws NamingException, RegistryException
	{
		SearchControls constraints = new SearchControls();

		if (getUserSubtree())
		{
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
		}
		else
		{
			constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		}

		String filter = "(" + getUserAttribute() + "=" + username + ")";

		constraints.setReturningAttributes(new String[0]);

		NamingEnumeration<SearchResult> results = context.search(getUserBase(), filter,
				constraints);

		if (results == null || !results.hasMore())
		{
			LOGGER.debug("user was not found in directory : " + username);

			throw new NamingException("User was not found in directory : "
					+ username);
		}

		// Get result for the first entry found
		SearchResult result = results.next();

		// Check no further entries were found
		if (results.hasMore())
		{
			LOGGER.debug("multiple entries for username " + username);

			throw new NamingException("Multiple entries for username "
					+ username);
		}

		// Get the entry's distinguished name
		NameParser parser = context.getNameParser("");
		Name contextName = parser.parse(context.getNameInNamespace());
		Name baseName = parser.parse(getUserBase());
		Name entryName = parser.parse(result.getName());
		Name name = contextName.addAll(baseName);
		name = name.addAll(entryName);
		String dn = name.toString();

		return dn;
	}

	private InitialLdapContext getInitialLdapContext(String username,
			String password) throws NamingException, RegistryException
	{
		Properties env = new Properties();

		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, getProviderUrl());

		env.put(Context.SECURITY_PRINCIPAL, username);
		env.put(Context.SECURITY_CREDENTIALS, password);

		LOGGER.debug("Logging into LDAP server, env=" + env);

		return new InitialLdapContext(env, null);
	}

	private void closeInitialLdapContext(InitialContext context)
	{
		try
		{
			if (context != null)
			{
				context.close();
			}
		}
		catch (Exception e)
		{
			// do nothing, we did our best.
		}
	}

	private String getProviderUrl() throws RegistryException
	{
		return StringKey.getValue(REG_KEY_PROVIDER_URL);
	}

	private String getConnectionName() throws RegistryException
	{
		return StringKey.getValue(REG_KEY_CONNECTION_NAME);
	}

	private String getConnectionPassword() throws RegistryException
	{
		return StringKey.getValue(REG_KEY_CONNECTION_PASSWORD);
	}

	private String getUserBase() throws RegistryException
	{
		return StringKey.getValue(REG_KEY_USER_BASE);
	}

	private boolean getUserSubtree() throws RegistryException
	{
		return BooleanKey.getValue(REG_KEY_USER_SUBTREE).booleanValue();
	}

	private String getUserAttribute()
	{
		return StringKey.getValue(REG_KEY_USER_ATTRIBUTE,
				REG_DEFAULT_USER_ATTRIBUTE);
	}
	
	/*
	private String getRoleBase() throws RegistryException
	{
		return StringKey.getValue(REG_KEY_ROLE_BASE);
	}

	private boolean getRoleSubtree() throws RegistryException
	{
		return BooleanKey.getValue(REG_KEY_ROLE_SUBTREE).booleanValue();
	}

	private String getRoleNameAttribute()
	{
		return StringKey.getValue(REG_KEY_ROLE_NAME_ATTRIBUTE,
				REG_DEFAULT_ROLE_NAME_ATTRIBUTE);
	}

	private String getRoleMemberAttribute()
	{
		return StringKey.getValue(REG_KEY_ROLE_MEMBER_ATTRIBUTE,
				REG_DEFAULT_ROLE_MEMBER_ATTRIBUTE);
	}
	*/
}