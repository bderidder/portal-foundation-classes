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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import portal.rt.servlet.PortalProcessException;

/**
 * <p> The default mplementation of the
 * {@link org.apache.commons.fileupload.FileItem FileItem} interface.
 *
 * <p> After retrieving an instance of this class from a {@link
 * org.apache.commons.fileupload.FileUpload FileUpload} instance (see
 * {@link org.apache.commons.fileupload.FileUpload
 * #parseRequest(javax.servlet.http.HttpServletRequest)}), you may
 * either request all contents of file at once using {@link #get()} or
 * request an {@link java.io.InputStream InputStream} with
 * {@link #getInputStream()} and process the file without attempting to load
 * it into memory, which may come handy with large files.
 *
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author <a href="mailto:sean@informage.net">Sean Legassick</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:jmcnally@apache.org">John McNally</a>
 * @author <a href="mailto:martinc@apache.org">Martin Cooper</a>
 *
 * @version $Id: FileItem.java,v 1.1 2005/09/13 11:28:01 bavodr Exp $
 */
public class FileItem
{

	// ----------------------------------------------------------- Data members

	private static final int MAX_ID = 100000000;
	private static final int BUFFER_SIZE = 2048;
	/**
	 * Counter used in unique identifier generation.
	 */
	private static int counter = 0;

	/**
	 * The original filename in the user's filesystem.
	 */
	private String _fileName;

	/**
	 * The content type passed by the browser, or <code>null</code> if
	 * not defined.
	 */
	private String _contentType;

	/**
	 * Cached contents of the file.
	 */
	private byte[] _content;

	/**
	 * Temporary storage location on disk.
	 */
	private File _storeLocation;

	/**
	 * Temporary storage for in-memory files.
	 */
	private ByteArrayOutputStream _byteStream;

	/**
	 * The name of the form field as provided by the browser.
	 */
	private String _fieldName;

	/**
	 * Whether or not this item is a simple form field.
	 */
	private boolean _isFormField;

	// ----------------------------------------------------------- Constructors

	/**
	 * Default constructor.
	 */
	public FileItem()
	{
	}

	/**
	 * Constructs a new <code>DefaultFileItem</code> instance.
	 *
	 * <p>Use {@link #newInstance(String,String,String,int,int)} to
	 * instantiate <code>DefaultFileItem</code>s.
	 *
	 * @param fileName The original filename in the user's filesystem.
	 * @param contentType The content type passed by the browser or
	 * <code>null</code> if not defined.
	 */
	protected FileItem(String fileName, String contentType)
	{
		this._fileName = fileName;
		this._contentType = contentType;
	}

	// ------------------------------- Methods from javax.activation.DataSource

	/**
	 * Returns an {@link java.io.InputStream InputStream} that can be
	 * used to retrieve the contents of the file.
	 *
	 * @return An {@link java.io.InputStream InputStream} that can be
	 *         used to retrieve the contents of the file.
	 *
	 * @exception IOException if an error occurs.
	 */
	public InputStream getInputStream() throws IOException
	{
		if (_content == null)
		{
			if (_storeLocation != null)
			{
				return new FileInputStream(_storeLocation);
			}
			else
			{
				_content = _byteStream.toByteArray();
				_byteStream = null;
			}
		}
		return new ByteArrayInputStream(_content);
	}

	/**
	 * Returns the content type passed by the browser or <code>null</code> if
	 * not defined.
	 *
	 * @return The content type passed by the browser or <code>null</code> if
	 *         not defined.
	 */
	public String getContentType()
	{
		return _contentType;
	}

	/**
	 * Returns the original filename in the client's filesystem.
	 *
	 * @return The original filename in the client's filesystem.
	 */
	public String getName()
	{
		return _fileName;
	}

	// ------------------------------------------------------- FileItem methods

	/**
	 * Provides a hint as to whether or not the file contents will be read
	 * from memory.
	 *
	 * @return <code>true</code> if the file contents will be read
	 *         from memory.
	 */
	public boolean isInMemory()
	{
		return (_content != null || _byteStream != null);
	}

	/**
	 * Returns the size of the file.
	 *
	 * @return The size of the file, in bytes.
	 */
	public long getSize()
	{
		if (_storeLocation != null)
		{
			return _storeLocation.length();
		}
		else if (_byteStream != null)
		{
			return _byteStream.size();
		}
		else
		{
			return _content.length;
		}
	}

	/**
	 * Returns the contents of the file as an array of bytes.  If the
	 * contents of the file were not yet cached in memory, they will be
	 * loaded from the disk storage and cached.
	 *
	 * @return The contents of the file as an array of bytes.
	 */
	public byte[] get()
	{
		if (_content == null)
		{
			if (_storeLocation != null)
			{
				_content = new byte[(int) getSize()];
				try
				{
					FileInputStream fis = new FileInputStream(_storeLocation);
					fis.read(_content);
				}
				catch (Exception e)
				{
					_content = null;
				}
			}
			else
			{
				_content = _byteStream.toByteArray();
				_byteStream = null;
			}
		}

		return _content;
	}

	/**
	 * Returns the contents of the file as a String, using the specified
	 * encoding.  This method uses {@link #get()} to retrieve the
	 * contents of the file.
	 *
	 * @param encoding The character encoding to use.
	 *
	 * @return The contents of the file, as a string.
	 *
	 * @exception UnsupportedEncodingException if the requested character
	 *                                         encoding is not available.
	 */
	public String getString(String encoding)
			throws UnsupportedEncodingException
	{
		return new String(get(), encoding);
	}

	/**
	 * Returns the contents of the file as a String, using the default
	 * character encoding.  This method uses {@link #get()} to retrieve the
	 * contents of the file.
	 *
	 * @return The contents of the file, as a string.
	 */
	public String getString()
	{
		return new String(get());
	}

	/**
	 * Returns the {@link java.io.File} object for the <code>FileItem</code>'s
	 * data's temporary location on the disk. Note that for
	 * <code>FileItem</code>s that have their data stored in memory,
	 * this method will return <code>null</code>. When handling large
	 * files, you can use {@link java.io.File#renameTo(File)} to
	 * move the file to new location without copying the data, if the
	 * source and destination locations reside within the same logical
	 * volume.
	 *
	 * @return The data file, or <code>null</code> if the data is stored in
	 *         memory.
	 */
	public File getStoreLocation()
	{
		return _storeLocation;
	}

	/**
	 * A convenience method to write an uploaded file to disk. The client code
	 * is not concerned whether or not the file is stored in memory, or on disk
	 * in a temporary location. They just want to write the uploaded file to
	 * disk.
	 *
	 * @param file The full path to location where the uploaded file should
	 *             be stored.
	 *
	 * @exception Exception if an error occurs.
	 */
	public void write(String file) throws Exception
	{
		if (isInMemory())
		{
			FileOutputStream fout = null;
			try
			{
				fout = new FileOutputStream(file);
				fout.write(get());
			}
			finally
			{
				if (fout != null)
				{
					fout.close();
				}
			}
		}
		else if (_storeLocation != null)
		{
			/*
			 * The uploaded file is being stored on disk
			 * in a temporary location so move it to the
			 * desired file.
			 */
			if (!_storeLocation.renameTo(new File(file)))
			{
				BufferedInputStream in = null;
				BufferedOutputStream out = null;
				try
				{
					in = new BufferedInputStream(new FileInputStream(
							_storeLocation));
					out = new BufferedOutputStream(new FileOutputStream(file));
					byte[] bytes = new byte[BUFFER_SIZE];
					int s = 0;
					while ((s = in.read(bytes)) != -1)
					{
						out.write(bytes, 0, s);
					}
				}
				finally
				{
					try
					{
						in.close();
					}
					catch (Exception e)
					{
						// ignore
					}
					try
					{
						out.close();
					}
					catch (Exception e)
					{
						// ignore
					}
				}
			}
		}
		else
		{
			/*
			 * For whatever reason we cannot write the
			 * file to disk.
			 */
			throw new PortalProcessException(
					"Cannot write uploaded file to disk!");
		}
	}

	/**
	 * Deletes the underlying storage for a file item, including deleting any
	 * associated temporary disk file. Although this storage will be deleted
	 * automatically when the <code>FileItem</code> instance is garbage
	 * collected, this method can be used to ensure that this is done at an
	 * earlier time, thus preserving system resources.
	 */
	public void delete()
	{
		_byteStream = null;
		_content = null;
		if (_storeLocation != null && _storeLocation.exists())
		{
			_storeLocation.delete();
		}
	}

	/**
	 * Returns the name of the field in the multipart form corresponding to
	 * this file item.
	 *
	 * @return The name of the form field.
	 */
	public String getFieldName()
	{
		return _fieldName;
	}

	/**
	 * Sets the field name used to reference this file item.
	 *
	 * @param fieldName The name of the form field.
	 */
	public void setFieldName(String fieldName)
	{
		this._fieldName = fieldName;
	}

	/**
	 * Determines whether or not a <code>FileItem</code> instance represents
	 * a simple form field.
	 *
	 * @return <code>true</code> if the instance represents a simple form
	 *         field; <code>false</code> if it represents an uploaded file.
	 */
	public boolean isFormField()
	{
		return _isFormField;
	}

	/**
	 * Specifies whether or not a <code>FileItem</code> instance represents
	 * a simple form field.
	 *
	 * @param state <code>true</code> if the instance represents a simple form
	 *              field; <code>false</code> if it represents an uploaded file.
	 */
	public void setIsFormField(boolean state)
	{
		_isFormField = state;
	}

	/**
	 * Returns an {@link java.io.OutputStream OutputStream} that can
	 * be used for storing the contents of the file.
	 *
	 * @return An {@link java.io.OutputStream OutputStream} that can be used
	 *         for storing the contensts of the file.
	 *
	 * @exception IOException if an error occurs.
	 */
	public OutputStream getOutputStream() throws IOException
	{
		if (_storeLocation == null)
		{
			return _byteStream;
		}
		else
		{
			return new FileOutputStream(_storeLocation);
		}
	}

	// --------------------------------------------------------- Public methods

	/**
	 * Removes the file contents from the temporary storage.
	 */
	protected void finalize()
	{
		if (_storeLocation != null && _storeLocation.exists())
		{
			_storeLocation.delete();
		}
	}

	/**
	 * Instantiates a DefaultFileItem. It uses <code>requestSize</code> to
	 * decide what temporary storage approach the new item should take.
	 *
	 * @param path        The path under which temporary files should be
	 *                    created.
	 * @param name        The original filename in the client's filesystem.
	 * @param contentType The content type passed by the browser, or
	 *                    <code>null</code> if not defined.
	 * @param requestSize The total size of the POST request this item
	 *                    belongs to.
	 * @param threshold   The maximum size to store in memory.
	 *
	 * @return A <code>DefaultFileItem</code> instance.
	 */
	public static FileItem newInstance(String path,
			String name,
			String contentType,
			int requestSize,
			int threshold)
	{
		FileItem item = new FileItem(name, contentType);
		if (requestSize > threshold)
		{
			String fileId = getUniqueId();
			fileId = "upload_" + fileId + ".tmp";
			fileId = path + "/" + fileId;
			File f = new File(fileId);
			f.deleteOnExit();
			item._storeLocation = f;
		}
		else
		{
			item._byteStream = new ByteArrayOutputStream();
		}
		return item;
	}

	// -------------------------------------------------------- Private methods

	/**
	 * Returns an identifier that is unique within the class loader used to
	 * load this class, but does not have random-like apearance.
	 *
	 * @return A String with the non-random looking instance identifier.
	 */
	private static String getUniqueId()
	{
		int current;
		synchronized (FileItem.class)
		{
			current = counter++;
		}
		String id = Integer.toString(current);

		// If you manage to get more than 100 million of ids, you'll
		// start getting ids longer than 8 characters.
		if (current < MAX_ID)
		{
			id = ("00000000" + id).substring(id.length());
		}
		return id;
	}

}