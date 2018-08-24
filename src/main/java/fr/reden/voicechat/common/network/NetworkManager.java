package fr.reden.voicechat.common.network;

import fr.reden.voicechat.common.GuiHandler;
import fr.reden.voicechat.common.VoiceChatMod;
import fr.reden.voicechat.common.network.audio.auth.CPacketClientInfo;
import fr.reden.voicechat.common.network.audio.auth.SPacketServerInfo;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkManager
{
    private static NetworkManager instance;

    private SimpleNetworkWrapper networkWrapper;

    private NetworkManager() {}

    public void registerNetwork()
    {
        int id = -1;

        this.networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("voice_channel");
        this.networkWrapper.registerMessage(PacketCustomFrequenciesCapability.ClientHandler.class, PacketCustomFrequenciesCapability.class, ++id, Side.CLIENT);
        this.networkWrapper.registerMessage(PacketCustomFrequenciesCapability.ServerHandler.class, PacketCustomFrequenciesCapability.class, ++id, Side.SERVER);

        this.networkWrapper.registerMessage(SPacketServerInfo.ClientHandler.class, SPacketServerInfo.class, ++id, Side.CLIENT);
        this.networkWrapper.registerMessage(CPacketClientInfo.ServerHandler.class, CPacketClientInfo.class, ++id, Side.SERVER);

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
