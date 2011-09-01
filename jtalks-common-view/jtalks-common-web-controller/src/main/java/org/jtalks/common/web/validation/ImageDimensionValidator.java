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
package org.jtalks.common.web.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.*;

/**
 * Validator for {@link ImageDimension}. Checks that image has allowable dimension.
 *
 * @author Eugeny Batov
 * @see ImageDimension
 */
public class ImageDimensionValidator implements ConstraintValidator<ImageDimension, MultipartFile> {

    private int imageHeight;
    private int imageWidth;

    /**
     * Initialize validator fields from annotation instance.
     *
     * @param constraintAnnotation {@link ImageDimension} annotation from class
     * @see ImageDimension
     */
    @Override
    public void initialize(ImageDimension constraintAnnotation) {
        this.imageHeight = constraintAnnotation.height();
        this.imageWidth = constraintAnnotation.width();
    }

    /**
     * Validate object with {@link ImageDimension} annotation.
     *
     * @param multipartFile image that user want upload as avatar
     * @param context       validation context
     * @return {@code true} if validation successfull or false if fails
     */
    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        if (multipartFile.isEmpty()) {
            //assume that empty multipart file is valid to avoid validation message when user doesn't load nothing
            return true;
        }
        Image image;
        try {
            image = ImageIO.read(multipartFile.getInputStream());
        }
        catch (Exception e) {
            return false;
        }
        return (image == null) ? false : image.getWidth(null) == imageWidth &&
              image.getHeight(null) == imageHeight;
    }
}
