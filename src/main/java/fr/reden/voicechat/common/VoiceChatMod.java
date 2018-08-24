package fr.reden.voicechat.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fr.reden.voicechat.common.VoiceChatMod.*;

@Mod(name = MOD_NAME, modid = MOD_ID, version = MOD_VERSION)
public class VoiceChatMod
{
    public static final String MOD_NAME = "Voice Chat";
    public static final String MOD_ID = "voicechat";
    public static final String MOD_VERSION = "1.1";
    public static final String CLIENT_PROXY = "fr.reden.voicechat.client.ClientProxy";
    public static final String SERVER_PROXY = "fr.reden.voicechat.server.ServerProxy";

    @Mod.Instance
    private static VoiceChatMod instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    private static CommonProxy proxy;

    public static final Logger logger = LogManager.getLogger(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        getProxy().preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        getProxy().init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        getProxy().postInit();
    }

    @Mod.EventHandler
    public void serverStartingEvent(FMLServerStartingEvent event)
    {
        getProxy().openServerSocket();
    }

    @Mod.EventHandler
    public void serverStoppingEvent(FMLServerStoppingEvent event)
    {
        if (getProxy().getAudioNetworkServerBound() != null)
        {
            getProxy().getAudioNetworkServerBound().disconnect();
            getProxy().getAudioNetworkServerBound().closeSocket();
        }
    }

    public static VoiceChatMod getInstance()
    {
        return instance;
    }

    public static CommonProxy getProxy()
    {
        return proxy;
    }
}
