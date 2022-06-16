package org.tektutor;

public class App {
    public String sayHello() {
        return "Hello Tekton !";
    }

    public static void main ( String[] args ) {
        App app = new App();
        System.out.println ( app.sayHello() );
    }
}
