package fr.reden.voicechat.common.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPPacketSender implements IUDPPacketSender
{
    @Override
    public void sendPacket(DatagramSocket socket, byte[] data, InetAddress address, int port)
    {
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        try
        {
            socket.send(packet);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
