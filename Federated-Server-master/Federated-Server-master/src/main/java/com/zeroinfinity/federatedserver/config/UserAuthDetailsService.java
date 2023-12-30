package com.zeroinfinity.federatedserver.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*It overrides the loadUserByUsername
 * for fetching user details from the database using the username.
 * The Spring Security Authentication Manager calls this method
 * for getting the user details from the database when authenticating
 * the user details provided by the user.*/

@Service
public class UserAuthDetailsService implements UserDetailsService {

	@Value("${jwt.user}")
	private String user;

	@Value("${jwt.password}")
	private String password;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		String[] usernameAndChannel = username.split("\\|");
		if (!user.equals(usernameAndChannel[0])) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}

		return new org.springframework.security.core.userdetails.User(username, password, getAuthorities());
	}

	private Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("federatedServerRole"));
		return authorities;
	}

}
