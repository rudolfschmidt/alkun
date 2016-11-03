package com.rudolfschmidt.alkun;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FiltersTest {

	@Test
	public void equal() {
		assertTrue(Filters.filterPath("/", "/"));
		assertTrue(Filters.filterPath("/a", "/a"));
		assertTrue(Filters.filterPath("/a/b", "/a/b"));
	}

	@Test
	public void noTEqual() {
		assertFalse(Filters.filterPath("/a", "/"));
		assertFalse(Filters.filterPath("/a/", "/a"));
		assertFalse(Filters.filterPath("/a/b", "/a/b/"));
	}

	@Test
	public void asterixTrue() {
		assertTrue(Filters.filterPath("/*", "/a"));
		assertTrue(Filters.filterPath("/*", "/a/"));
		assertTrue(Filters.filterPath("/*", "/a/b"));
		assertTrue(Filters.filterPath("/*", "/a/b/"));
	}

	@Test
	public void asterixSubPathTrue() {
		assertTrue(Filters.filterPath("/a/*", "/a/"));
		assertTrue(Filters.filterPath("/a/*", "/a/b"));
		assertTrue(Filters.filterPath("/a/*", "/a/b/"));
		assertTrue(Filters.filterPath("/a/*", "/a/b/c"));
	}

	@Test
	public void asterixFalse() {
		assertFalse(Filters.filterPath("/a*", "/a"));
		assertFalse(Filters.filterPath("/a/*", "/a"));
	}

	@Test
	public void paramsTrue() {
		assertTrue(Filters.filterPath("/a", "/a"));
		assertTrue(Filters.filterPath("/:foo", "/a"));
		assertTrue(Filters.filterPath("/:foo", "/ab"));
		assertTrue(Filters.filterPath("/:foo", "/abc"));
		assertTrue(Filters.filterPath("/a/:foo", "/a/bc"));
		assertTrue(Filters.filterPath("/a/:foo/bc", "/a/foo/bc"));
		assertTrue(Filters.filterPath("/a/:foo/bc/:bar", "/a/foo/bc/bar"));
	}

	@Test
	public void paramsFalse() {
		assertFalse(Filters.filterPath("/a/", "/a"));
		assertFalse(Filters.filterPath("/:foo", "/a/b"));
		assertFalse(Filters.filterPath("/:foo", "/a/b/c"));
		assertFalse(Filters.filterPath("/a/:foo", "/a/bc/d"));
		assertFalse(Filters.filterPath("/a/:foo/bc/:bar", "/a/foo/bcd/bar"));
	}

}
