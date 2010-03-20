package de.oschoen.bdd.doc;

import org.junit.Test;


public class ExampleTestClazz extends BaseExampleTestClazz {

    private static final String TEST = "title";

    @Test
    public void shouldBeAnotherExample() {
        givenAnExpose(TEST);

        Object aVarExample = new Object();
        
        whenPushButton();

        this.thenTheLinkIsRed();
    }

    public void anotherPublicMethodButNotAnnotatedWithTest() {

    }

    private void givenAnExpose(String title) {

    }

    private void whenPushButton() {

    }

    private void thenTheLinkIsRed() {

    }
}
