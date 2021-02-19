package com.neotys.neoload.model.v3.writers.neoload.settings;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.Map;

public class TokenReplacingReader extends Reader {

	private static final String OPERATION_NOT_SUPPORTED = "Operation Not Supported";

	private PushbackReader pushbackReader = null;
	private Map<String,String> tokenResolver = null;
	private StringBuilder tokenNameBuffer = new StringBuilder();
	private String tokenValue = null;
	private int tokenValueIndex = 0;

	public TokenReplacingReader(final Reader source, final Map<String,String> tokenResolver) {
		this.pushbackReader = new PushbackReader(source, 2);
		this.tokenResolver = tokenResolver;
	}

	@Override
	public int read(CharBuffer target) throws IOException {
		throw new IllegalAccessError(OPERATION_NOT_SUPPORTED);
	}

	@Override
	public int read() throws IOException {
		if (this.tokenValue != null) {
			if (this.tokenValueIndex < this.tokenValue.length()) {
				return this.tokenValue.charAt(this.tokenValueIndex++);
			}
			if (this.tokenValueIndex == this.tokenValue.length()) {
				this.tokenValue = null;
				this.tokenValueIndex = 0;
			}
		}

		int data = this.pushbackReader.read();
		if (data != '@')
			return data;
		data = this.pushbackReader.read();
		while (data != '@') {
			this.tokenNameBuffer.append((char) data);
			data = this.pushbackReader.read();
		}
		final String tokenName = this.tokenNameBuffer.toString();
		tokenNameBuffer = new StringBuilder();

		this.tokenValue = this.tokenResolver.get(tokenName);

		if (this.tokenValue == null) {
			this.tokenValue = "@" + tokenName + "@";
		}
		if (this.tokenValue.length() == 0) {
			return read();
		}
		return this.tokenValue.charAt(this.tokenValueIndex++);

	}

	@Override
	public int read(final char[] cbuf) throws IOException {
		return read(cbuf, 0, cbuf.length);
	}

	@Override
	public int read(final char[] cbuf, final int off, final int len) throws IOException {
		int charsRead = 0;
		for (int i = 0 ; i < len ; i++) {
			int nextChar = read();
			if (nextChar == -1) {
				if (charsRead == 0) {
					charsRead = -1;
				}
				break;
			}
			charsRead = i + 1;
			cbuf[off + i] = (char) nextChar;
		}
		return charsRead;
	}

	@Override
	public void close() throws IOException {
		this.pushbackReader.close();
	}

	@Override
	public long skip(long n) throws IOException {
		throw new IllegalAccessError(OPERATION_NOT_SUPPORTED);
	}

	@Override
	public boolean ready() throws IOException {
		return this.pushbackReader.ready();
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public void mark(final int readAheadLimit) throws IOException {
		throw new IllegalAccessError(OPERATION_NOT_SUPPORTED);
	}

	@Override
	public void reset() throws IOException {
		throw new IllegalAccessError(OPERATION_NOT_SUPPORTED);
	}
}