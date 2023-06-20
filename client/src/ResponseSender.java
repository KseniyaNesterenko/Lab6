import commands.CommandContainer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Base64;

public class ResponseSender {

    private final DatagramChannel clientChannel;

    public ResponseSender(DatagramChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    public void sendContainer(CommandContainer commandContainer, InetSocketAddress inetSocketAddress) throws IOException {

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        byteBuffer.clear();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);

        oos.writeObject(commandContainer);

        byteBuffer.put(Base64.getEncoder().withoutPadding().encode(byteArrayOutputStream.toByteArray()));

        byteBuffer.flip();

        clientChannel.send(byteBuffer, inetSocketAddress);
    }
}