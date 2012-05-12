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
import org.jtalks.common.validation.annotations.UniqueConstraint;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * User Groups is the class that can join users into groups. After that permissions can be assigned to the groups and
 * all users in this group will have that permission while browsing components.
 *
 * @author Akimov Konstantin
 * @author Vyacheslav Zhivaev
 */
@UniqueConstraint
public class Group extends Entity {
    /**
     * Error message if group description has illegal length
     */
    private static final String GROUP_DESCRIPTION_ILLEGAL_LENGTH = "{group.description.length_constraint_violation}";

    /**
     * Error message if group name is void
     */
    private static final String GROUP_CANT_BE_VOID = "{group.name.emptiness_constraint_violation}";

    /**
     * Error message if group name length is illegal
     */
    private static final String GROUP_NAME_ILLEGAL_LENGTH = "{group.name.length_constraint_violation}";

    public static final int GROUP_NAME_MAX_LENGTH = 100;
    public static final int GROUP_DESCRIPTION_MAX_LENGTH = 256;

    @NotBlank(message = GROUP_CANT_BE_VOID)
    @Length(max = GROUP_NAME_MAX_LENGTH, message = GROUP_NAME_ILLEGAL_LENGTH)
    private String name;

    @Length(max = GROUP_DESCRIPTION_MAX_LENGTH, message = GROUP_DESCRIPTION_ILLEGAL_LENGTH)
    private String description;
    private List<User> users = new ArrayList<User>();

    /**
     * Creates {@link Group} with empty users list
     */
    public Group() {
    }

    /**
     * @param name the title of the groups, when saving to DB, can't be empty or
     * {@code null}, it also should be unique
     */
    public Group(String name) {
        this.name = name;
    }

    /**
     * @param name the title of the groups, when saving to DB, can't be empty or
     * {@code null}, it also should be unique
     * @param description an optional description of the group
     */
    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the title of the group, if it's already in DB, it's unique and not empty or {@code null}.
     *
     * @return the title of the group
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the title of the group, when saving to DB, can't be empty or {@code null}, it also should be unique.
     *
     * @param name the title of the group
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the textual description of the group.
     *
     * @return the optional description of the group
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the optional textual description of the group.
     *
     * @param description the description of the group; optional
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets list of users assigned to this group.
     *
     * @return the list of users in this group
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Sets list of users assigned to this group.
     *
     * @param users the list of users in this group
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * A handy method to create a number of groups with specified names.
     *
     * @param names the names you want resulting groups to be with
     * @return a list of groups with the specified name in the same order
     */
    public static List<Group> createGroupsWithNames(String... names) {
        List<Group> groups = new ArrayList<Group>(names.length);
        for (String nextName : names) {
            groups.add(new Group(nextName, ""));
        }
        return groups;
    }

    /**
     * Lets the Group classes be comparable by their names. Throws NPE if anything is {@code null} whether it's a group
     * itself or its name.
     *
     * @author stanislav bashkirtsev
     */
    public static class ByNameComparator implements Comparator<Group> {
        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(@Nonnull Group group, @Nonnull Group group1) {
            return group.getName().compareTo(group1.getName());
        }
    }

}
