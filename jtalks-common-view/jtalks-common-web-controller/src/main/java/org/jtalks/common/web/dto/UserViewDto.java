/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.common.web.dto;

import org.joda.time.DateTime;
import org.jtalks.common.model.entity.User;

/**
 * DTO for {@link User} object
 * 
 * Date: 15.09.11
 * Time: 21:44
 *
 * @author Dmitriy Butakov
 */
public class UserViewDto {
    private String firstName;
    private String lastName;
    private String username;
    private String encodedUsername;
    private String email;
    private DateTime lastLogin;

    public UserViewDto(User user) {
        firstName = user.getFirstName();
        lastName = user.getLastName();
        username = user.getUsername();
        encodedUsername = user.getEncodedUsername();
        email = user.getEmail();
        lastLogin = user.getLastLogin();
    }

    /**
     * Get user's first name
     * @return
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Get user's last name
     * @return
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Get username
     * @return
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Get encoded username
     * @return
     */
    public String getEncodedUsername()
    {
        return encodedUsername;
    }

    /**
     * Get email
     * @return
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Get last login date
     * @return
     */
    public DateTime gelLastLogin() {
        return lastLogin;
    }
}