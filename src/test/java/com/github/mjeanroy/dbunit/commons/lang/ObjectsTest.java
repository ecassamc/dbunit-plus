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

package com.github.mjeanroy.dbunit.commons.lang;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ObjectsTest {

	@Test
	public void it_should_compare_objects() {
		assertThat(Objects.equals(null, null)).isTrue();
		assertThat(Objects.equals("foo", null)).isFalse();
		assertThat(Objects.equals(null, "foo")).isFalse();
		assertThat(Objects.equals("foo", "foo")).isTrue();
		assertThat(Objects.equals("foo", "bar")).isFalse();
	}

	@Test
	public void it_should_compute_hash_code() {
		assertThat(Objects.hashCode((Object[]) null)).isZero();
		assertThat(Objects.hashCode("foo")).isNotZero();
		assertThat(Objects.hashCode("foo", "bar")).isNotZero();
	}
}
