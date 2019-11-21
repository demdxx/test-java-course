package space.harbour.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

  static public void main(String [] args) throws IOException, InterruptedException {
    String pwd;
    if (args.length == 0 || args[0].length() == 0) {
      pwd = Paths.get(".").toAbsolutePath().normalize().toString();
    } else {
      pwd = args[0];
    }
    var server = ServerBuilder.forPort(GRPC_SERVER_PORT)
        .addService(new FileServer(pwd)).build();

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
    return true;
  }

  protected String getPath(String directory) {
    if (directory == null || directory.equals("."))
      return pwd;
    if (!directory.startsWith("/")) {
      directory = pwd + "/" + directory;
    }
    return Paths.get(directory).toAbsolutePath().normalize().toString();
  }

  @Override
  public void execute(Server.Command request, StreamObserver<Server.Response> responseObserver) {
    System.out.printf("exec> %s %s\n", request.getName(), request.getArgs());
    if (request.getName().equals("ls")) {
      executeLs(request, responseObserver);
    } else if (request.getName().equals("pwd")) {
      executePwd(request, responseObserver);
    } else if (request.getName().equals("cd")) {
      executeCd(request, responseObserver);
    } else if (request.getName().equals("help")) {
      printHelp(null, responseObserver);
    } else {
      printNotfoundHelp(request, responseObserver);
    }
  }

  protected void printHelp(String message, StreamObserver<Server.Response> responseObserver) {
    Server.Response response = null;
    Server.Status status = Status.OK;
    var helpText = "Command list\n"+
      " ls - list of files\n";

    if (message != null) {
      message = message + "\n\n" + helpText;
      status = Status.UNDEFINED;
    } else {
      message = helpText;
    }
  
    response = Server.Response.newBuilder().
      setStatus(status).
      setOutput(message).build();
  
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  protected void printNotfoundHelp(Server.Command request, StreamObserver<Server.Response> responseObserver) {
    var message = String.format("undefined command: %s", request.getName());
    System.out.println(message);
    printHelp(message, responseObserver);
  }

  protected void executePwd(Server.Command request, StreamObserver<Server.Response> responseObserver) {
    renderResponse(Status.OK, pwd, responseObserver);
  }

  protected void executeCd(Server.Command request, StreamObserver<Server.Response> responseObserver) {
    var target = getPath(request.getArgs());
    if (!checkChroot(target)) {
      renderResponse(Status.DENY, "Unavailable directory", responseObserver);
      return;
    }
    pwd = target;
    renderResponse(Status.OK, pwd, responseObserver);
  }

  protected void executeLs(Server.Command request, StreamObserver<Server.Response> responseObserver) {
    var args = request.getArgs().strip().split(" ");
    var larg = args[args.length-1];
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
      var result = walk
          .filter(f -> showAll || !prepareFilename(target, f).startsWith("."))
          .collect(Collectors.toList());
  
      if (!asList) {
        renderResponse(Status.OK,
          String.join(" ", result.stream().
            map(x -> prepareFilename(target, x)).
            collect(Collectors.toList())),
          responseObserver);
      } else {
        renderResponse(Status.OK,
          String.join("\n", result.stream().
            map(x -> String.format("%s - %s", x.toFile().length(), prepareFilename(target, x))).
            collect(Collectors.toList())),
          responseObserver);
      }
      return;
    } catch (IOException e) {
      e.printStackTrace();
      renderResponse(Status.ERROR, e.getMessage(), responseObserver);
    }
  }

  protected void renderResponse(Status status, String message, StreamObserver<Server.Response> responseObserver) {
    var response = Server.Response.newBuilder().
      setStatus(status).
      setOutput(message).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  protected String prepareFilename(String target, Path path) {
    if (target.equals(path.toString())) {
      return ".";
    }
    return path.toFile().getName();
  }
}
