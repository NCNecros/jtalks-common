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
package org.jtalks.common.service;

import org.jtalks.common.model.entity.User;
import org.jtalks.common.service.exceptions.DuplicateEmailException;
import org.jtalks.common.service.exceptions.DuplicateUserException;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.common.service.exceptions.WrongPasswordException;

/**
 * This interface should have methods which give us more abilities in manipulating User persistent entity.
 *
 * @author Osadchuck Eugeny
 * @author Kirill Afonin
 * @author Dmitry Sokolov
 */
public interface UserService extends EntityService<User> {
    /**
     * Get {@link User} by username.
     *
     * @param username username of User
     * @return {@link User} with given username
     * @throws NotFoundException if user not found
     * @see User
     */
    User getByUsername(String username) throws NotFoundException;

    /**
     * Get {@link User} by encodedUsername.
     *
     * @param encodedUsername encodedUsername of User
     * @return {@link User} with given encodedUsername
     * @throws NotFoundException if user not found
     * @see User
     */
    User getByEncodedUsername(String encodedUsername) throws NotFoundException;

    /**
     * Try to register {@link User} with given features.
     *
     * @param user user for register
     * @return registered {@link User}
     * @throws DuplicateUserException  if user with username already exist
     * @throws DuplicateEmailException when user with given email already exist
     * @see User
     */
    User registerUser(User user) throws DuplicateUserException, DuplicateEmailException;


    /**
     * Updates user last login time to current time.
     *
     * @param user user which must be updated
     * @see User
     */
    void updateLastLoginTime(User user);

    /**
     * Update user entity.
     *
     * @param email           email
     * @param firstName       first name
     * @param lastName        last name
     * @param currentPassword current user password, could be NULL
     * @param newPassword     new user password, could be NULL
     * @return edited user
     * @throws DuplicateEmailException when user with given email already exist
     * @throws WrongPasswordException  when user enter wrong currentPassword
     */
    User editUserProfile(String email, String firstName, String lastName, String currentPassword, String newPassword,
                         byte[] avatar) throws DuplicateEmailException, WrongPasswordException;

    /**
     * Remove current user's avatar.
     */
    void removeAvatarFromCurrentUser();

    /**
     * Return default avatar for example if user doesn't have one
     * @return avatar as byte array
     */
    byte[] getDefaultAvatar();

}
