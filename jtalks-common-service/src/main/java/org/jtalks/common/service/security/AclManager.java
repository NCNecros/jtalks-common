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
package org.jtalks.common.service.security;

import org.jtalks.common.model.entity.Entity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import java.util.List;

/**
 * Interface that contains operations with ACL.
 *
 * @author Kirill Afonin
 */
public interface AclManager {
    /**
     * Grant permissions from list to every sid in list on {@code target} object.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      secured object
     */
    void grant(List<Sid> sids, List<Permission> permissions, Entity target);

    /**
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      secured object
     */
    void revoke(List<Sid> sids, List<Permission> permissions, Entity target);

    /**
     * Delete permissions from list for every sid in list on {@code target} object.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      secured object
     */
    void delete(List<Sid> sids, List<Permission> permissions, Entity target);

    /**
     * Delete object from acl. All permissions will be removed.
     *
     * @param clazz object {@code Class}
     * @param id    object id
     */
    void deleteFromAcl(Class clazz, long id);
}
