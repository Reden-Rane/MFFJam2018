package fr.reden.voicechat.common.network.audio.server;

import fr.reden.voicechat.common.network.audio.UDPAudioPacketListener;
import fr.reden.voicechat.common.network.audio.UDPAudioSocket;
import fr.reden.voicechat.common.network.udp.UDPPacketSender;
import net.minecraft.entity.player.EntityPlayerMP;

import java.net.SocketException;

import static fr.reden.voicechat.common.network.NetworkUtil.getAvailablePort;

public class UDPAudioSocketServer extends UDPAudioSocket<IAudioNetworkServerBound>
{
    public UDPAudioSocketServer(IAudioNetworkServerBound audioNetworkServerBound) throws SocketException
    {
        this(audioNetworkServerBound, getAvailablePort());
    }

    public UDPAudioSocketServer(IAudioNetworkServerBound audioNetworkServerBound, int port) throws SocketException
    {
        super(audioNetworkServerBound, port, new UDPAudioPacketListener(), new UDPAudioPacketProcessorServer(), new UDPPacketSender());
    }

    public void sendPacketToPlayer(byte[] data, EntityPlayerMP playerMP)
    {
        AudioNetworkClientInfo audioNetworkClientInfo = getAudioNetworkBound().getClientInfo(playerMP);
        this.sendPacket(data, audioNetworkClientInfo.getAddress(), audioNetworkClientInfo.getAudioPort());
    }
}
