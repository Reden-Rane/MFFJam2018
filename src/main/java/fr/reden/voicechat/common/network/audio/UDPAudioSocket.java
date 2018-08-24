package fr.reden.voicechat.common.network.audio;

import fr.reden.voicechat.common.network.udp.IUDPPacketProcessor;
import fr.reden.voicechat.common.network.udp.UDPPacketSender;
import fr.reden.voicechat.common.network.udp.UDPSocket;

public abstract class UDPAudioSocket<T extends IAudioNetworkBound> extends UDPSocket
{
    private final T audioNetworkBound;

    public UDPAudioSocket(T audioNetworkBound, int port, UDPAudioPacketListener audioPacketListener, IUDPPacketProcessor packetProcessor, UDPPacketSender packetSender)
    {
        super(port, audioPacketListener, packetProcessor, packetSender);
        this.audioNetworkBound = audioNetworkBound;
    }

    public T getAudioNetworkBound()
    {
        return audioNetworkBound;
    }
}
