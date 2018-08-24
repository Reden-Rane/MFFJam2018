package fr.reden.voicechat.common.network.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public interface IUDPPacketProcessor
{
    void processPacket(DatagramSocket socket, DatagramPacket packet);
}
