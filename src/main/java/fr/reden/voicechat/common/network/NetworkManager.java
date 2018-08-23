package fr.reden.voicechat.common.network;

import fr.reden.voicechat.common.GuiHandler;
import fr.reden.voicechat.common.VoiceChatMod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkManager
{
    private static NetworkManager instance;

    private SimpleNetworkWrapper networkWrapper;

    public void registerNetwork()
    {
        this.networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("voice_channel");
        this.networkWrapper.registerMessage(PacketAudioSample.ClientHandler.class, PacketAudioSample.class, 0, Side.CLIENT);
        this.networkWrapper.registerMessage(PacketAudioSample.ServerHandler.class, PacketAudioSample.class, 1, Side.SERVER);
        this.networkWrapper.registerMessage(PacketCustomFrequenciesCapability.ClientHandler.class, PacketCustomFrequenciesCapability.class, 2, Side.CLIENT);
        this.networkWrapper.registerMessage(PacketCustomFrequenciesCapability.ServerHandler.class, PacketCustomFrequenciesCapability.class, 3, Side.SERVER);
        NetworkRegistry.INSTANCE.registerGuiHandler(VoiceChatMod.getInstance(), new GuiHandler());
    }

    public SimpleNetworkWrapper getNetworkWrapper()
    {
        return networkWrapper;
    }

    public static NetworkManager getInstance()
    {
        if (instance == null)
        {
            instance = new NetworkManager();
        }
        return instance;
    }
}
