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
package org.jtalks.common.validation.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

/**
 * Factory which returns the same instance of validator, defined via its
 * {@link PredefinedConstraintValidationFactory#setValidators(List)}<br>
 * <br>
 * 
 * <b>Note</b>: this implementation is <b>NOT THREAD SAFE</b> when used
 * incorrectly. It will return <b>exactly the same instance</b> each time a
 * validator, provided via setter, is requested - thus <b>they must not keep any
 * state</b>. For safe implementation, see
 * {@link ContextAwareConstraintValidatorFactory} or the default spring's one.
 * The reason why it isn't used is, when creating a bean, autowiring leads to
 * tons of warnings from zk-beans due to their incorrect implementation of some
 * specific spring features.
 */
public class PredefinedConstraintValidationFactory implements ConstraintValidatorFactory {

    private Map<Class<?>, ConstraintValidator<?, ?>> validators;

    /**
     * Returns one of the validators passed via {@link #setValidators(List)}. If
     * not found, it will try to create a new instance of requested validator.
     * If failed, null will be returned (which afterwards may lead to runtime
     * exception).
     * 
     * @param key class of validator to be initialized
     * @param <T> class of validator
     * 
     */
    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        @SuppressWarnings("unchecked")
        T constraintValidator = (T) validators.get(key);

        if (constraintValidator == null) {
            try {
                constraintValidator = key.newInstance();
            } catch (Exception e) {
                return null;
            }
        }

        return constraintValidator;
    }

    /**
     * Pay attention to the note at
     * {@link PredefinedConstraintValidationFactory} and don't put in the list
     * any validators which can have any state.
     * 
     * @param validators predefined validators for returning from {@link #getInstance(Class)}
     */
    public void setValidators(List<ConstraintValidator<?, ?>> validators) {
        Map<Class<?>, ConstraintValidator<?, ?>> index = new HashMap<Class<?>, ConstraintValidator<?, ?>>();
        for (ConstraintValidator<?, ?> cv : validators) {
            index.put(cv.getClass(), cv);
        }

        this.validators = index;
    }

}
