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

package com.github.mjeanroy.dbunit.core.runner;

import static com.github.mjeanroy.dbunit.tests.db.JdbcQueries.countFrom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.mjeanroy.dbunit.core.jdbc.JdbcConnectionFactory;
import com.github.mjeanroy.dbunit.core.sql.SqlScriptParserConfiguration;
import com.github.mjeanroy.dbunit.exception.DbUnitException;
import com.github.mjeanroy.dbunit.tests.db.EmbeddedDatabaseRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class SqlScriptFunctionTest {

	@ClassRule
	public static EmbeddedDatabaseRule dbRule = new EmbeddedDatabaseRule(true);

	@Rule
	public ExpectedException thrown = none();

	private SqlScriptParserConfiguration configuration;

	private JdbcConnectionFactory factory;

	@Before
	public void setUp() {
		configuration = mock(SqlScriptParserConfiguration.class);
		when(configuration.getDelimiter()).thenReturn(';');
		when(configuration.getLineComment()).thenReturn("--");
		when(configuration.getStartBlockComment()).thenReturn("/*");
		when(configuration.getEndBlockComment()).thenReturn("*/");

		factory = mock(JdbcConnectionFactory.class);
	}

	@Test
	public void it_should_load_script() throws Exception {
		when(factory.getConnection()).thenAnswer(new Answer<Connection>() {
			@Override
			public Connection answer(InvocationOnMock invocationOnMock) throws Throwable {
				return dbRule.getConnection();
			}
		});

		assertThat(countFrom(dbRule.getConnection(), "foo")).isZero();
		assertThat(countFrom(dbRule.getConnection(), "bar")).isZero();

		SqlScriptFunction func = new SqlScriptFunction(factory, configuration);
		func.apply("/sql/data.sql");

		assertThat(countFrom(dbRule.getConnection(), "foo")).isEqualTo(2);
		assertThat(countFrom(dbRule.getConnection(), "bar")).isEqualTo(3);

		verify(factory).getConnection();
	}

	@Test
	public void it_should_wrap_sql_exception() throws Exception {
		Connection connection = mock(Connection.class);
		when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Fail Test"));
		when(factory.getConnection()).thenReturn(connection);

		thrown.expect(DbUnitException.class);

		SqlScriptFunction func = new SqlScriptFunction(factory, configuration);
		func.apply("/sql/data.sql");
	}
}
