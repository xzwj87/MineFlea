package com.github.xzwj87.mineflea;

import com.github.xzwj87.mineflea.market.data.cache.FileCacheTest;
import com.github.xzwj87.mineflea.market.data.repository.DataRepositoryTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by jason on 10/29/16.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({DataRepositoryTest.class, FileCacheTest.class})
public class MineFleaUnitTestSuite {}
