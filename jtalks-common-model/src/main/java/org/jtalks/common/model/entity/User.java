/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.common.model.entity;

import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Stores information about the forum user.
 * Used as {@code UserDetails} in spring security for user authentication, authorization.
 *
 * @author Pavel Vervenko
 * @author Kirill Afonin
 * @author Alexandre Teterin
 */
public class User extends Entity implements UserDetails {
    private String lastName;
    private String firstName;
    private String username;
    private String email;
    private String password;
    private DateTime lastLogin;
    private String role = "ROLE_USER";
    private String encodedUsername;
    private byte[] avatar;

    /**
     * Minimum length of the username.
     */
    public static final int USERNAME_MIN_LENGTH = 3;
    /**
     * Maximum length of the username.
     */
    public static final int USERNAME_MAX_LENGTH = 20;
    /**
     * Minimum length of the password.
     */
    public static final int PASSWORD_MIN_LENGTH = 4;
    /**
     * Maximum length of the password.
     */
    public static final int PASSWORD_MAX_LENGTH = 20;
    /**
     * Maximum avatar width in pixels.
     */
    public static final int AVATAR_MAX_WIDTH = 100;
    /**
     * Maximum avatar height in pixels.
     */
    public static final int AVATAR_MAX_HEIGHT = 100;
    /**
     * Maximum avatar size in kilobytes.
     */
    public static final int AVATAR_MAX_SIZE = 65;


    /**
     * Only for hibernate usage.
     */
    protected User() {
    }

    /**
     * Create instance with requiered fields.
     *
     * @param username username
     * @param email    email
     * @param password password
     */
    public User(String username, String email, String password) {
        this.setUsername(username);
        this.email = email;
        this.password = password;
        this.avatar = new byte[0];
    }

    /**
     * Get the user's Last Name.
     *
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Set the username and encoded username (based on username)
     *
     * @param username the username to set
     */
    public final void setUsername(String username) {
        this.username = username;
        try {
            setEncodedUsername(URLEncoder.encode(username, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Could not encode username", e);
        }
    }

    /**
     * @return user role in security system
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return user avatar
     */
    public byte[] getAvatar() {
        return avatar.clone();
    }

    /**
     * @param avatar user avatar
     */
    public void setAvatar(byte[] avatar) {
        this.avatar = (avatar != null) ? avatar.clone() : new byte[0];
    }

    /**
     * @return collection of user roles
     */
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new GrantedAuthorityImpl(role));
        return authorities;
    }

    /**
     * @return password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    //methods from UserDetails inteface, indicating that
    //user can or can't authenticate.
    //we don't need this functional now and users always enabled

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * @return last login time  and date
     */
    public DateTime getLastLogin() {
        return lastLogin;
    }

    /**
     * Set last login time and date.
     *
     * @param lastLogin last login time
     */
    protected void setLastLogin(DateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    private static final long serialVersionUID = 19981017L;

    /**
     * Updates login time to current time
     */
    public void updateLastLoginTime() {
        this.lastLogin = new DateTime();
    }

    /**
     * @return encoded username
     */
    public String getEncodedUsername() {
        return encodedUsername;
    }

    /**
     * @param encodedUsername encoded username to set
     */
    protected final void setEncodedUsername(String encodedUsername) {
        this.encodedUsername = encodedUsername;
    }
}
