#!/usr/bin/shebang4j

System.out.println("I'm a Java shell script");
if (args.length == 0) {
    System.out.println("I didn't get any arguments");
} else {
    System.out.printf("I got %d arguments: %s\n", args.length, Arrays.toString(args));
}
System.out.println("exiting now");
