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
package org.jtalks.common.testutils;

import java.util.Random;
import java.util.UUID;

import org.jtalks.common.model.entity.*;

/**
 * @author Kirill Afonin
 * @author Alexey Grigorev
 */
public final class ObjectsFactory {

    public static Branch createBranch() {
        Branch newBranch = new Branch(random(), "desc");
        newBranch.setSection(createSection());
        return newBranch;
    }

    public static Component createComponent(ComponentType type) {
        Component c = new Component(random(), "desc", type);
        c.addProperty("prop.name", "prop.value");
        return c;
    }

    public static Section createSectionWithBranches() {
        return createSectionWithBranches(randomInt());
    }

    public static Section createSectionWithBranches(int branchesAmount) {
        Section section = new Section(random());

        for (int i = 0; i < branchesAmount; i++) {
            Branch branch = createBranch();
            branch.setSection(section);
            branch.setPosition(i);
            section.addOrUpdateBranch(branch);
        }

        return section;
    }

    public static Section createSection() {
        return new Section(random());
    }

    public static User createUser() {
        String random = random();
        return new User(random, random, random, random);
    }

    public static Group createGroup() {
        return new Group(random(), "desc");
    }

    private static String random() {
        return UUID.randomUUID().toString();
    }

    private static int randomInt() {
        return new Random().nextInt(10) + 1;
    }

    public static Rank createRank() {
        int randNum = new Random().nextInt();
        Rank rank = new Rank("Rank" + randNum, randNum);
        return rank;
    }

}
