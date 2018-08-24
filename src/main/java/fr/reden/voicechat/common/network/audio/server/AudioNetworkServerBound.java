package fr.reden.voicechat.common.network.audio.server;

import fr.reden.voicechat.common.VoiceChatMod;
import fr.reden.voicechat.common.network.NetworkManager;
import fr.reden.voicechat.common.network.audio.auth.SPacketServerInfo;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashMap;
import java.util.UUID;

public class AudioNetworkServerBound implements IAudioNetworkServerBound
{
    private UDPAudioSocketServer socketServer;
    private HashMap<UUID, AudioNetworkClientInfo> authenticatedAudioPlayerHashMap = new HashMap<>();

    @Override
    public void authenticate(EntityPlayerMP playerMP, int playerAudioPort)
    {
        AudioNetworkClientInfo audioNetworkClientInfo = new AudioNetworkClientInfo(playerMP, playerAudioPort);
        this.authenticatedAudioPlayerHashMap.put(playerMP.getUniqueID(), audioNetworkClientInfo);

        //On envoie le port audio du serveur au client
        SPacketServerInfo serverInfoPacket = new SPacketServerInfo(getSocket().getPort());
        NetworkManager.getInstance().getNetworkWrapper().sendTo(serverInfoPacket, playerMP);

        VoiceChatMod.logger.info("Client " + playerMP.getDisplayNameString() + " authenticated.");
    }

    @Override
    public void disconnect()
    {
        this.authenticatedAudioPlayerHashMap.clear();
        VoiceChatMod.logger.info("Disconnected server bound from audio network.");
    }

    @Override
    public AudioNetworkClientInfo getClientInfo(EntityPlayerMP playerMP)
    {
        return authenticatedAudioPlayerHashMap.get(playerMP.getUniqueID());
    }

    @Override
    public void closeSocket()
    {
        this.socketServer.closeSocket();
        VoiceChatMod.logger.info("Closed server's audio socket.");
    }

    @Override
    public void openSocket(UDPAudioSocketServer socket)
    {
        this.socketServer = socket;
        this.socketServer.openSocket();
        VoiceChatMod.logger.info("Opened server's audio socket.");
    }

    @Override
    public UDPAudioSocketServer getSocket()
    {
        return this.socketServer;
    }
}
