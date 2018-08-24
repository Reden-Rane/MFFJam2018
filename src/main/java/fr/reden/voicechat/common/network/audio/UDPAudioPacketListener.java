package fr.reden.voicechat.common.network.audio;

import fr.reden.voicechat.common.network.udp.IUDPPacketListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPAudioPacketListener implements IUDPPacketListener
{
    //TODO Remplacer par une taille dynamique en fonction de l'échantillonage
    public static final int AUDIO_BUFFER_SIZE = 10000;

    @Override
    public DatagramPacket listen(DatagramSocket socket)
    {
        byte[] data = new byte[AUDIO_BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try
        {
            socket.receive(packet);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return packet;
    }
}
