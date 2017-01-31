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

package com.github.mjeanroy.dbunit.tests.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.rules.ExternalResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * Start and Stop embedded database.
 */
public class EmbeddedDatabaseRule extends ExternalResource {

	private final boolean loadScript;

	/**
	 * Embedded Database.
	 */
	private EmbeddedDatabase db;

	public EmbeddedDatabaseRule() {
		this(true);
	}

	public EmbeddedDatabaseRule(boolean loadScript) {
		this.loadScript = loadScript;
	}

	@Override
	protected void before() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.HSQL)
			.generateUniqueName(false)
			.setName("testdb");

		if (loadScript) {
			builder.addScript("classpath:/sql/init.sql");
		}

		db = builder.build();
	}

	@Override
	protected void after() {
		try {
			db.shutdown();
		}
		finally {
			db = null;
		}
	}

	public EmbeddedDatabase getDb() {
		return db;
	}

	public Connection getConnection() {
		try {
			return db.getConnection();
		}
		catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
}
