package space.harbour.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import space.harbour.grpc.FileServiceGrpc;
import space.harbour.grpc.Server;
import space.harbour.grpc.Server.Status;

public class FileServer extends FileServiceGrpc.FileServiceImplBase {
  private final static int GRPC_SERVER_PORT = 8090;
  private final String chroot;
  private String pwd = "";

  static public void main(String[] args) throws IOException, InterruptedException {
    String pwd;
    if (args.length == 0 || args[0].length() == 0) {
      pwd = Paths.get(".").toAbsolutePath().normalize().toString();
    } else {
      pwd = args[0];
    }
    var server = ServerBuilder.forPort(GRPC_SERVER_PORT).addService(new FileServer(pwd)).build();

    System.out.printf("Starting server :%d...\n", GRPC_SERVER_PORT);
    server.start();
    System.out.println("Server started!");
    server.awaitTermination();
  }

  FileServer(String chroot) {
    this.chroot = chroot;
    this.pwd = chroot;
  }

  protected boolean checkChroot(String directory) {
    return directory.startsWith(chroot);
  }

  protected String getPath(String directory) {
    if (directory == null || directory.equals("."))
      return pwd;
    if (!directory.startsWith("/")) {
      directory = pwd + "/" + directory;
    }
    return Paths.get(directory).toAbsolutePath().normalize().toString();
  }

  /**
   * execute gRPC server command `ls` - list of file `cat` - the file `cd` -
   * change working directory `pwd` - get current directory `touch` - change file
   * update time or create the new empty file `mkdir` - create directory `rm` -
   * remove file or directory
   * 
   * @param request
   * @param responseObserver
   */
  @Override
  public void execute(Server.Command request, StreamObserver<Server.Response> responseObserver) {
    System.out.printf("exec> %s %s\n", request.getName(), request.getArgs());
    if (request.getName().equals("ls")) {
      executeLs(request, responseObserver);
    } else if (request.getName().equals("pwd")) {
      executePwd(request, responseObserver);
    } else if (request.getName().equals("touch")) {
      executeTouch(request, responseObserver);
    } else if (request.getName().equals("cd")) {
      executeCd(request, responseObserver);
    } else if (request.getName().equals("mkdir")) {
      executeMkdir(request, responseObserver);
    } else if (request.getName().equals("rm")) {
      executeRm(request, responseObserver);
    } else if (request.getName().equals("help")) {
      printHelp(null, responseObserver);
    } else {
      printNotfoundHelp(request, responseObserver);
    }
  }

  protected void printHelp(String message, StreamObserver<Server.Response> responseObserver) {
    Server.Response response = null;
    Server.Status status = Status.OK;
    var helpText = "List of commands\n"
        + " `ls`   - list of files\n"
        + "`cat`   - the file\n"
        + "`cd`    - change working directory\n"
        + "`pwd`   - get current directory\n"
        + "`touch` - change file update time or create the new empty file\n"
        + "`mkdir` - create directory\n"
        + "`rm`    - remove file or directory\n";

    if (message != null) {
      message = message + "\n\n" + helpText;
      status = Status.UNDEFINED;
    } else {
      message = helpText;
    }

    response = Server.Response.newBuilder().setStatus(status).setOutput(message).build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  protected void printNotfoundHelp(Server.Command request, StreamObserver<Server.Response> responseObserver) {
    var message = String.format("undefined command: %s", request.getName());
    System.out.println(message);
    printHelp(message, responseObserver);
  }

  protected void executePwd(Server.Command request, StreamObserver<Server.Response> responseObserver) {
    renderResponse(Status.OK, pwd.startsWith(chroot) ? pwd.substring(chroot.length()) : pwd, responseObserver);
  }

  protected void executeTouch(Server.Command request, StreamObserver<Server.Response> responseObserver) {
    var filename = request.getArgs().strip();
    if (filename.length() == 0) {
      renderResponse(Status.ERROR, "invalid argument", responseObserver);
      return;
    }
    var target = getPath(filename);
    if (!checkChroot(target)) {
      renderResponse(Status.DENY, "no permissions", responseObserver);
      return;
    }
    var file = new File(target);

    // Create new file
    if (!file.exists()) {
      try {
        new FileOutputStream(file).close();
      } catch (IOException e) {
        e.printStackTrace();
        renderResponse(Status.ERROR, e.getMessage(), responseObserver);
        return;
      }
      renderResponse(Status.OK, "new file created", responseObserver);
      return;
    }

    // Update last modified time
    long timestamp = System.currentTimeMillis();
    file.setLastModified(timestamp);

    renderResponse(Status.OK, "", responseObserver);
  }

  protected void executeMkdir(Server.Command request, StreamObserver<Server.Response> responseObserver) {
    var args = request.getArgs().strip().split(" ");
    var directory = args[args.length - 1];
    if (args.length > 2) {
      renderResponse(Status.ERROR, "invalid arguments", responseObserver);
      return;
    }
    if (args[0].startsWith("-")) {
      if (args.length != 2) {
        renderResponse(Status.ERROR, "invalid arguments", responseObserver);
        return;
      }
      if (!args[0].equals("-p")) {
        renderResponse(Status.ERROR, "invalid parameter argument. suports only `-p`", responseObserver);
        return;
      }
    }

    if (directory.length() == 0) {
      renderResponse(Status.ERROR, "invalid argument", responseObserver);
      return;
    }

    var target = getPath(directory);
    if (!checkChroot(target)) {
      renderResponse(Status.DENY, "no permissions", responseObserver);
      return;
    }
    var file = new File(target);

    // Create new file
    if (file.isFile()) {
      renderResponse(Status.DENY, "this is file", responseObserver);
      return;
    }

    if (args.length == 1) {
      if (file.mkdir()) {
        renderResponse(Status.OK, "", responseObserver);
      } else {
        renderResponse(Status.ERROR, "can`t create directory", responseObserver);
      }
    } else {
      if (file.mkdirs()) {
        renderResponse(Status.OK, "", responseObserver);
      } else {
        renderResponse(Status.ERROR, "can`t create directory", responseObserver);
      }
    }
  }

  protected void executeRm(Server.Command request, StreamObserver<Server.Response> responseObserver) {
    var args = request.getArgs().strip().split(" ");
    var larg = args[args.length - 1];
    if (args.length > 2) {
      renderResponse(Status.ERROR, "invalid arguments", responseObserver);
      return;
    }
    if (args[0].startsWith("-")) {
      if (args.length != 2) {
        renderResponse(Status.ERROR, "invalid arguments", responseObserver);
        return;
      }
      if (!args[0].equals("-f")) {
        renderResponse(Status.ERROR, "invalid parameter argument. suports only `-f`", responseObserver);
        return;
      }
    }

    if (larg.length() == 0) {
      renderResponse(Status.ERROR, "invalid argument", responseObserver);
      return;
    }

    var target = getPath(larg);
    if (!checkChroot(target)) {
      renderResponse(Status.DENY, "no permissions", responseObserver);
      return;
    }
    var file = new File(target);

    // Create new file
    if (file.isFile()) {
      if (file.delete()) {
        renderResponse(Status.OK, "successfully deleted", responseObserver);
      } else {
        renderResponse(Status.ERROR, "cant remove file", responseObserver);
      }
      return;
    }

    if (args.length == 1) {
      renderResponse(Status.DENY, "can`t delite directory. Use force parameter `-f`", responseObserver);
    } else {
      if (deleteDir(file)) {
        renderResponse(Status.OK, "", responseObserver);
      } else {
        renderResponse(Status.ERROR, "can`t create directory", responseObserver);
      }
    }
  }

  protected void executeCd(Server.Command request, StreamObserver<Server.Response> responseObserver) {
    var target = getPath(request.getArgs().strip());
    if (!checkChroot(target)) {
      renderResponse(Status.DENY, "no permissions", responseObserver);
      return;
    }
    var file = new File(target);
    if (!file.exists()) {
      renderResponse(Status.ERROR, String.format("'%s' does not exists", target), responseObserver);
      return;
    }
    if (!file.isDirectory()) {
      renderResponse(Status.DENY, String.format("'%s' not a directory", target), responseObserver);
      return;
    }
    pwd = target;
    renderResponse(Status.OK, pwd, responseObserver);
  }

  protected void executeLs(Server.Command request, StreamObserver<Server.Response> responseObserver) {
    var args = request.getArgs().strip().split(" ");
    var larg = args[args.length - 1];
    var asList = false;
    var asAll = false;
    if (args[0].startsWith("-")) {
      asList = args[0].contains("l");
      asAll = args[0].contains("a");
    }
    final var target = getPath(larg.startsWith("-") ? null : larg);
    if (!checkChroot(target)) {
      renderResponse(Status.DENY, "Unavailable directory", responseObserver);
      return;
    }

    // Enumerate files
    try (Stream<Path> walk = Files.walk(Paths.get(target), 1)) {
      final var showAll = asAll;
      var result = walk.filter(f -> showAll || !prepareFilename(target, f).startsWith("."))
          .collect(Collectors.toList());

      if (!asList) {
        renderResponse(Status.OK,
            String.join(" ", result.stream().map(x -> prepareFilename(target, x)).collect(Collectors.toList())),
            responseObserver);
      } else {
        renderResponse(Status.OK,
            String.join("\n", result.stream().map(x -> lsListItem(x, target)).collect(Collectors.toList())),
            responseObserver);
      }
    } catch (IOException e) {
      e.printStackTrace();
      renderResponse(Status.ERROR, e.getMessage(), responseObserver);
    }
  }

  protected String lsListItem(Path x, String target) {
    var file = x.toFile();
    var format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    return String.format("%6d %20s %s", file.length(), format.format(new Date(file.lastModified())),
        prepareFilename(target, x));
  }

  protected void renderResponse(Status status, String message, StreamObserver<Server.Response> responseObserver) {
    var response = Server.Response.newBuilder().setStatus(status).setOutput(message).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  protected String prepareFilename(String target, Path path) {
    var file = path.toFile();
    if (file.isFile())
      return file.getName();
    if (target.equals(path.toString()))
      return ".";
    return file.getName();
  }

  protected boolean deleteDir(File file) {
    File[] contents = file.listFiles();
    if (contents != null) {
      for (File f : contents) {
        if (!Files.isSymbolicLink(f.toPath())) {
          if (!deleteDir(f)) {
            return false;
          }
        }
      }
    }
    return file.delete();
  }
}
