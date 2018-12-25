package com.tngtech.archunit.visual;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.visual.testclasses.SomeClass;
import com.tngtech.archunit.visual.testclasses.subpkg.ThirdSubPkgClass;
import org.junit.Test;

import static com.tngtech.archunit.core.domain.TestUtils.importClasses;
import static com.tngtech.archunit.testutil.Assertions.assertThat;

public class JsonJavaClassTest {
    @Test
    public void getChild_returns_self_or_child_by_full_name() {
        JavaClasses classes = importClasses(SomeClass.class, SomeClass.InnerClass.class);

        String root = SomeClass.class.getPackage().getName();
        JsonJavaPackage jsonPackage = new JsonJavaPackage(root, root);
        JsonJavaClass jsonClass = new JsonJavaClass(classes.get(SomeClass.class));
        jsonPackage.insert(jsonClass);
        JsonJavaClass jsonInnerClass = new JsonJavaClass(classes.get(SomeClass.InnerClass.class));
        jsonClass.insert(jsonInnerClass);

        assertThat(jsonClass.getChild(SomeClass.class.getName())).contains(jsonClass);
        assertThat(jsonClass.getChild(SomeClass.InnerClass.class.getName())).contains(jsonInnerClass);

        assertThat(jsonClass.getChild("something.not.There")).isAbsent();
    }

    @Test
    public void testCreateEnclosingClass() {
        JavaClasses classes = importClasses(SomeClass.InnerClass.class);
        JsonJavaClass jsonJavaClass = new JsonJavaClass(classes.get(SomeClass.InnerClass.class));
        JsonJavaClass enclosingClass = JsonJavaClass.createEnclosingClassOf(jsonJavaClass, "com.tngtech.archunit.visual.testclasses");

        assertThat(enclosingClass.fullName).isEqualTo("com.tngtech.archunit.visual.testclasses.SomeClass");
        assertThat(enclosingClass.name).isEqualTo("SomeClass");
        assertThat(enclosingClass.children.contains(jsonJavaClass)).isTrue();
    }

    @Test
    public void testCreateTwoEnclosingClasses() {
        JavaClasses classes = importClasses(ThirdSubPkgClass.InnerClass1.InnerClass2.class);
        JsonJavaClass jsonJavaClass = new JsonJavaClass(classes.get(ThirdSubPkgClass.InnerClass1.InnerClass2.class));
        JsonJavaClass enclosingClass = JsonJavaClass.createEnclosingClassOf(jsonJavaClass, "com.tngtech.archunit.visual.testclasses.subpkg");

        assertThat(enclosingClass.fullName).isEqualTo("com.tngtech.archunit.visual.testclasses.subpkg.ThirdSubPkgClass");
        assertThat(enclosingClass.name).isEqualTo("ThirdSubPkgClass");

        JsonJavaElement secondEnclosingClass = enclosingClass.children.iterator().next();
        assertThat(secondEnclosingClass.fullName).isEqualTo("com.tngtech.archunit.visual.testclasses.subpkg.ThirdSubPkgClass$InnerClass1");
        assertThat(secondEnclosingClass.name).isEqualTo("InnerClass1");

        assertThat(secondEnclosingClass.children.contains(jsonJavaClass)).isTrue();
    }
}
