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
package org.jtalks.common.service.nontransactional;

import org.jtalks.common.model.dao.UserDao;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.User;
import org.jtalks.common.service.SecurityService;
import org.jtalks.common.service.security.AclBuilder;
import org.jtalks.common.service.security.AclBuilderImpl;
import org.jtalks.common.service.security.AclManager;
import org.jtalks.common.service.security.SecurityConstants;
import org.jtalks.common.service.security.SecurityContextFacade;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * Abstract layer for Spring Security.
 * Contains methods for authentication and authorization.
 * This service
 *
 * @author Kirill Afonin
 * @author Max Malakhov
 */
public class SecurityServiceImpl implements SecurityService {

    private UserDao userDao;
    private AclManager aclManager;
    private SecurityContextFacade securityContextFacade;

    /**
     * Constructor creates an instance of service.
     *
     * @param userDao               {@link UserDao} to be injected
     * @param securityContextFacade {@link .SecurityContextFacade} to be injected
     * @param aclManager            manager for actions with ACLs
     */
    public SecurityServiceImpl(UserDao userDao, SecurityContextFacade securityContextFacade, AclManager aclManager) {
        this.userDao = userDao;
        this.securityContextFacade = securityContextFacade;
        this.aclManager = aclManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getCurrentUser() {
        return userDao.getByUsername(getCurrentUserUsername());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentUserUsername() {
        Authentication auth = securityContextFacade.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        String username = extractUsername(principal);

        if (isAnonymous(username)) {
            return null;
        }
        return username;
    }

    /**
     * Get username from principal.
     *
     * @param principal principal
     * @return username
     */
    private String extractUsername(Object principal) {
        // if principal is spring security user, cast it and get username
        // else it is javax.security principal with toString() that return username
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    /**
     * @param username username
     * @return {@code true} if user is anonymous
     */
    private boolean isAnonymous(String username) {
        return username.equals(SecurityConstants.ANONYMOUS_USERNAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFromAcl(Entity securedObject) {
        deleteFromAcl(securedObject.getClass(), securedObject.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFromAcl(Class clazz, long id) {
        aclManager.deleteFromAcl(clazz, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AclBuilder grant() {
        return new AclBuilderImpl(aclManager, AclBuilderImpl.Action.GRANT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AclBuilder grantToCurrentUser() {
        return new AclBuilderImpl(aclManager, AclBuilderImpl.Action.GRANT).user(getCurrentUserUsername());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AclBuilder delete() {
        return new AclBuilderImpl(aclManager, AclBuilderImpl.Action.DELETE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDao.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }

}
