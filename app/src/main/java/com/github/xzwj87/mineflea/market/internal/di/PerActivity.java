package com.github.xzwj87.mineflea.market.internal.di;

/**
 * Created by jason on 9/29/16.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the activity to be memorized in the
 * correct component.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity{}