#!/usr/bin/shebang4j

if (args.length == 0) { System.out.println("Feel free to pass me an argument!"); }
String name = args.length > 0 ? args[0] : "world";
System.out.printf("Hello, %s!\n", name);
