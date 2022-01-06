/*
 * Copyright 2000,  Bavo De Ridder
 *
 * This file is part of Portal Foundation Classes.
 *
 * Portal Foundation Classes is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * Portal Foundation Classes is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Portal Foundation Classes; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston
 *
 * http://www.gnu.org/licenses/gpl.html
 */
package portal.ui;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Bavo De Ridder <bavo@coderspotting.org>
 *
 */
public interface IFileItem
{
    // ------------------------------- Methods from javax.activation.DataSource
    InputStream getInputStream() throws IOException;

    /**
     * Returns the content type passed by the browser or
     * <code>null</code> if not defined.
     *
     * @return The content type passed by the browser or <code>null</code> if
     * not defined.
     */
    String getContentType();

    /**
     * Returns the original filename in the client's filesystem.
     *
     * @return The original filename in the client's filesystem.
     */
    String getName();

    /**
     * Returns the size of the file.
     *
     * @return The size of the file, in bytes.
     */
    long getSize();

    /**
     * Deletes the underlying storage for a file item, including deleting any
     * associated temporary disk file. Although this storage will be deleted
     * automatically when the
     * <code>FileItem</code> instance is garbage collected, this method can be
     * used to ensure that this is done at an earlier time, thus preserving
     * system resources.
     */
    void delete();
}
