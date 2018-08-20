package fr.reden.voicechat.common;

import fr.reden.voicechat.common.network.PacketAudioSample;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkManager
{
    private static NetworkManager instance;

    private SimpleNetworkWrapper networkWrapper;

    public void registerNetwork()
    {
        this.networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("pigmessenger_channel01");
        this.networkWrapper.registerMessage(PacketAudioSample.class, PacketAudioSample.class, 0, Side.CLIENT);
        this.networkWrapper.registerMessage(PacketAudioSample.class, PacketAudioSample.class, 1, Side.SERVER);
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
