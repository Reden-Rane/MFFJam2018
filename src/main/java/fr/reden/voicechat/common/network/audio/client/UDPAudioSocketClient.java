package fr.reden.voicechat.common.network.audio.client;

import fr.reden.voicechat.common.network.audio.UDPAudioPacketListener;
import fr.reden.voicechat.common.network.audio.UDPAudioSocket;
import fr.reden.voicechat.common.network.udp.UDPPacketSender;

import java.net.SocketException;
import java.net.UnknownHostException;

import static fr.reden.voicechat.common.network.NetworkUtil.getAvailablePort;

public class UDPAudioSocketClient extends UDPAudioSocket<IAudioNetworkClientBound>
{
    public UDPAudioSocketClient(IAudioNetworkClientBound audioNetworkClientBound) throws SocketException
    {
        super(audioNetworkClientBound, getAvailablePort(), new UDPAudioPacketListener(), new UDPAudioPacketProcessorClient(), new UDPPacketSender());
    }

    public void sendPacketToServer(byte[] data)
    {
        try
        {
            AudioNetworkServerInfo audioNetworkServerInfo = getAudioNetworkBound().getServerInfo();
            if (audioNetworkServerInfo != null)
            {
                super.sendPacket(data, audioNetworkServerInfo.getAddress(), audioNetworkServerInfo.getServerAudioPort());
            }
        }
        catch (UnknownHostException | IllegalStateException e)
        {
            e.printStackTrace();
        }
    }
}
