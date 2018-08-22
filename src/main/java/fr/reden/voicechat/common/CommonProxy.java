package fr.reden.voicechat.common;

import fr.reden.voicechat.common.capability.CapabilityEventHandler;
import fr.reden.voicechat.common.capability.CustomFrequenciesCapability;
import fr.reden.voicechat.common.item.ItemRegistry;
import fr.reden.voicechat.common.network.NetworkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CommonProxy
{
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

}
