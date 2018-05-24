# Shebang4J
#### Shebang4J is a bash script to facilitate specifying jshell (Java 9 REPL) in the shebang of shell scripts

JShell is a Java [REPL](https://en.wikipedia.org/wiki/Read%E2%80%93eval%E2%80%93print_loop) (Read-Eval-Print-Loop) that 
ships with JDK 9. Like other REPLs (e.g. node in NodeJS, irb in Ruby) it can be run interactively or given a script file
and it will execute the code in the script.

Unfortunately, both invoking ```${JAVA_HOME}/bin/jshell myScript.jsh``` and putting the path to the jshell binary in the 
shebang line of ```myScript.jsh``` and invoking ```./myScript.jsh``` have some drawbacks:
- jshell tries to parse the shebang line and throws errors at line 1: ```illegal character: '#'``` and ```illegal start 
of expression```
- jshell can't receive arguments and make them available to the script
- after running the script, jshell stays open and must manually be exited by the user

This utility gets around those limitations by providing a script to be used as the interpreter directive in Java shell 
scripts in place of jshell. It does the following:
- prevents parsing errors caused by the shebang line
- makes command-line arguments available to the script in a variable called ```args```, type: ```String[]```
- automatically exits at the end of the script, like other types of shell scripts (jshell might still stay open under 
certain conditions, e.g. OutOfMemoryError)

# Installation
Put the Shebang4J script file ```shebang4j``` from this repo in a directory on the system PATH, such as ```/usr/bin```, 
and make sure that it's executable (```chmod +x```).

# Usage:
Create a shell script with some Java code in it, and add a shebang line to the top with the path to the JShell Shebang 
script (```#!/usr/bin/shebang4j``` if you followed the example in the previous step).

You can now execute Java shell scripts as you would other types of shell scripts.

# Complete example

```bash
# clone repo and cd into it
$ sudo cp shebang4j /usr/bin/shebang4j
$ sudo chmod +x /usr/bin/shebang4j
# create a script in a file called test.jsh
$ cat test.jsh
#!/usr/bin/shebang4j

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

# Options to JShell

The jshell binary can take options when started interactively or with a script (run ```jshell --help``` for info). To 
pass options to the jshell binary, you have to invoke Shebang4J, passing it options and a script
(```shebang4j --no-startup myScript```). You can't pass options to jshell by directly invoking an executable script 
(```./myScript```).

#### Examples
```bash
$ ls
commons-math3-3.6.1.jar  withoutCp.jsh  withCp.jsh
$ cat withoutCp.jsh
#!/usr/bin/shebang4j

# jar needs to be included in the classpath on the command line
import org.apache.commons.math3.analysis.function.Add;

Add a = new Add();
System.out.println(a.value(Double.parseDouble(args[0]), Double.parseDouble(args[1])));
$
$ shebang4j --class-path commons-math3-3.6.1.jar withoutCp.jsh 3 4
7.0
$
$
$
# the same script, with the classpath in the script itself
$ cat withCp.jsh
#!/usr/bin/shebang4j

/env -class-path commons-math3-3.6.1.jar

import org.apache.commons.math3.analysis.function.Add;

Add a = new Add();
System.out.println(a.value(Double.parseDouble(args[0]), Double.parseDouble(args[1])));
$
$ ./withCp.jsh 5 6
11.0
$
```

# Notes
- so far, it's only been tested on Ubuntu
- The script tries to find the jshell binary by looking in the same directory as ```java```. If you have JDK 9
installed, but a different version set as the default, it will try to find the binary in any nearby directory named like
"java-9" or "java-1.9".
- with the jshell binary, you can pass multiple arguments, each being a script that gets evaluated. Shebang4J
doesn't allow that behavior since it treats arguments after the script as arguments to the script
- calling ```System.exit(int)``` in a script doesn't exit jshell and ```/exit``` doesn't take arguments, so there isn't a way to use exit codes in scripts
