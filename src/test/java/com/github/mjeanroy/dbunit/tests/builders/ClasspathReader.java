/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Mickael Jeanroy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mjeanroy.dbunit.tests.builders;

import static com.github.mjeanroy.dbunit.tests.utils.TestUtils.getTestResource;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/**
 * Factory that will return instance of {@link FileReader} using a reference
 * to a file in the classpath.
 */
class ClasspathReader implements ReaderFactory {

	/**
	 * File path.
	 */
	private final String path;

	/**
	 * Create the reader with the path.
	 *
	 * @param path Path.
	 */
	ClasspathReader(String path) {
		this.path = path;
	}

	@Override
	public Reader create() {
		try {
			return new FileReader(getTestResource(path));
		} catch (FileNotFoundException ex) {
			throw new AssertionError(ex);
		}
	}
}
