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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p> Low level API for processing file uploads.
 *
 * <p> This class can be used to process data streams conforming to MIME
 * 'multipart' format as defined in
 * <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>. Arbitrarily
 * large amounts of data in the stream can be processed under constant
 * memory usage.
 *
 * <p> The format of the stream is defined in the following way:<br>
 *
 * <code>
 *   multipart-body := preamble 1*encapsulation close-delimiter epilogue<br>
 *   encapsulation := delimiter body CRLF<br>
 *   delimiter := "--" boundary CRLF<br>
 *   close-delimiter := "--" boudary "--"<br>
 *   preamble := &lt;ignore&gt;<br>
 *   epilogue := &lt;ignore&gt;<br>
 *   body := header-part CRLF body-part<br>
 *   header-part := 1*header CRLF<br>
 *   header := header-name ":" header-value<br>
 *   header-name := &lt;printable ascii characters except ":"&gt;<br>
 *   header-value := &lt;any ascii characters except CR & LF&gt;<br>
 *   body-data := &lt;arbitrary data&gt;<br>
 * </code>
 *
 * <p>Note that body-data can contain another mulipart entity.  There
 * is limited support for single pass processing of such nested
 * streams.  The nested stream is <strong>required</strong> to have a
 * boundary token of the same length as the parent stream (see {@link
 * #setBoundary(byte[])}).
 *
 * <p>Here is an exaple of usage of this class.<br>
 *
 * <pre>
 *    try {
 *        MultipartStream multipartStream = new MultipartStream(input,
 *                                                              boundary);
 *        boolean nextPart = malitPartStream.skipPreamble();
 *        OutputStream output;
 *        while(nextPart) {
 *            header = chunks.readHeader();
 *            // process headers
 *            // create some output stream
 *            multipartStream.readBodyPart(output);
 *            nextPart = multipartStream.readBoundary();
 *        }
 *    } catch(MultipartStream.MalformedStreamException e) {
 *          // the stream failed to follow required syntax
 *    } catch(IOException) {
 *          // a read or write error occurred
 *    }
 *
 * </pre>
 *
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author <a href="mailto:martinc@apache.org">Martin Cooper</a>
 *
 * @version $Id: MultipartStream.java,v 1.2 2010/08/07 11:06:58 bavodr Exp $
 */
public class MultipartStream
{

	// ----------------------------------------------------- Manifest constants

	/**
	 * The maximum length of <code>header-part</code> that will be
	 * processed (10 kilobytes = 10240 bytes.).
	 */
	public static final int HEADER_PART_SIZE_MAX = 10240;

	/**
	 * The default length of the buffer used for processing a request.
	 */
	protected static final int DEFAULT_BUFSIZE = 4096;

	/**
	 * A byte sequence that marks the end of <code>header-part</code>
	 * (<code>CRLFCRLF</code>).
	 */
	protected static final byte[] HEADER_SEPARATOR = {0x0D, 0x0A, 0x0D, 0x0A};

	/**
	 * A byte sequence that that follows a delimiter that will be
	 * followed by an encapsulation (<code>CRLF</code>).
	 */
	protected static final byte[] FIELD_SEPARATOR = {0x0D, 0x0A};

	/**
	 * A byte sequence that that follows a delimiter of the last
	 * encapsulation in the stream (<code>--</code>).
	 */
	protected static final byte[] STREAM_TERMINATOR = {0x2D, 0x2D};

	// ----------------------------------------------------------- Data members

	/**
	 * The input stream from which data is read.
	 */
	private InputStream _input;

	/**
	 * The length of the boundary token plus the leading <code>CRLF--</code>.
	 */
	private int _boundaryLength;

	/**
	 * The amount of data, in bytes, that must be kept in the buffer in order
	 * to detect delimiters reliably.
	 */
	private int _keepRegion;

	/**
	 * The byte sequence that partitions the stream.
	 */
	private byte[] _boundary;

	/**
	 * The length of the buffer used for processing the request.
	 */
	private int _bufSize;

	/**
	 * The buffer used for processing the request.
	 */
	private byte[] _buffer;

	/**
	 * The index of first valid character in the buffer.
	 * <br>
	 * 0 <= head < bufSize
	 */
	private int _head;

	/**
	 * The index of last valid characer in the buffer + 1.
	 * <br>
	 * 0 <= tail <= bufSize
	 */
	private int _tail;

	// ----------------------------------------------------------- Constructors

	/**
	 * Default constructor.
	 */
	public MultipartStream()
	{
	}

	/**
	 * <p> Constructs a <code>MultipartStream</code> with a custom size buffer.
	 *
	 * <p> Note that the buffer must be at least big enough to contain the
	 * boundary string, plus 4 characters for CR/LF and double dash, plus at
	 * least one byte of data.  Too small a buffer size setting will degrade
	 * performance.
	 *
	 * @param input    The <code>InputStream</code> to serve as a data source.
	 * @param boundary The token used for dividing the stream into
	 *                 <code>encapsulations</code>.
	 * @param bufSize  The size of the buffer to be used, in bytes.
	 */
	public MultipartStream(InputStream input, byte[] boundary, int bufSize)
	{
		this._input = input;
		this._bufSize = bufSize;
		this._buffer = new byte[bufSize];

		// We prepend CR/LF to the boundary to chop trailng CR/LF from
		// body-data tokens.
		this._boundary = new byte[boundary.length + 4];
		this._boundaryLength = boundary.length + 4;
		this._keepRegion = boundary.length + 3;
		this._boundary[0] = 0x0D;
		this._boundary[1] = 0x0A;
		this._boundary[2] = 0x2D;
		this._boundary[3] = 0x2D;
		System.arraycopy(boundary, 0, this._boundary, 4, boundary.length);

		_head = 0;
		_tail = 0;
	}

	/**
	 * <p> Constructs a <code>MultipartStream</code> with a default size buffer.
	 *
	 * @param input    The <code>InputStream</code> to serve as a data source.
	 * @param boundary The token used for dividing the stream into
	 *                 <code>encapsulations</code>.
	 *
	 * @exception IOException when an error occurs.
	 */
	public MultipartStream(InputStream input, byte[] boundary)
			throws IOException
	{
		this(input, boundary, DEFAULT_BUFSIZE);
	}

	// --------------------------------------------------------- Public methods

	/**
	 * Reads a byte from the <code>buffer</code>, and refills it as
	 * necessary.
	 *
	 * @return The next byte from the input stream.
	 *
	 * @exception IOException if there is no more data available.
	 */
	public byte readByte() throws IOException
	{
		// Buffer depleted ?
		if (_head == _tail)
		{
			_head = 0;
			// Refill.
			_tail = _input.read(_buffer, _head, _bufSize);
			if (_tail == -1)
			{
				// No more data available.
				throw new IOException("No more data is available");
			}
		}
		return _buffer[_head++];
	}

	/**
	 * Skips a <code>boundary</code> token, and checks whether more
	 * <code>encapsulations</code> are contained in the stream.
	 *
	 * @return <code>true</code> if there are more encapsulations in
	 *         this stream; <code>false</code> otherwise.
	 *
	 * @exception MalformedStreamException if the stream ends unexpecetedly or
	 *                                     fails to follow required syntax.
	 */
	public boolean readBoundary() throws MalformedStreamException
	{
		byte[] marker = new byte[2];
		boolean nextChunk = false;

		_head += _boundaryLength;
		try
		{
			marker[0] = readByte();
			marker[1] = readByte();
			if (arrayequals(marker, STREAM_TERMINATOR, 2))
			{
				nextChunk = false;
			}
			else if (arrayequals(marker, FIELD_SEPARATOR, 2))
			{
				nextChunk = true;
			}
			else
			{
				throw new MalformedStreamException(
						"Unexpected characters follow a boundary");
			}
		}
		catch (IOException e)
		{
			throw new MalformedStreamException("Stream ended unexpectedly");
		}
		return nextChunk;
	}

	/**
	 * <p>Changes the boundary token used for partitioning the stream.
	 *
	 * <p>This method allows single pass processing of nested multipart
	 * streams.
	 *
	 * <p>The boundary token of the nested stream is <code>required</code>
	 * to be of the same length as the boundary token in parent stream.
	 *
	 * <p>Restoring the parent stream boundary token after processing of a
	 * nested stream is left to the application.
	 *
	 * @param boundary The boundary to be used for parsing of the nested
	 *                 stream.
	 *
	 * @exception IllegalBoundaryException if the <code>boundary</code>
	 *                                     has a different length than the one
	 *                                     being currently parsed.
	 */
	public void setBoundary(byte[] boundary) throws IllegalBoundaryException
	{
		if (boundary.length != _boundaryLength - 4)
		{
			throw new IllegalBoundaryException(
					"The length of a boundary token can not be changed");
		}
		System.arraycopy(boundary, 0, this._boundary, 4, boundary.length);
	}

	/**
	 * <p>Reads the <code>header-part</code> of the current
	 * <code>encapsulation</code>.
	 *
	 * <p>Headers are returned verbatim to the input stream, including the
	 * trailing <code>CRLF</code> marker. Parsing is left to the
	 * application.
	 *
	 * <p><strong>TO DO</strong> allow limiting maximum header size to
	 * protect against abuse.
	 *
	 * @return The <code>header-part</code> of the current encapsulation.
	 *
	 * @exception MalformedStreamException if the stream ends unexpecetedly.
	 */
	public String readHeaders() throws MalformedStreamException
	{
		int i = 0;
		byte[] b = new byte[1];
		// to support multi-byte characters
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int sizeMax = HEADER_PART_SIZE_MAX;
		int size = 0;
		while (i < 4)
		{
			try
			{
				b[0] = readByte();
			}
			catch (IOException e)
			{
				throw new MalformedStreamException("Stream ended unexpectedly");
			}
			size++;
			if (b[0] == HEADER_SEPARATOR[i])
			{
				i++;
			}
			else
			{
				i = 0;
			}
			if (size <= sizeMax)
			{
				baos.write(b[0]);
			}
		}
		return baos.toString();
	}

	/**
	 * <p>Reads <code>body-data</code> from the current
	 * <code>encapsulation</code> and writes its contents into the
	 * output <code>Stream</code>.
	 *
	 * <p>Arbitrary large amounts of data can be processed by this
	 * method using a constant size buffer. (see {@link
	 * #MultipartStream(InputStream,byte[],int) constructor}).
	 *
	 * @param output The <code>Stream</code> to write data into.
	 *
	 * @return the amount of data written.
	 *
	 * @exception MalformedStreamException if the stream ends unexpectedly.
	 * @exception IOException              if an i/o error occurs.
	 */
	public int readBodyData(OutputStream output)
			throws MalformedStreamException, IOException
	{
		boolean done = false;
		int pad;
		int pos;
		int bytesRead;
		int total = 0;
		while (!done)
		{
			// Is boundary token present somewere in the buffer?
			pos = findSeparator();
			if (pos != -1)
			{
				// Write the rest of the data before the boundary.
				output.write(_buffer, _head, pos - _head);
				total += pos - _head;
				_head = pos;
				done = true;
			}
			else
			{
				// Determine how much data should be kept in the
				// buffer.
				if (_tail - _head > _keepRegion)
				{
					pad = _keepRegion;
				}
				else
				{
					pad = _tail - _head;
				}
				// Write out the data belonging to the body-data.
				output.write(_buffer, _head, _tail - _head - pad);

				// Move the data to the beging of the buffer.
				total += _tail - _head - pad;
				System.arraycopy(_buffer, _tail - pad, _buffer, 0, pad);

				// Refill buffer with new data.
				_head = 0;
				bytesRead = _input.read(_buffer, pad, _bufSize - pad);

				// [pprrrrrrr]
				if (bytesRead != -1)
				{
					_tail = pad + bytesRead;
				}
				else
				{
					// The last pad amount is left in the buffer.
					// Boundary can't be in there so write out the
					// data you have and signal an error condition.
					output.write(_buffer, 0, pad);
					output.flush();
					total += pad;
					throw new MalformedStreamException(
							"Stream ended unexpectedly");
				}
			}
		}
		output.flush();
		return total;
	}

	/**
	 * <p> Reads <code>body-data</code> from the current
	 * <code>encapsulation</code> and discards it.
	 *
	 * <p>Use this method to skip encapsulations you don't need or don't
	 * understand.
	 *
	 * @return The amount of data discarded.
	 *
	 * @exception MalformedStreamException if the stream ends unexpectedly.
	 * @exception IOException              if an i/o error occurs.
	 */
	public int discardBodyData() throws MalformedStreamException, IOException
	{
		boolean done = false;
		int pad;
		int pos;
		int bytesRead;
		int total = 0;
		while (!done)
		{
			// Is boundary token present somewere in the buffer?
			pos = findSeparator();
			if (pos != -1)
			{
				// Write the rest of the data before the boundary.
				total += pos - _head;
				_head = pos;
				done = true;
			}
			else
			{
				// Determine how much data should be kept in the
				// buffer.
				if (_tail - _head > _keepRegion)
				{
					pad = _keepRegion;
				}
				else
				{
					pad = _tail - _head;
				}
				total += _tail - _head - pad;

				// Move the data to the beging of the buffer.
				System.arraycopy(_buffer, _tail - pad, _buffer, 0, pad);

				// Refill buffer with new data.
				_head = 0;
				bytesRead = _input.read(_buffer, pad, _bufSize - pad);

				// [pprrrrrrr]
				if (bytesRead != -1)
				{
					_tail = pad + bytesRead;
				}
				else
				{
					// The last pad amount is left in the buffer.
					// Boundary can't be in there so signal an error
					// condition.
					total += pad;
					throw new MalformedStreamException(
							"Stream ended unexpectedly");
				}
			}
		}
		return total;
	}

	/**
	 * Finds the beginning of the first <code>encapsulation</code>.
	 *
	 * @return <code>true</code> if an <code>encapsulation</code> was found in
	 *         the stream.
	 *
	 * @exception IOException if an i/o error occurs.
	 */
	public boolean skipPreamble() throws IOException
	{
		// First delimiter may be not preceeded with a CRLF.
		System.arraycopy(_boundary, 2, _boundary, 0, _boundary.length - 2);
		_boundaryLength = _boundary.length - 2;
		try
		{
			// Discard all data up to the delimiter.
			discardBodyData();

			// Read boundary - if succeded, the stream contains an
			// encapsulation.
			return readBoundary();
		}
		catch (MalformedStreamException e)
		{
			return false;
		}
		finally
		{
			// Restore delimiter.
			System.arraycopy(_boundary, 0, _boundary, 2, _boundary.length - 2);
			_boundaryLength = _boundary.length;
			_boundary[0] = 0x0D;
			_boundary[1] = 0x0A;
		}
	}

	/**
	 * Compares <code>count</code> first bytes in the arrays
	 * <code>a</code> and <code>b</code>.
	 *
	 * @param a     The first array to compare.
	 * @param b     The second array to compare.
	 * @param count How many bytes should be compared.
	 *
	 * @return <code>true</code> if <code>count</code> first bytes in arrays
	 *         <code>a</code> and <code>b</code> are equal.
	 */
	public static boolean arrayequals(byte[] a, byte[] b, int count)
	{
		for (int i = 0; i < count; i++)
		{
			if (a[i] != b[i])
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Searches for a byte of specified value in the <code>buffer</code>,
	 * starting at the specified <code>position</code>.
	 *
	 * @param value The value to find.
	 * @param pos   The starting position for searching.
	 *
	 * @return The position of byte found, counting from beginning of the
	 *         <code>buffer</code>, or <code>-1</code> if not found.
	 */
	protected int findByte(byte value, int pos)
	{
		for (int i = pos; i < _tail; i++)
		{
			if (_buffer[i] == value)
			{
				return i;
			}
		}

		return -1;
	}

	/**
	 * Searches for the <code>boundary</code> in the <code>buffer</code>
	 * region delimited by <code>head</code> and <code>tail</code>.
	 *
	 * @return The position of the boundary found, counting from the
	 *         beginning of the <code>buffer</code>, or <code>-1</code> if
	 *         not found.
	 */
	protected int findSeparator()
	{
		int first;
		int match = 0;
		int maxpos = _tail - _boundaryLength;
		for (first = _head; ((first <= maxpos) && (match != _boundaryLength)); first++)
		{
			first = findByte(_boundary[0], first);
			if (first == -1 || (first > maxpos))
			{
				return -1;
			}
			for (match = 1; match < _boundaryLength; match++)
			{
				if (_buffer[first + match] != _boundary[match])
				{
					break;
				}
			}
		}
		if (match == _boundaryLength)
		{
			return first - 1;
		}
		return -1;
	}

	/**
	 * Thrown to indicate that the input stream fails to follow the
	 * required syntax.
	 */
	public class MalformedStreamException extends IOException
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Constructs a <code>MalformedStreamException</code> with no
		 * detail message.
		 */
		public MalformedStreamException()
		{
			super();
		}

		/**
		 * Constructs an <code>MalformedStreamException</code> with
		 * the specified detail message.
		 *
		 * @param message The detail message.
		 */
		public MalformedStreamException(String message)
		{
			super(message);
		}
	}

	/**
	 * Thrown upon attempt of setting an invalid boundary token.
	 */
	public class IllegalBoundaryException extends IOException
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Constructs an <code>IllegalBoundaryException</code> with no
		 * detail message.
		 */
		public IllegalBoundaryException()
		{
			super();
		}

		/**
		 * Constructs an <code>IllegalBoundaryException</code> with
		 * the specified detail message.
		 *
		 * @param message The detail message.
		 */
		public IllegalBoundaryException(String message)
		{
			super(message);
		}
	}
}