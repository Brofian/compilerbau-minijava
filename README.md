# Compilerbau MiniJava
Source of the minijava compiler, created as a practical exam to the compilerbau course.

## Prerequisites
- A working sbt installation in version >= 1.10.0

## Usage
Every interaction with the raw program will happen via sbt. This can either be done directly in the sbt
shell or as parameters to the sbt executable otherwise. The following examples will use the second method:

| Command                             | Explanation                                                                                                                                              |
|-------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| `sbt clean`                         | Clean up generated files                                                                                                                                 | 
| `sbt clean compile`                 | Clean up and regenerate ANTLR4 grammar files                                                                                                             | 
| `sbt "run"`                         | The most basic way of running the compiler. This is typically extended with some options, as described in the next examples                              | 
| `sbt "run --help"`                  | Show the help text with the structure of the command, all available parameters and some examples                                                         | 
| `sbt "run [OPTIONS] [-- FILES...]"` | The compiler can be used by specifying some options and optionally a list of files, separated from the options by two dashes (further explanation below) |      
| `sbt "~run ..."`                    | Same as the examples above, but automatically reloading changed files and restarting the execution                                                       |      

### Options
When using the compiler, there are multiple options, that can be defined via the command line arguments:
- `--help` Show a help text about the available arguments and exit
- `-v`/`-vv`/`-vvv` Setting the Logger output to be errors only (default, if missing), then also notices, infos and debug by adding more `v`
- `-o <path>` Changing the output directory, which will be the root of the generated .class files. Default is `./out`
- `-i <path>` Adding a file (typically `.java`) or directory to the list of input, that will be read and compiled. This option can be used multiple times 

Once a double dash `--` is encountered, all following inputs are not parsed but directly used as input files. This can 
make lists of files more compact and separate from other options. For example, these two commands are equal:
- `sbt "run -i input/operations.java -i input/minimal.java"`
- `sbt "run -- input/operations.java input/minimal.java"`