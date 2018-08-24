package fr.reden.voicechat.common.network.audio;

public interface IAudioNetworkBound<T extends UDPAudioSocket>
{

    void closeSocket();

    void openSocket(T audioSocketServer);

    T getSocket();

}
