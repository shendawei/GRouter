package com.gome.mobile.frame.router.utils;

import com.gome.mobile.frame.router.annotation.IParam;
import com.gome.mobile.frame.router.annotation.IRoute;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.gome.mobile.frame.router.utils.ReflectUtil.findDeclaredMethodsByAnnotation;
import static com.gome.mobile.frame.router.utils.ReflectUtil.getInheritedInterfaces;
import static com.gome.mobile.frame.router.utils.ReflectUtil.getParameterAnnotation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

interface TestInterfaceA {
    @IRoute(uri = "/test/path")
    void foo();
}

interface TestInterfaceB {
}

interface TestInterfaceC extends TestInterfaceA, TestInterfaceB {
}

interface TestInterfaceD {
}

class TestClassA implements TestInterfaceD {
}

class TestClassB extends TestClassA implements TestInterfaceC {
    @Override
    @IRoute(uri = "/test/path")
    public void foo() {
    }
}

class TestClassC {
    public void foo(@IParam("a") int a) {
    }
}

public class ReflectUtilTest {

    @Test
    public void getInterfaceGenericClassesTest() {
    }

    @Test
    public void getInheritedInterfacesTest() {
        assertEquals(0, getInheritedInterfaces(null).size());
        assertEquals(0, getInheritedInterfaces(TestInterfaceA.class).size());
        assertEquals(2, getInheritedInterfaces(TestInterfaceC.class).size());
        assertEquals(4, getInheritedInterfaces(TestClassB.class).size());
        assertEquals(0, getInheritedInterfaces(TestClassC.class).size());
    }

    @Test
    public void findDeclaredMethodsByAnnotationTest() {
        assertEquals(1, findDeclaredMethodsByAnnotation(TestInterfaceA.class, IRoute.class).size());
        assertEquals(0, findDeclaredMethodsByAnnotation(TestInterfaceB.class, IRoute.class).size());
        assertEquals(1, findDeclaredMethodsByAnnotation(TestClassB.class, IRoute.class).size());
    }

    @Test
    public void getParameterAnnotationTest() throws Exception {
        Method method = TestClassC.class.getDeclaredMethod("foo", int.class);
        IParam anno = getParameterAnnotation(method, 0, IParam.class);
        assertNotNull(anno);
    }
}