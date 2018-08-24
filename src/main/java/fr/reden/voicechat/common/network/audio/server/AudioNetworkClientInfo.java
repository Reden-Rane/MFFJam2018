package fr.reden.voicechat.common.network.audio.server;

import net.minecraft.entity.player.EntityPlayerMP;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class AudioNetworkClientInfo
{
    private final EntityPlayerMP player;
    private final int audioPort;

    public AudioNetworkClientInfo(EntityPlayerMP player, int audioPort)
    {
        this.player = player;
        this.audioPort = audioPort;
    }

    public EntityPlayerMP getPlayer()
    {
        return player;
    }

    public InetAddress getAddress()
    {
        SocketAddress socketAddress = player.connection.getNetworkManager().getRemoteAddress();
        if (socketAddress instanceof InetSocketAddress)
        {
            InetSocketAddress netSocketAddress = (InetSocketAddress) socketAddress;
            return netSocketAddress.getAddress();
        }
        else
        {
            try
            {
                return InetAddress.getByName("localhost");
            }
            catch (UnknownHostException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int getAudioPort()
    {
        return audioPort;
    }
}
