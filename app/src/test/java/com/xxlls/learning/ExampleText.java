package com.xxlls.learning;

import java.lang.ref.WeakReference;

public class ExampleText {

    private String str_example;

    static class ContentTest {

        WeakReference<ExampleText> weakReference;

        public ContentTest(ExampleText exampleText) {
            weakReference = new WeakReference<>(exampleText);
        }
    }
}
