package com.github.xzwj87.mineflea;

import com.github.xzwj87.mineflea.market.data.cache.FileCacheTest;
import com.github.xzwj87.mineflea.market.data.repository.MineFleaRepositoryTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by jason on 10/29/16.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({MineFleaRepositoryTest.class, FileCacheTest.class})
public class MineFleaUnitTestSuite {}
