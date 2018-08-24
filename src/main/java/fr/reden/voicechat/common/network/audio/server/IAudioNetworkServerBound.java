package fr.reden.voicechat.common.network.audio.server;

import fr.reden.voicechat.common.network.audio.IAudioNetworkBound;
import net.minecraft.entity.player.EntityPlayerMP;

public interface IAudioNetworkServerBound extends IAudioNetworkBound<UDPAudioSocketServer>
{
    void authenticate(EntityPlayerMP playerMP, int playerAudioPort);

    void disconnect();

    AudioNetworkClientInfo getClientInfo(EntityPlayerMP playerMP);

}
