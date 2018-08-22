package fr.reden.voicechat.common;

import fr.reden.voicechat.common.item.ItemRegistry;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{

    public void preInit()
    {
        NetworkManager.getInstance().registerNetwork();
        MinecraftForge.EVENT_BUS.register(new ItemRegistry());
    }

    public void init()
    {

    }

    public void postInit()
    {

    }

}
