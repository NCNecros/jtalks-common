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
package org.jtalks.common.model.permissions;

import com.google.common.collect.Lists;
import ru.javatalks.utils.general.Assert;

import javax.annotation.Nonnull;
import java.util.List;


/**
 * These are the restrictions that relate only to branches and sections.
 * <p/>
 * Please follow the binary numeration in permissions
 * and do not create numbers more then 1 in comparing to existing ones
 * (i.e. yoo have 010, use 011, not 10010)
 *
 * @author stanislav bashkirtsev
 */
public enum BranchPermission implements JtalksPermission {
    /**
     * The ability of user group or user to create new topics in the branch.
     *//*
    CREATE_TOPICS("11", "CREATE_TOPICS"),
    *//**
     * The ability of user group or user to view the branch (to see its topics).
     */
    VIEW_TOPICS("110", "VIEW_TOPICS"),
    /**
     * The ability to move topics from the branch, this permissions is granted for to another branch
     * (not mandatory to be grated to the permission for that branch as well).
     */
    MOVE_TOPICS("1000", "MOVE_TOPICS"),

    /* will be implemented soon, no need of remove
    /**
     * The ability to split topic into 2 different topics (not necessary in the same branch).
     */
    /**
     SPLIT_TOPICS("1001", "SPLIT_TOPICS"),
     */
    /**
     * The ability of user group or user to delete topics from the branch.
    DELETE_TOPICS("1010", "DELETE_TOPICS"),*/
    /**
     * The ability of user group or user to close topics in the branch.
     */
    CLOSE_TOPICS("1011", "CLOSE_TOPICS"),
    /**
     * The ability of user group or user to create posts in the branch.
     */
    CREATE_POSTS("1100", "CREATE_POSTS"),
    /**
     * The ability of users to remove their own posts. Some forums prefer to restrict this functionality to avoid
     * misunderstanding between users.
     */
    DELETE_OWN_POSTS("111", "DELETE_OWN_POSTS"),
    /**
     * The ability of users to remove posts of the other users.
     */
    DELETE_OTHERS_POSTS("1101", "DELETE_OTHERS_POSTS"),
    /**
     * The ability of users to edit their own posts.
     */
    EDIT_OWN_POSTS("10000101", "EDIT_OWN_POSTS"),
    /**
     * The ability of users to edit posts of other users.
     */
    EDIT_OTHERS_POSTS("10001", "EDIT_OTHERS_POSTS"),
    /**
     * The ability of users to create announcements
     */
    CREATE_ANNOUNCEMENTS("10010", "CREATE_ANNOUNCEMENTS"),
    /**
     * The ability of users to create sticked topics
     */
    CREATE_STICKED_TOPICS("10011", "CREATE_STICKED_TOPICS");

    private final String name;
    private final int mask;

    /**
     * Constructs the whole object without symbol.
     *
     * @param mask a bit mask that represents the permission, can be negative only for restrictions (look at the class
     *             description). The integer representation of it is saved to the ACL tables of Spring Security.
     * @param name a textual representation of the permission (usually the same as the constant name), though the
     *             restriction usually starts with the 'RESTRICTION_' word
     */
    BranchPermission(int mask, @Nonnull String name) {
        this.mask = mask;
        throwIfNameNotValid(name);
        this.name = name;
    }

    /**
     * Takes a string bit mask.
     *
     * @param mask a bit mask that represents the permission. It's parsed into integer and saved into the ACL tables of
     *             Spring Security.
     * @param name a textual representation of the permission (usually the same as the constant name)
     * @throws NumberFormatException look at {@link Integer#parseInt(String, int)} for details on this as this method is
     *                               used underneath
     * @see BranchPermission#BranchPermission(int, String)
     * @see org.springframework.security.acls.domain.BasePermission
     */
    BranchPermission(@Nonnull String mask, @Nonnull String name) {
        throwIfNameNotValid(name);
        this.mask = Integer.parseInt(mask, 2);
        this.name = name;
    }

    /**
     * Gets the human readable textual representation of the restriction (usually the same as the constant name).
     *
     * @return the human readable textual representation of the restriction (usually the same as the constant name)
     */
    @Override
    public String getName() {
        return name;
    }

    private void throwIfNameNotValid(String name) {
        Assert.throwIfNull(name, "The name can't be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMask() {
        return mask;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPattern() {
        return null;
    }

    public static BranchPermission findByMask(int mask) {
        for (BranchPermission nextPermission : values()) {
            if (mask == nextPermission.getMask()) {
                return nextPermission;
            }
        }
        return null;
    }

    public static List<BranchPermission> getAllAsList() {
        return Lists.newArrayList(values());
    }

}
