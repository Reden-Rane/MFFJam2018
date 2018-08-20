package fr.reden.pigmessenger.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

import static fr.reden.pigmessenger.common.PigMessengerMod.*;

@Mod(name = MOD_NAME, modid = MOD_ID, version = MOD_VERSION)
public class PigMessengerMod
{
    public static final String MOD_NAME = "PigMessenger";
    public static final String MOD_ID = "pigmessenger";
    public static final String MOD_VERSION = "1.0";
    public static final String CLIENT_PROXY = "fr.reden.pigmessenger.client.ClientProxy";
    public static final String COMMON_PROXY = "fr.reden.pigmessenger.common.CommonProxy";

    @Mod.Instance
    private static PigMessengerMod instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    private static CommonProxy proxy;

    public static PigMessengerMod getInstance()
    {
        return instance;
    }

    public static CommonProxy getProxy()
    {
        return proxy;
    }
}
