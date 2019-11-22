# gRPC file client project [![Build Status](https://travis-ci.org/demdxx/test-java-course.svg?branch=master)](https://travis-ci.org/demdxx/test-java-course)

The main idea of the project to provides file access from one PC to another by gRPC protocol.
The first part of the project is the Server side application.
The second one is CLI application with client connect to the server.

## Server features

 * All operations is isolated in specific :root directory which passed as CLI argument
 * `ls`    - List of files for the directory
 * `cat`   - the file
 * `cd`    - change working directory
 * `pwd`   - get current directory
 * `touch` - change file update time or create the new empty file
 * `mkdir` - create directory
 * `rm`    - remove file or directory

## Protocol description

Description of protocol in the `src/main/proto` directory.

> gRPC version 3.

## Notes

Send notifications on MacOS X

```sh
osascript -e "display notification \"This is a message\" with title \"Title\" subtitle \"Subtitle\" sound name \"Funk\""
```
