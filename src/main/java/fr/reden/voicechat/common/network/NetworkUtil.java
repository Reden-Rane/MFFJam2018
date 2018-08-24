package fr.reden.voicechat.common.network;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

public class NetworkUtil
{

    public static int getAvailablePort() throws SocketException
    {
        int min = 4000;
        int max = 65535;

        for (int port = min; port <= max; port++)
        {
            try (DatagramSocket ds = new DatagramSocket(port))
            {
                ds.setReuseAddress(true);
                return port;
            }
            catch (IOException ignored)
            {
            }
        }

        throw new SocketException("No available port found.");
    }

}
