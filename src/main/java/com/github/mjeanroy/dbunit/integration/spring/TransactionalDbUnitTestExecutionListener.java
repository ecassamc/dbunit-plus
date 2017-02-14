/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2017 Mickael Jeanroy
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

package com.github.mjeanroy.dbunit.integration.spring;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * Provide integration between spring {@link TransactionalTestExecutionListener} and
 * custom {@link DbUnitTestExecutionListener}:
 * <ol>
 *   <li>{@link TransactionalTestExecutionListener} will be executed first.</li>
 *   <li>{@link DbUnitTestExecutionListener} will be executed last.</li>
 * </ol>
 */
public class TransactionalDbUnitTestExecutionListener extends CompositeTestExecutionListener {

	/**
	 * Create listeners with defaults {@link TestExecutionListener} list.
	 */
	public TransactionalDbUnitTestExecutionListener() {
		super(listeners());
	}

	private static List<TestExecutionListener> listeners() {
		TestExecutionListener transactionalTestExecutionListener = new TransactionalTestExecutionListener();
		TestExecutionListener dbUnitTestExecutionListener = new DbUnitTestExecutionListener();
		return asList(transactionalTestExecutionListener, dbUnitTestExecutionListener);
	}
}
