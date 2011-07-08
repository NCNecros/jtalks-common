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
package org.jtalks.jcommune.web.validation;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.*;
import java.util.Set;

/**
 * Test for {@link Matches} annotation constraint and {@link MatchesValidator}
 * implementation.
 *
 * @author Kirill Afonin
 */
public class MatchesValidatorTest {
    /**
     * Class for testing constraint.
     */
    @Matches(field = "value", verifyField = "value2", message = "Values don't match")
    public class TestObject {
        String value;
        String value2;

        public TestObject(String value, String value2) {
            this.value = value;
            this.value2 = value2;
        }

        public String getValue() {

            return value;
        }

        public String getValue2() {
            return value2;
        }
    }

    /**
     * Class for testing constraint with non existing properties.
     */
    @Matches(field = "aaa", verifyField = "bcv")
    public class TestObjectBadProperties {
        String value;
        String value2;

        public TestObjectBadProperties(String value, String value2) {
            this.value = value;
            this.value2 = value2;
        }
    }

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidatorSuccess() {
        Set<ConstraintViolation<TestObject>> constraintViolations =
                validator.validate(new TestObject("value", "value"));

        Assert.assertEquals(constraintViolations.size(), 0, "Validation errors");
    }

    @Test
    public void testValidatorFail() {
        Set<ConstraintViolation<TestObject>> constraintViolations =
                validator.validate(new TestObject("value", "not"));

        Assert.assertEquals(constraintViolations.size(), 1, "Validation without errors");
        Assert.assertEquals(constraintViolations.iterator().next().getMessage(), "Values don't match");
    }

    @Test
    public void testNullFields() {
        Set<ConstraintViolation<TestObject>> constraintViolations =
                validator.validate(new TestObject(null, null));

        Assert.assertEquals(constraintViolations.size(), 0, "Validation errors");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testPropertiesNotExist() {
        Set<ConstraintViolation<TestObjectBadProperties>> constraintViolations =
                validator.validate(new TestObjectBadProperties("1", "2"));

    }

}
