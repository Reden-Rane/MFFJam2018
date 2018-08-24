package fr.reden.voicechat.common.network.udp;

import fr.reden.voicechat.common.VoiceChatMod;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public abstract class UDPSocket extends Thread
{
    private boolean shouldClose = false;

    private final int port;

    private final IUDPPacketListener packetListener;
    private final IUDPPacketProcessor packetProcessor;
    private final IUDPPacketSender packetSender;

    private DatagramSocket socket;

    public UDPSocket(int port, IUDPPacketListener packetListener, IUDPPacketProcessor packetProcessor, IUDPPacketSender packetSender)
    {
        this.port = port;
        this.packetListener = packetListener;
        this.packetProcessor = packetProcessor;
        this.packetSender = packetSender;

        try
        {
            socket = new DatagramSocket(port);
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }

        VoiceChatMod.logger.info("Created a UDP socket on " + FMLCommonHandler.instance().getEffectiveSide() + " with port " + getPort());
    }

    public void sendPacket(byte[] data, InetAddress address, int port)
    {
        this.packetSender.sendPacket(socket, data, address, port);
    }

    @Override
    public void run()
    {
        while (!shouldClose)
        {
            //La réception des packets est bloquante
            DatagramPacket packet = packetListener.listen(socket);
            packetProcessor.processPacket(socket, packet);
        }

        socket.close();
    }

    public void openSocket()
    {
        this.start();
    }

    public void closeSocket()
    {
        this.shouldClose = true;
    }

    public int getPort()
    {
        return port;
    }
}
