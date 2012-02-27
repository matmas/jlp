Java Plugin System
==================

Java Plugin System is lightweight plugin framework for Java applications.

Features:

- very simple usage
- no configuration files
- plugins as JARs or built in other JARs
- auto-discovery of plugins in `/plugins` folder
- no versioning of plugins, plugins are collections of extensions - identified with {package.classname}
- extensions as POJOs, extension points as interfaces or abstract classes
- declare extension just by creating a POJO class implementing an extension point (this also serves as dependency declaration)
- support to declare ordering of extensions relative to others using annotations (@InitializeAfter, @InitializeBefore), handy e.g. for menu items

*Demo: package net.matmas.demoeditor*

Example of usage:

    public interface FooBarExtensionPoint {
        public String getName();
    }

    public class Bar implements FooBarExtensionPoint {
        public String getName() {
            return "Bar";
        }
    }
    
    @InitializeBefore({Bar.class})
    public class Foo implements FooBarExtensionPoint {
        public String getName() {
            return "Foo";
        }
    }
    
    public class Main implements BasicExtensionPoint {
        public Main() {
            for (FooBarExtensionPoint extension : ExtensionManager.getAllExtensions(FooBarExtensionPoint.class)) {
                System.out.println(extension.getName());
            }
            // for getting application arguments use ExtensionManager.getApplicationArguments()
        }
    }
    
Output of previous example:

    Foo
    Bar
    

Code license: [GNU GPL v3](http://www.gnu.org/licenses/gpl.html)
