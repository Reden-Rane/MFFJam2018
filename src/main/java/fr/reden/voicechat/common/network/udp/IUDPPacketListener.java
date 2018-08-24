package fr.reden.voicechat.common.network.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public interface IUDPPacketListener
{

    DatagramPacket listen(DatagramSocket socket);

}
