package fr.reden.voicechat.common;

import fr.reden.voicechat.common.capability.CapabilityEventHandler;
import fr.reden.voicechat.common.capability.CustomFrequenciesCapability;
import fr.reden.voicechat.common.item.ItemRegistry;
import fr.reden.voicechat.common.network.NetworkManager;
import fr.reden.voicechat.common.network.audio.server.AudioNetworkServerBound;
import fr.reden.voicechat.common.network.audio.server.UDPAudioSocketServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.net.SocketException;

public class CommonProxy
{
    private final AudioNetworkServerBound audioNetworkServerBound = new AudioNetworkServerBound();

    @CapabilityInject(CustomFrequenciesCapability.class)
    public static final Capability<CustomFrequenciesCapability> SAVED_FREQUENCIES_CAPABILITY = null;

    public void preInit()
    {
        NetworkManager.getInstance().registerNetwork();
        MinecraftForge.EVENT_BUS.register(new CapabilityEventHandler());
        MinecraftForge.EVENT_BUS.register(new ItemRegistry());
    }

    public void init()
    {
        CustomFrequenciesCapability.register();
    }

    public void postInit()
    {

    }

    public void openServerSocket()
    {
        try
        {
            this.audioNetworkServerBound.openSocket(new UDPAudioSocketServer(this.audioNetworkServerBound));
        }
        catch (SocketException e)
        {
            VoiceChatMod.logger.error("Error while opening the server socket.");
            e.printStackTrace();
        }
    }

    public AudioNetworkServerBound getAudioNetworkServerBound()
    {
        return audioNetworkServerBound;
    }
}
