/*
 * Copyright 2000 - 2004,  Bavo De Ridder
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
package portal.rt.servlet;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.ui.PortalException;

public class FileStylesheetProvider implements IStylesheetProvider
{
    private static final Log LOGGER = LogFactory.getLog(FileStylesheetProvider.class);
    private static final int EOF = -1;

    public FileStylesheetProvider(String path)
    {
        _path = path;
    }

    public Reader getStyleSheetReader() throws PortalException
    {
        return new ChainReader(getReaders());
    }

    public boolean isCacheable()
    {
        return false;
    }

    private List<Reader> getReaders() throws PortalException
    {
        List<Reader> readers = new ArrayList<Reader>();

        File sheetsFolder = new File(_path);

        if (sheetsFolder == null || !sheetsFolder.exists())
        {
            throw new PortalException("The path '" + _path
                    + "' does not exist or could not be read.");
        }

        File[] files = sheetsFolder.listFiles();

        for (int i = 0; i < files.length; i++)
        {
            String fileName = files[i].getName();

            if (fileName.endsWith(".css"))
            {
                try
                {
                    readers.add(new FileReader(files[i]));
                }
                catch (Exception e)
                {
                    LOGGER.warn("Could not read css file " + fileName);
                }
            }
        }

        return readers;
    }

    private class ChainReader extends Reader
    {
        public ChainReader(List<Reader> readers)
        {
            _readers = readers;
            _currentReaderIndex = 0;
        }

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException
        {
            if (_internalBuffer != null)
            {
                int numCharsToWrite;

                if (_internalBuffer.length > len)
                {
                    numCharsToWrite = len;
                }
                else
                {
                    numCharsToWrite = _internalBuffer.length;
                }

                System.arraycopy(_internalBuffer, 0, cbuf, off, numCharsToWrite);

                if (numCharsToWrite == _internalBuffer.length)
                {
                    _internalBuffer = null;
                }
                else
                {
                    int remainingChars = _internalBuffer.length
                            - numCharsToWrite;
                    char[] newBuffer = new char[remainingChars];

                    System.arraycopy(_internalBuffer, numCharsToWrite,
                            newBuffer, 0, remainingChars);
                }

                return numCharsToWrite;
            }

            Reader currentReader = getCurrentReader();

            int charRead = currentReader.read(cbuf, off, len);

            if (charRead == EOF)
            {
                if (isCurrentLast())
                {
                    return EOF;
                }
                else
                {
                    _currentReaderIndex++;

                    _internalBuffer = "\n\n".toCharArray();

                    return 0;
                }
            }

            return charRead;
        }

        @Override
        public void close() throws IOException
        {
        }

        private Reader getCurrentReader()
        {
            return _readers.get(_currentReaderIndex);
        }

        private boolean isCurrentLast()
        {
            return _currentReaderIndex == (_readers.size() - 1);
        }
        private int _currentReaderIndex;
        private List<Reader> _readers;
        private char[] _internalBuffer;
    }
    private String _path;
}
