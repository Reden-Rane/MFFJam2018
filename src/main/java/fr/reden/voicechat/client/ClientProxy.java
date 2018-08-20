package fr.reden.voicechat.client;

import fr.reden.voicechat.client.audio.AudioManager;
import fr.reden.voicechat.common.CommonProxy;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        super.preInit();
    }

    @Override
    public void init()
    {
        super.init();
        AudioManager.getInstance().init();
    }

    @Override
    public void postInit()
    {
        super.postInit();
    }
}
