package space.harbour.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import space.harbour.grpc.FileServiceGrpc;
import space.harbour.grpc.Server;

public class FileClient {
  private final static String GRPC_CLIENT_HOST = "localhost";
  private final static int GRPC_CLIENT_PORT = 8090;

  public static void main(String[] args) throws InterruptedException {
    var console = new BufferedReader(new InputStreamReader(System.in), 1024 * 64);
    ManagedChannel channel = ManagedChannelBuilder.forAddress(GRPC_CLIENT_HOST, GRPC_CLIENT_PORT).usePlaintext()
        .build();

    var stub = FileServiceGrpc.newBlockingStub(channel);
    System.out.printf("Run GRPC client %s:%d\n", GRPC_CLIENT_HOST, GRPC_CLIENT_PORT);

    try {
      while (true) {
        System.out.print("> ");
        var cmd = console.readLine().strip();
        if (cmd.length() == 0) continue;
        var cmdargs = cmd.split(" ", 2);
        var command = Server.Command.newBuilder()
          .setName(cmdargs[0].toLowerCase());
        if (cmdargs.length > 1)
          command = command.setArgs(cmdargs[1].strip());
        var response = stub.execute(command.build());
        System.out.printf("%s\n%s\n", response.getStatus().toString(), response.getOutput());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    channel.shutdown();
  }
}
