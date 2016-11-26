package com.github.xzwj87.mineflea.utils;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by jason on 11/26/16.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class UserInfoUtilsTest {

    @Test
    public void testEmail(){
        String e1 = "w2zn@gmail.com";
        String e2 = "jason@qq.com";
        String e3 = "@daf";

        assertEquals(UserInfoUtils.isEmailValid(e1),true);
        assertEquals(UserInfoUtils.isEmailValid(e2),true);
        assertEquals(UserInfoUtils.isEmailValid(e3),false);
    }

    @Test
    public void testPhoneNumber(){
        String p1 = "18923238989";
        String p2 = "14589898787";

        assertEquals(UserInfoUtils.isTelNumber(p1),true);
        assertEquals(UserInfoUtils.isTelNumber(p2),true);
    }
}
