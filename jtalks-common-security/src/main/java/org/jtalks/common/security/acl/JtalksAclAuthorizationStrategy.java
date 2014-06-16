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
package org.jtalks.common.security.acl;

import org.jtalks.common.security.acl.sids.SidFactory;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Mikhail Stryzhonok
 */
public class JtalksAclAuthorizationStrategy extends AclAuthorizationStrategyImpl {
    private SidFactory sidFactory;

    public JtalksAclAuthorizationStrategy(GrantedAuthority... auths) {
        super(auths);
    }

    @Override
    protected Sid createCurrentUser(Authentication authentication) {
        return sidFactory.createPrincipal(authentication);
    }

    public SidFactory getSidFactory() {
        return sidFactory;
    }

    public void setSidFactory(SidFactory sidFactory) {
        this.sidFactory = sidFactory;
    }
}
