# JShellShebang
### JShell Shebang is a bash script to facilitate specifying jshell (Java 9 REPL) in the shebang of shell scripts

JShell is a Java [REPL](https://en.wikipedia.org/wiki/Read%E2%80%93eval%E2%80%93print_loop) (Read-Eval-Print-Loop) that ships with the early access version of JDK 9. Like other REPLs (e.g. node in NodeJS, irb in Ruby) it can be run interactively or given a script file and it will execute the code in the script.

Unfortunately, both invoking ```${JAVA_HOME}/java-9-oracle/bin/jshell myScript.jsh``` and putting the path to the jshell binary in the shebang line of ```myScript.jsh``` and invoking ```./myScript.jsh``` have some drawbacks:
- jshell tries to parse the shebang line and throws errors at line 1: ```illegal character: '#'``` and ```illegal start of expression```
- jshell can't receive arguments and make them available to the script
- after running the script, jshell stays open and must manually be exited by the user

This utility gets around those limitations by providing a script to be used as the interpreter directive in Java shell scripts in place of jshell. It does the following:
- prevents parsing errors caused by the shebang line
- makes command-line arguments available to the script in a variable called ```args```, type: ```String[]```
- automatically exits at the end of the script, like other types of shell scripts

# Installation
Put the JShell Shebang script file ```jshell``` from this repo in a directory on the system PATH, such as ```/usr/bin```, and make sure that it's executable (```chmod +x```).

# Usage:
Create a shell script with some Java code in it, and add a shebang line to the top with the path to the JShell Shebang script (```#!/usr/bin/jshell``` if you followed the example in the previous step).

You can now execute Java shell scripts as you would other types of shell scripts.

# Complete example

```
# clone repo and cd into it
$ sudo cp jshell /usr/bin/jshell
# create a script in a file called test.jsh
$ cat test.jsh
#!/usr/bin/jshell

System.out.println("I'm a Java shell script");
if (args.length == 0) {
    System.out.println("I didn't get any arguments");
} else {
    System.out.printf("I got %d arguments: %s\n", args.length, Arrays.toString(args));
}
System.out.println("exiting now");
$
$
$ chmod +x test.jsh
$ ./test.jsh
I'm a Java shell script
I didn't get any arguments
exiting now
$
$
$ jshell test.jsh
I'm a Java shell script
I didn't get any arguments
exiting now
$
$
$ ./test.jsh first second "a \"third\" arg"
I'm a Java shell script
I got 3 arguments: [first, second, a "third" arg]
exiting now
$
```

# Notes
- so far, it's only been tested on Ubuntu
- the path to JDK 9 jshell is hard-coded in the script as "/usr/lib/jvm/java-9-oracle/bin/jshell"
