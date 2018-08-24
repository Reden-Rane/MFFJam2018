package fr.reden.voicechat.common.network.audio.client;

import fr.reden.voicechat.common.VoiceChatMod;
import fr.reden.voicechat.common.network.NetworkManager;
import fr.reden.voicechat.common.network.audio.auth.CPacketClientInfo;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AudioNetworkClientBound implements IAudioNetworkClientBound
{
    private UDPAudioSocketClient socketClient;
    private AudioNetworkServerInfo serverInfo;
    private boolean authenticated;
    private boolean hasStartedAuthentication;

    @SubscribeEvent
    public void authenticationEventHandler(TickEvent.ClientTickEvent event)
    {
        if (Minecraft.getMinecraft().player != null)
        {
            if (!isAuthenticated() && !hasStartedAuthentication())
            {
                authenticate();
            }
        }
        else if (isAuthenticated())
        {
            disconnect();
        }
    }

    @Override
    public void authenticate()
    {
        this.hasStartedAuthentication = true;
        VoiceChatMod.logger.info("Connecting the client to the server's audio network...");
        NetworkManager.getInstance().getNetworkWrapper().sendToServer(new CPacketClientInfo(getSocket().getPort()));
    }

    @Override
    public void markAsAuthenticated()
    {
        VoiceChatMod.logger.info("Client successfully connected to the server's audio network.");
        this.authenticated = true;
    }

    @Override
    public void disconnect()
    {
        this.serverInfo = null;
        this.authenticated = false;
        this.hasStartedAuthentication = false;
        VoiceChatMod.logger.info("Client disconnected from the server's audio network.");
    }

    @Override
    public void closeSocket()
    {
        this.socketClient.closeSocket();
        VoiceChatMod.logger.info("Closed client audio socket.");
    }

    @Override
    public void openSocket(UDPAudioSocketClient socket)
    {
        this.socketClient = socket;
        this.socketClient.openSocket();
    }

    @Override
    public void storeAudioServerPort(int serverAudioPort)
    {
        this.serverInfo = new AudioNetworkServerInfo(serverAudioPort);
        VoiceChatMod.logger.info("Received server's audio info (audio port).");
    }

    @Override
    public AudioNetworkServerInfo getServerInfo()
    {
        return serverInfo;
    }

    @Override
    public UDPAudioSocketClient getSocket()
    {
        return this.socketClient;
    }

    @Override
    public boolean isAuthenticated()
    {
        return authenticated;
    }

    @Override
    public boolean hasStartedAuthentication()
    {
        return hasStartedAuthentication;
    }
}
