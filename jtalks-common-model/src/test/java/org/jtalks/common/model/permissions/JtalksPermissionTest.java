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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.assertNotSame;

/**
 * @author stanislav bashkirtsev
 */
public class JtalksPermissionTest {
    /**
     * Looks for all the permission classes within JTalks, reads all their constants and compares to each other making
     * sure that there are no duplicates (those that have the same bit mask).
     *
     * @param permissions all the permissions in the project to compare to each other
     * @throws Exception these are tests, who would care
     */
    @Test(dataProvider = "allProjectPermissions")
    public void testNoIdenticalConstants(List<JtalksPermission> permissions) throws Exception {
        for (int i = 0; i < permissions.size(); i++) {
            for (int j = i + 1; j < permissions.size(); j++) {
                JtalksPermission first = permissions.get(i);
                JtalksPermission second = permissions.get(j);
                assertNotSame("Permissions with identical bit mask were encountered: "
                        + first.getName() + " & " + second.getName(), first.getMask(), second.getMask());
            }
        }
    }

    /**
     * This method looks at all the classes that contain {@link JtalksPermission}, looks up for its declared fields,
     * then filters out those that are not of type {@link JtalksPermission} and returns only permissions. If you
     * implement another such class which contains permissions, then you should add it to this method so that your class
     * will be tested as well.
     *
     * @return all the instances-constants of {@link JtalksPermission} from all the classes in the project
     */
    @DataProvider(name = "allProjectPermissions")
    protected Object[][] getAllProjectPermissions() {
        List<JtalksPermission> permissions = new ArrayList<JtalksPermission>();
        permissions.addAll(BranchPermission.getAllAsList());
        permissions.addAll(GeneralPermission.getAllAsList());
        return new Object[][]{{permissions}};
    }

}
