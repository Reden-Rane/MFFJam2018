package fr.reden.voicechat.client;

import fr.reden.voicechat.client.audio.AudioManager;
import fr.reden.voicechat.common.CommonProxy;
import fr.reden.voicechat.common.VoiceChatMod;
import fr.reden.voicechat.common.item.ItemRegistry;
import fr.reden.voicechat.common.network.audio.client.AudioNetworkClientBound;
import fr.reden.voicechat.common.network.audio.client.IAudioNetworkClientBound;
import fr.reden.voicechat.common.network.audio.client.UDPAudioSocketClient;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.openal.OpenALException;

import java.net.SocketException;

public class ClientProxy extends CommonProxy
{
    private final IAudioNetworkClientBound audioNetworkClientBound = new AudioNetworkClientBound();

    public ClientProxy()
    {
        try
        {
            this.audioNetworkClientBound.openSocket(new UDPAudioSocketClient(this.audioNetworkClientBound));
        }
        catch (SocketException e)
        {
            VoiceChatMod.logger.error("Error while opening the client socket.");
            e.printStackTrace();
        }
    }

    @Override
    public void preInit()
    {
        super.preInit();
        ItemRegistry.registerItemModels();
        MinecraftForge.EVENT_BUS.register(getAudioNetworkClientBound());
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }

    @Override
    public void init()
    {
        super.init();
        try
        {
            AudioManager.getInstance().init();
        }
        catch (OpenALException e)
        {
            VoiceChatMod.logger.error("Unable to initialize the audio manager.");
            e.printStackTrace();
        }
    }

    @Override
    public void postInit()
    {
        super.postInit();
    }

    public IAudioNetworkClientBound getAudioNetworkClientBound()
    {
        return audioNetworkClientBound;
    }
}
