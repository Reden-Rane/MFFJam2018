package fr.reden.voicechat.common.network.audio.client;

import fr.reden.voicechat.common.network.audio.IAudioNetworkBound;

public interface IAudioNetworkClientBound extends IAudioNetworkBound<UDPAudioSocketClient>
{
    void authenticate();

    void markAsAuthenticated();

    void disconnect();

    boolean isAuthenticated();

    boolean hasStartedAuthentication();

    void storeAudioServerPort(int serverAudioPort);

    AudioNetworkServerInfo getServerInfo();

}
