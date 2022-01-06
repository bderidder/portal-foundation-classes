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
package portal.rt.ui.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import portal.rt.servlet.PortalProcessException;

/**
 * <p>
 * High level API for processing file uploads.
 * </p>
 *
 * <p>
 * This class handles multiple files per single HTML widget, sent using
 * < code > multipart / mixed < / code > encoding type, as specified by <a
 * href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>. Use
 * {@link #parseRequest(HttpServletRequest)} to acquire a list of
 * {@link org.apache.commons.fileupload.FileItem}s associated with a given HTML
 * widget.
 * </p>
 *
 * <p>
 * Files will be stored in temporary disk storage or in memory, depending on
 * request size, and will be available as
 * {@link org.apache.commons.fileupload.FileItem}s.
 * </p>
 *
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author <a href="mailto:dlr@collab.net">Daniel Rall</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:jmcnally@collab.net">John McNally</a>
 * @author <a href="mailto:martinc@apache.org">Martin Cooper</a>
 * @author Sean C. Sullivan
 *
 * @version $Id: FileUpload.java,v 1.3 2010/08/07 11:06:58 bavodr Exp $
 */
public class FileUpload
{
    // ----------------------------------------------------- Class methods
    private static final int NAME_HEADER_PREFIX_SIZE = 6;
    private static final int FILENAME_HEADER_PREFIX_SIZE = 10;
    private static final int BOUNDARY_HEADER_PREFIX_SIZE = 9;

    /**
     * Utility method that determines whether the request contains multipart
     * content.
     *
     * @param req The servlet request to be evaluated. Must be non-null.
     *
     * @return <code>true</code> if the request is multipart; <code>false</code>
     * otherwise.
     */
    public static final boolean isMultipartContent(HttpServletRequest req)
    {
        String contentType = req.getHeader(CONTENT_TYPE);
        if (contentType == null)
        {
            return false;
        }
        if (contentType.startsWith(MULTIPART))
        {
            return true;
        }
        return false;
    }
    // ----------------------------------------------------- Manifest constants
    /**
     * HTTP content type header name.
     */
    public static final String CONTENT_TYPE = "Content-type";
    /**
     * HTTP content disposition header name.
     */
    public static final String CONTENT_DISPOSITION = "Content-disposition";
    /**
     * Content-disposition value for form data.
     */
    public static final String FORM_DATA = "form-data";
    /**
     * Content-disposition value for file attachment.
     */
    public static final String ATTACHMENT = "attachment";
    /**
     * Part of HTTP content type header.
     */
    private static final String MULTIPART = "multipart/";
    /**
     * HTTP content type header for multipart forms.
     */
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    /**
     * HTTP content type header for multiple uploads.
     */
    public static final String MULTIPART_MIXED = "multipart/mixed";
    /**
     * The maximum length of a single header line that will be parsed (1024
     * bytes).
     */
    public static final int MAX_HEADER_SIZE = 1024;
    // ----------------------------------------------------------- Data members
    /**
     * The maximum size permitted for an uploaded file.
     */
    private int _sizeMax;
    /**
     * The threshold above which uploads will be stored on disk.
     */
    private int _sizeThreshold;
    /**
     * The path to which uploaded files will be stored, if stored on disk.
     */
    private String _repositoryPath;

    // ----------------------------------------------------- Property accessors
    /**
     * Returns the maximum allowed upload size.
     *
     * @return The maximum allowed size, in bytes.
     *
     * @see #setSizeMax(int)
     *
     */
    public int getSizeMax()
    {
        return _sizeMax;
    }

    /**
     * Sets the maximum allowed upload size. If negative, there is no maximum.
     *
     * @param sizeMax The maximum allowed size, in bytes, or -1 for no maximum.
     *
     * @see #getSizeMax()
     *
     */
    public void setSizeMax(int sizeMax)
    {
        this._sizeMax = sizeMax;
    }

    /**
     * Returns the size threshold beyond which files are written directly to
     * disk. The default value is 1024 bytes.
     *
     * @return The size threshold, in bytes.
     *
     * @see #setSizeThreshold(int)
     *
     *
     */
    public int getSizeThreshold()
    {
        return _sizeThreshold;
    }

    /**
     * Sets the size threshold beyond which files are written directly to disk.
     *
     * @param sizeThreshold The size threshold, in bytes.
     *
     * @see #getSizeThreshold()
     *
     */
    public void setSizeThreshold(int sizeThreshold)
    {
        this._sizeThreshold = sizeThreshold;
    }

    /**
     * Returns the location used to temporarily store files that are larger than
     * the configured size threshold.
     *
     * @return The path to the temporary file location.
     *
     * @see #setRepositoryPath(String)
     *
     */
    public String getRepositoryPath()
    {
        return _repositoryPath;
    }

    /**
     * Sets the location used to temporarily store files that are larger than
     * the configured size threshold.
     *
     * @param repositoryPath The path to the temporary file location.
     *
     * @see #getRepositoryPath()
     *
     */
    public void setRepositoryPath(String repositoryPath)
    {
        this._repositoryPath = repositoryPath;
    }

    // --------------------------------------------------------- Public methods
    /**
     * Processes an <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>
     * compliant
     * <code>multipart/form-data</code> stream. If files are stored on disk, the
     * path is given by
     * <code>getRepositoryPath()</code>.
     *
     * @param req The servlet request to be parsed.
     *
     * @return A list of <code>FileItem</code> instances parsed from the
     * request, in the order that they were transmitted.
     *
     * @exception FileUploadException if there are problems reading/parsing the
     * request or storing files.
     */
    public List<FileItem> parseRequest(HttpServletRequest request)
            throws PortalProcessException
    {
        return parseRequest(request, getSizeThreshold(), getSizeMax(),
                getRepositoryPath());
    }

    /**
     * Processes an <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>
     * compliant
     * <code>multipart/form-data</code> stream. If files are stored on disk, the
     * path is given by
     * <code>getRepositoryPath()</code>.
     *
     * @param req The servlet request to be parsed. Must be non-null.
     * @param sizeThreshold The max size in bytes to be stored in memory.
     * @param sizeMax The maximum allowed upload size, in bytes.
     * @param path The location where the files should be stored.
     *
     * @return A list of <code>FileItem</code> instances parsed from the
     * request, in the order that they were transmitted.
     *
     * @exception FileUploadException if there are problems reading/parsing the
     * request or storing files.
     */
    public List<FileItem> parseRequest(HttpServletRequest request,
            int pSizeThreshold, int pSizeMax, String pPath)
            throws PortalProcessException
    {
        if (null == request)
        {
            throw new NullPointerException("req parameter");
        }

        String contentType = request.getHeader(CONTENT_TYPE);

        if ((null == contentType) || (!contentType.startsWith(MULTIPART)))
        {
            throw new PortalProcessException("the request doesn't contain a "
                    + MULTIPART_FORM_DATA + " or " + MULTIPART_MIXED
                    + " stream, content type header is " + contentType);
        }
        int requestSize = request.getContentLength();

        if (requestSize == -1)
        {
            throw new PortalProcessException(
                    "the request was rejected because "
                    + "it's size is unknown");
        }

        if (pSizeMax >= 0 && requestSize > pSizeMax)
        {
            throw new PortalProcessException(
                    "the request was rejected because "
                    + "it's size exceeds allowed range");
        }

        return internalParseRequest(request, pSizeThreshold, pPath,
                contentType, requestSize);
    }

    private List<FileItem> internalParseRequest(HttpServletRequest request,
            int pSizeThreshold, String pPath, String contentType,
            int requestSize) throws PortalProcessException
    {
        List<FileItem> items = new ArrayList<FileItem>();

        try
        {
            byte[] boundary = contentType.substring(
                    contentType.indexOf("boundary=")
                    + BOUNDARY_HEADER_PREFIX_SIZE).getBytes();

            InputStream input = (InputStream) request.getInputStream();

            MultipartStream multi = new MultipartStream(input, boundary);
            boolean nextPart = multi.skipPreamble();
            while (nextPart)
            {
                Map<String, String> headers = parseHeaders(multi.readHeaders());
                String fieldName = getFieldName(headers);
                if (fieldName != null)
                {
                    String subContentType = getHeader(headers, CONTENT_TYPE);
                    if (subContentType != null
                            && subContentType.startsWith(MULTIPART_MIXED))
                    {
                        // Multiple files.
                        byte[] subBoundary = subContentType.substring(
                                subContentType.indexOf("boundary=")
                                + BOUNDARY_HEADER_PREFIX_SIZE)
                                .getBytes();
                        multi.setBoundary(subBoundary);
                        boolean nextSubPart = multi.skipPreamble();
                        while (nextSubPart)
                        {
                            headers = parseHeaders(multi.readHeaders());
                            if (getFileName(headers) != null)
                            {
                                FileItem item = createItem(pSizeThreshold,
                                        pPath, headers, requestSize);
                                OutputStream os = item.getOutputStream();
                                try
                                {
                                    multi.readBodyData(os);
                                }
                                finally
                                {
                                    os.close();
                                }
                                item.setFieldName(getFieldName(headers));
                                items.add(item);
                            }
                            else
                            {
                                // Ignore anything but files inside
                                // multipart/mixed.
                                multi.discardBodyData();
                            }
                            nextSubPart = multi.readBoundary();
                        }
                        multi.setBoundary(boundary);
                    }
                    else
                    {
                        if (getFileName(headers) != null)
                        {
                            // A single file.
                            FileItem item = createItem(pSizeThreshold, pPath,
                                    headers, requestSize);
                            OutputStream os = item.getOutputStream();
                            try
                            {
                                multi.readBodyData(os);
                            }
                            finally
                            {
                                os.close();
                            }
                            item.setFieldName(getFieldName(headers));
                            items.add(item);
                        }
                        else
                        {
                            // A form field.
                            FileItem item = createItem(pSizeThreshold, pPath,
                                    headers, requestSize);
                            OutputStream os = item.getOutputStream();
                            try
                            {
                                multi.readBodyData(os);
                            }
                            finally
                            {
                                os.close();
                            }
                            item.setFieldName(getFieldName(headers));
                            item.setIsFormField(true);
                            items.add(item);
                        }
                    }
                }
                else
                {
                    // Skip this part.
                    multi.discardBodyData();
                }
                nextPart = multi.readBoundary();
            }
        }
        catch (IOException e)
        {
            throw new PortalProcessException("Processing of "
                    + MULTIPART_FORM_DATA + " request failed. ", e);
        }

        return items;
    }

    // ------------------------------------------------------ Protected methods
    /**
     * Retrieves the file name from the
     * <code>Content-disposition</code> header.
     *
     * @param headers A <code>Map</code> containing the HTTP request headers.
     *
     * @return The file name for the current <code>encapsulation</code>.
     */
    protected String getFileName(Map<String, String> headers)
    {
        String fileName = null;
        String cd = getHeader(headers, CONTENT_DISPOSITION);
        if (cd.startsWith(FORM_DATA) || cd.startsWith(ATTACHMENT))
        {
            int start = cd.indexOf("filename=\"");
            int end = cd.indexOf('"', start + FILENAME_HEADER_PREFIX_SIZE);
            if (start != -1 && end != -1)
            {
                fileName = cd.substring(start + FILENAME_HEADER_PREFIX_SIZE,
                        end).trim();
            }
        }
        return fileName;
    }

    /**
     * Retrieves the field name from the
     * <code>Content-disposition</code> header.
     *
     * @param headers A <code>Map</code> containing the HTTP request headers.
     *
     * @return The field name for the current <code>encapsulation</code>.
     */
    protected String getFieldName(Map<String, String> headers)
    {
        String fieldName = null;
        String cd = getHeader(headers, CONTENT_DISPOSITION);
        if (cd != null && cd.startsWith(FORM_DATA))
        {
            int start = cd.indexOf("name=\"");
            int end = cd.indexOf('"', start + NAME_HEADER_PREFIX_SIZE);
            if (start != -1 && end != -1)
            {
                fieldName = cd.substring(start + NAME_HEADER_PREFIX_SIZE, end);
            }
        }
        return fieldName;
    }

    /**
     * Creates a new {@link org.apache.commons.fileupload.FileItem} instance.
     *
     * @param sizeThreshold The max size in bytes to be stored in memory.
     * @param path The path for the FileItem.
     * @param headers A <code>Map</code> containing the HTTP request headers.
     * @param requestSize The total size of the request, in bytes.
     *
     * @return A newly created <code>FileItem</code> instance.
     *
     * @exception FileUploadException if an error occurs.
     */
    protected FileItem createItem(int pSizeThreshold, String path,
            Map<String, String> headers, int requestSize)
            throws PortalProcessException
    {
        return FileItem.newInstance(path, getFileName(headers),
                getHeader(headers, CONTENT_TYPE), requestSize, pSizeThreshold);
    }

    /**
     * <p>
     * Parses the
     * <code>header-part</code> and returns as key/value pairs.
     *
     * <p>
     * If there are multiple headers of the same names, the name will map to a
     * comma-separated list containing the values.
     *
     * @param headerPart The <code>header-part</code> of the current
     * <code>encapsulation</code>.
     *
     * @return A <code>Map</code> containing the parsed HTTP request headers.
     */
    protected Map<String, String> parseHeaders(String headerPart)
    {
        Map<String, String> headers = new HashMap<>();
        char[] buffer = new char[MAX_HEADER_SIZE];
        boolean done = false;
        int j = 0;
        int i;
        String header, headerName, headerValue;
        try
        {
            while (!done)
            {
                i = 0;
                // Copy a single line of characters into the buffer,
                // omitting trailing CRLF.
                while (i < 2 || buffer[i - 2] != '\r' || buffer[i - 1] != '\n')
                {
                    buffer[i++] = headerPart.charAt(j++);
                }
                header = new String(buffer, 0, i - 2);
                if (header.equals(""))
                {
                    done = true;
                }
                else
                {
                    if (header.indexOf(':') == -1)
                    {
                        // This header line is malformed, skip it.
                        continue;
                    }
                    headerName = header.substring(0, header.indexOf(':'))
                            .trim().toLowerCase();
                    headerValue = header.substring(header.indexOf(':') + 1)
                            .trim();
                    if (getHeader(headers, headerName) != null)
                    {
                        // More that one heder of that name exists,
                        // append to the list.
                        headers.put(headerName, getHeader(headers, headerName)
                                + ',' + headerValue);
                    }
                    else
                    {
                        headers.put(headerName, headerValue);
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException e)
        {
            // Headers were malformed. continue with all that was
            // parsed.
        }
        return headers;
    }

    /**
     * Returns the header with the specified name from the supplied map. The
     * header lookup is case-insensitive.
     *
     * @param headers A <code>Map</code> containing the HTTP request headers.
     * @param name The name of the header to return.
     *
     * @return The value of specified header, or a comma-separated list if there
     * were multiple headers of that name.
     */
    protected final String getHeader(Map<String, String> headers, String name)
    {
        return headers.get(name.toLowerCase());
    }
}
