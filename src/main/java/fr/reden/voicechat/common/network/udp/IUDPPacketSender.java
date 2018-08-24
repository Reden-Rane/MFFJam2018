package fr.reden.voicechat.common.network.udp;

import java.net.DatagramSocket;
import java.net.InetAddress;

public interface IUDPPacketSender
{
    void sendPacket(DatagramSocket socket, byte[] data, InetAddress address, int port);
}
