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

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;
import org.jtalks.common.validation.annotations.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import ru.javatalks.utils.datetime.DateTimeUtilsFactory;

import javax.validation.constraints.NotNull;
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
 * @author Masich Ivan
 * @author Ancient_Mariner
 */
public class User extends Entity implements UserDetails {
    
    private static final String USER_EMAIL_ILLEGAL_FORMAT = "{user.email.email_format_constraint_violation}";
    private static final String USER_PASSWORD_ILLEGAL_LENGTH = "{user.password.length_constraint_violation}";
    private static final String USER_USERNAME_ILLEGAL_LENGTH = "{user.username.length_constraint_violation}";
    private static final String USER_CANT_BE_NULL = "{user.username.null_constraint_violation}";

    private String lastName;
    private String firstName;

    @NotNull(message = USER_CANT_BE_NULL)
    @Length(min = 1, max = 25, message = USER_USERNAME_ILLEGAL_LENGTH)
    private String username;

    @Email(message = USER_EMAIL_ILLEGAL_FORMAT)
    private String email;

    @NotBlank
    @Length(max = 50, message = USER_PASSWORD_ILLEGAL_LENGTH)
    private String password;
    private DateTime lastLogin;
    private String role = "ROLE_USER";
    private String encodedUsername;
    private byte[] avatar = new byte[0];
    private boolean permanentBan;
    private DateTime banExpirationDate;
    private String banReason;
    private String salt;


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
     * @deprecated
     */
    public User(String username, String email, String password) {
        this();
        this.setUsername(username);
        this.email = email;
        this.password = password;
        this.permanentBan = false;
        this.salt = "";
    }

    /**
     * Create instance with requiered fields.
     *
     * @param username username
     * @param email    email
     * @param password password
     * @param salt     salt
     */
    public User(String username, String email, String password, String salt) {
        this(username, email, password);
        this.setSalt(salt);
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
     * {@inheritDoc}
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Set the username and encoded username (based on username).
     *
     * @param username the username to set
     */
    public final void setUsername(String username) {
        this.username = username;
        try {
            setEncodedUsername(URLEncoder.encode(username, "UTF-8").replace("+", "%20"));
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
     * Updates last login time to current time.
     */
    public void updateLastLoginTime() {
        this.lastLogin = DateTimeUtilsFactory.getDateTimeUtils().getNow();
    }

    /**
     * Sets the encoded version of the username, where all the symbols that might mean something special (like symbols
     * {@code \;:",.}, etc are replaced with the encoding sequence of symbols. This is required for instance while
     * constructing a URL that contains a username (let's say a link to the user profile).
     *
     * @return encoded version of username that doesn't contain special symbols
     */
    public String getEncodedUsername() {
        return encodedUsername;
    }

    /**
     * Sets the encoded version of the username, where all the symbols that might mean something special (like symbols
     * {@code \;:",.}, etc are replaced with the encoding sequence of symbols. This is required for instance while
     * constructing a URL that contains a username (let's say a link to the user profile).
     *
     * @param encodedUsername encoded version of username that doesn't contain special symbols
     */
    protected final void setEncodedUsername(String encodedUsername) {
        this.encodedUsername = encodedUsername;
    }

    /**
     * Gets the user permanent ban status.
     *
     * @return the {@code true} if user is banned, {@code false} otherwise
     */
    public boolean isPermanentBan() {
        return permanentBan;
    }

    /**
     * Sets the user permanent ban status.
     *
     * @param permanentBan the status to set
     */
    public void setPermanentBan(boolean permanentBan) {
        this.permanentBan = permanentBan;
    }

    /**
     * Gets the user ban expiration date and time.
     *
     * @return the {@code DateTime} object, contains date and time when user ban
     *         status will have expired
     */
    public DateTime getBanExpirationDate() {
        return banExpirationDate;
    }

    /**
     * Sets the user ban expiration date and time.
     *
     * @param banExpirationDate the {@code DateTime} object to set, contains date and time when user
     *                          ban status will have expired
     */
    public void setBanExpirationDate(DateTime banExpirationDate) {
        this.banExpirationDate = banExpirationDate;
    }

    /**
     * This method returns the string ban reason description
     *
     * @return ban reason
     */
    public String getBanReason() {
        return banReason;
    }

    /**
     * This method sets user ban reason description
     *
     * @param banReason User ban reason description
     */
    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

    /**
     * This method returns the salt for encode password
     *
     * @return salt string
     */
    public String getSalt() {
        return salt;
    }

    /**
     * This method sets salt for encode password
     * @param salt salt salstring
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }
}
