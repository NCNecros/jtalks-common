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
package org.jtalks.common.service.transactional;

import org.joda.time.DateTime;
import org.jtalks.common.model.dao.UserDao;
import org.jtalks.common.model.entity.User;
import org.jtalks.common.service.SecurityService;
import org.jtalks.common.service.UserService;
import org.jtalks.common.service.exceptions.DuplicateEmailException;
import org.jtalks.common.service.exceptions.DuplicateException;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.jtalks.common.service.exceptions.WrongPasswordException;
import org.jtalks.common.service.security.SecurityConstants;
import org.mockito.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * @author Kirill Afonin
 * @author Osadchuck Eugeny
 */
public class TransactionalUserServiceTest {
    private static final String USERNAME = "username";
    private static final String ENCODED_USERNAME = "encodedUsername";
    private static final String FIRST_NAME = "first name";
    private static final String LAST_NAME = "last name";
    private static final String NEW_EMAIL = "new_username@mail.com";
    private static final String EMAIL = "username@mail.com";
    private static final String PASSWORD = "password";
    private static final String WRONG_PASSWORD = "abracodabra";
    private static final String NEW_PASSWORD = "newPassword";
    private byte[] avatar = new byte[10];
    private static final Long USER_ID = 999L;

    private UserService userService;
    private UserDao userDao;
    private SecurityService securityService;

    @BeforeMethod
    public void setUp() throws Exception {
        securityService = mock(SecurityService.class);
        userDao = mock(UserDao.class);
        userService = new TransactionalUserService(userDao, securityService);
    }

    @Test
    public void testGetByUsername() throws Exception {
        User expectedUser = getUser();
        when(userDao.getByUsername(USERNAME)).thenReturn(expectedUser);

        User result = userService.getByUsername(USERNAME);

        assertEquals(result, expectedUser, "Username not equals");
        verify(userDao).getByUsername(USERNAME);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testGetByUsernameNotFound() throws Exception {
        when(userDao.getByUsername(USERNAME)).thenReturn(null);

        userService.getByUsername(USERNAME);
    }

    @Test
    public void testGetByEncodedUsername() throws Exception {
        User user = getUser();
        when(userDao.getByEncodedUsername(ENCODED_USERNAME)).thenReturn(user);

        User actualUser = userService.getByEncodedUsername(ENCODED_USERNAME);

        assertEquals(actualUser, user, "Users are not equal");
        verify(userDao).getByEncodedUsername(anyString());
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testGetByEncodedUsernamenotFound() throws Exception {
        when(userDao.getByEncodedUsername(ENCODED_USERNAME)).thenReturn(null);

        User actualUser = userService.getByEncodedUsername(ENCODED_USERNAME);

        verify(userDao).getByEncodedUsername(anyString());
    }

    @Test
    public void testRegisterUser() throws Exception {
        User user = getUser();
        when(userDao.isUserWithEmailExist(EMAIL)).thenReturn(false);
        when(userDao.isUserWithUsernameExist(USERNAME)).thenReturn(false);

        User registeredUser = userService.registerUser(user);

        assertEquals(registeredUser.getUsername(), USERNAME);
        assertEquals(registeredUser.getEmail(), EMAIL);
        assertEquals(registeredUser.getPassword(), PASSWORD);
        verify(userDao).isUserWithEmailExist(EMAIL);
        verify(userDao).isUserWithUsernameExist(USERNAME);
        verify(userDao).saveOrUpdate(user);
    }

    @Test(expectedExceptions = {DuplicateException.class})
    public void testRegisterUserUsernameExist() throws Exception {
        User user = getUser();
        when(userDao.isUserWithEmailExist(EMAIL)).thenReturn(false);
        when(userDao.isUserWithUsernameExist(USERNAME)).thenReturn(true);

        userService.registerUser(user);
    }

    @Test(expectedExceptions = {DuplicateException.class})
    public void testRegisterUserEmailExist() throws Exception {
        User user = getUser();
        when(userDao.isUserWithEmailExist(EMAIL)).thenReturn(true);
        when(userDao.isUserWithUsernameExist(USERNAME)).thenReturn(false);

        userService.registerUser(user);
    }

    @Test(expectedExceptions = {DuplicateException.class})
    public void testRegisterUserBothExist() throws Exception {
        User user = getUser();
        when(userDao.isUserWithEmailExist(EMAIL)).thenReturn(true);
        when(userDao.isUserWithUsernameExist(USERNAME)).thenReturn(true);

        userService.registerUser(user);
    }

    @Test(expectedExceptions = {DuplicateException.class})
    public void testRegisterUserAnonymous() throws Exception {
        User user = getUser(SecurityConstants.ANONYMOUS_USERNAME);

        userService.registerUser(user);
    }

    @Test
    public void testEditUserProfile() throws Exception {
        User user = getUser();
        when(securityService.getCurrentUser()).thenReturn(user);
        when(userDao.isUserWithEmailExist(EMAIL)).thenReturn(false);

        User editedUser = userService.editUserProfile(EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, NEW_PASSWORD, avatar);

        verify(securityService).getCurrentUser();
        verify(userDao).saveOrUpdate(user);
        assertEquals(editedUser.getEmail(), EMAIL, "Email was not changed");
        assertEquals(editedUser.getFirstName(), FIRST_NAME, "first name was not changed");
        assertEquals(editedUser.getLastName(), LAST_NAME, "last name was not changed");
        assertEquals(editedUser.getPassword(), NEW_PASSWORD, "new password was not accepted");
    }

    @Test
    public void testChangeEmail() throws Exception {
        User user = getUser();
        user.setEmail("some@mail.com");

        when(securityService.getCurrentUser()).thenReturn(user);
        when(userDao.isUserWithEmailExist(EMAIL)).thenReturn(false);

        User editedUser = userService.editUserProfile(EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, NEW_PASSWORD, avatar);

        verify(securityService).getCurrentUser();
        verify(userDao).saveOrUpdate(user);
        assertEquals(editedUser.getEmail(), EMAIL, "Email was not changed");
        assertEquals(editedUser.getFirstName(), FIRST_NAME, "first name was not changed");
        assertEquals(editedUser.getLastName(), LAST_NAME, "last name was not changed");
        assertEquals(editedUser.getPassword(), NEW_PASSWORD, "new password was not accepted");
    }

    @Test
    public void testSetNullAvatar() throws Exception {
        User user = mock(User.class);
        when(user.getPassword()).thenReturn(PASSWORD);
        when(user.getEmail()).thenReturn(EMAIL);

        when(securityService.getCurrentUser()).thenReturn(user);
        when(userDao.isUserWithEmailExist(EMAIL)).thenReturn(false);

        userService.editUserProfile(EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, NEW_PASSWORD, new byte[0]);

        verify(securityService).getCurrentUser();
        verify(user, never()).setAvatar(Matchers.<byte[]>any());
        verify(userDao).saveOrUpdate(user);
    }

    @Test(expectedExceptions = WrongPasswordException.class)
    public void testEditUserProfileWrongPassword() throws Exception {
        User user = getUser();
        when(securityService.getCurrentUser()).thenReturn(user);

        userService.editUserProfile(EMAIL, FIRST_NAME, LAST_NAME, WRONG_PASSWORD, NEW_PASSWORD, avatar);

        verify(securityService).getCurrentUser();
        verify(userDao, never()).isUserWithEmailExist(anyString());
        verify(userDao, never()).saveOrUpdate(any(User.class));
    }

    @Test(expectedExceptions = WrongPasswordException.class)
    public void testEditUserProfilecurrentPasswordNull() throws Exception {
        User user = getUser();
        when(securityService.getCurrentUser()).thenReturn(user);

        userService.editUserProfile(EMAIL, FIRST_NAME, LAST_NAME, null, NEW_PASSWORD, avatar);

        verify(securityService).getCurrentUser();
        verify(userDao, never()).isUserWithEmailExist(anyString());
        verify(userDao, never()).saveOrUpdate(any(User.class));
    }

    @Test(expectedExceptions = DuplicateEmailException.class)
    public void testEditUserProfileDublicateEmail() throws Exception {
        User user = getUser();
        when(securityService.getCurrentUser()).thenReturn(user);
        when(userDao.isUserWithEmailExist(NEW_EMAIL)).thenReturn(true);

        userService.editUserProfile(NEW_EMAIL, FIRST_NAME, LAST_NAME, null, null, avatar);

        verify(securityService).getCurrentUser();
        verify(userDao).isUserWithEmailExist(NEW_EMAIL);
        verify(userDao, never()).saveOrUpdate(any(User.class));
    }


    /**
     * @param username username
     * @return create and return {@link User} with default username, encodedUsername,
     *         first name, last name,  email and password
     */
    private User getUser(String username) {
        User user = new User(username, EMAIL, PASSWORD);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setAvatar(avatar);
        return user;
    }

    private User getUser() {
        return getUser(USERNAME);
    }

    @Test
    public void testGet() throws NotFoundException {
        User expectedUser = new User(USERNAME, EMAIL, PASSWORD);
        when(userDao.get(USER_ID)).thenReturn(expectedUser);
        when(userDao.isExist(USER_ID)).thenReturn(true);

        User user = userService.get(USER_ID);

        assertEquals(user, expectedUser, "Users aren't equals");
        verify(userDao).isExist(USER_ID);
        verify(userDao).get(USER_ID);
    }

    @Test
    public void testUpdateLastLoginTime() throws Exception {
        User user = new User(USERNAME, EMAIL, PASSWORD);
        DateTime dateTimeBefore = new DateTime();
        Thread.sleep(1000);

        userService.updateLastLoginTime(user);

        DateTime dateTimeAfter = user.getLastLogin();
        assertEquals(dateTimeAfter.compareTo(dateTimeBefore), 1, "last login time lesser than before test");
        verify(userDao).saveOrUpdate(user);
    }

    @Test
    public void testRemoveAvatar() {
        User user = getUser();
        when(securityService.getCurrentUser()).thenReturn(user);
        userService.removeAvatarFromCurrentUser();
        assertEquals(user.getAvatar().length, 0, "Avatar after remove should be null");
    }
}
