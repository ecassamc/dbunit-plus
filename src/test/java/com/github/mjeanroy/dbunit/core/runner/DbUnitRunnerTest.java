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

package com.github.mjeanroy.dbunit.core.runner;

import com.github.mjeanroy.dbunit.tests.db.EmbeddedDatabaseRule;
import com.github.mjeanroy.dbunit.tests.fixtures.TestClassWithDataSet;
import com.github.mjeanroy.dbunit.tests.fixtures.TestClassWithoutDataSet;
import org.dbunit.dataset.IDataSet;
import org.junit.ClassRule;
import org.junit.Test;

import static com.github.mjeanroy.dbunit.tests.db.JdbcQueries.countFrom;
import static com.github.mjeanroy.dbunit.tests.utils.TestUtils.readPrivate;
import static org.assertj.core.api.Assertions.assertThat;

public class DbUnitRunnerTest {

	@ClassRule
	public static EmbeddedDatabaseRule dbRule = new EmbeddedDatabaseRule();

	@Test
	public void it_should_create_runner_and_read_data_set_on_class() throws Exception {
		Class klass = TestClassWithDataSet.class;
		String testMethod = "method1";

		DbUnitRunner runner = new DbUnitRunner(klass, testMethod);

		assertThat(readPrivate(runner, "testClass", Class.class))
			.isNotNull()
			.isSameAs(klass);

		assertThat(readPrivate(runner, "testMethod", String.class))
			.isNotNull()
			.isSameAs(testMethod);

		IDataSet dataSet = readPrivate(runner, "dataSet", IDataSet.class);
		assertThat(dataSet).isNotNull();
		assertThat(dataSet.getTableNames())
			.isNotNull()
			.hasSize(2)
			.contains("foo", "bar");
	}

	@Test
	public void it_should_create_runner_and_read_data_set_on_method() throws Exception {
		Class klass = TestClassWithDataSet.class;
		String testMethod = "method2";

		DbUnitRunner runner = new DbUnitRunner(klass, testMethod);

		assertThat(readPrivate(runner, "testClass", Class.class))
			.isNotNull()
			.isSameAs(klass);

		assertThat(readPrivate(runner, "testMethod", String.class))
			.isNotNull()
			.isSameAs(testMethod);

		IDataSet dataSet = readPrivate(runner, "dataSet", IDataSet.class);
		assertThat(dataSet).isNotNull();
		assertThat(dataSet.getTableNames())
			.isNotNull()
			.hasSize(1)
			.contains("foo");
	}

	@Test
	public void it_should_create_runner_and_not_fail_if_data_set_cannot_be_found() throws Exception {
		Class klass = TestClassWithoutDataSet.class;
		String testMethod = "method1";

		DbUnitRunner runner = new DbUnitRunner(klass, testMethod);

		assertThat(readPrivate(runner, "testClass", Class.class))
			.isNotNull()
			.isSameAs(klass);

		assertThat(readPrivate(runner, "testMethod", String.class))
			.isNotNull()
			.isSameAs(testMethod);

		IDataSet dataSet = readPrivate(runner, "dataSet", IDataSet.class);
		assertThat(dataSet).isNull();
	}

	@Test
	public void it_should_load_data_set() throws Exception {
		Class klass = TestClassWithDataSet.class;
		String testMethod = "method1";
		DbUnitRunner runner = new DbUnitRunner(klass, testMethod);

		// Setup Operation
		runner.beforeTest(dbRule.getConnection());

		assertThat(countFrom(dbRule.getConnection(), "foo")).isEqualTo(2);
		assertThat(countFrom(dbRule.getConnection(), "bar")).isEqualTo(3);

		// Tear Down Operation
		runner.afterTest(dbRule.getConnection());

		assertThat(countFrom(dbRule.getConnection(), "foo")).isZero();
		assertThat(countFrom(dbRule.getConnection(), "bar")).isZero();
	}

	@Test
	public void it_should_load_data_set_with_custom_operation() throws Exception {
		Class klass = TestClassWithDataSet.class;
		String testMethod = "method3";
		DbUnitRunner runner = new DbUnitRunner(klass, testMethod);

		// Setup Operation
		runner.beforeTest(dbRule.getConnection());

		assertThat(countFrom(dbRule.getConnection(), "foo")).isZero();
		assertThat(countFrom(dbRule.getConnection(), "bar")).isZero();

		// Tear Down Operation
		runner.afterTest(dbRule.getConnection());

		assertThat(countFrom(dbRule.getConnection(), "foo")).isZero();
		assertThat(countFrom(dbRule.getConnection(), "bar")).isZero();
	}
}