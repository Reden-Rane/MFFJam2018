package fr.reden.voicechat.common;

public class CommonProxy
{

    public void preInit()
    {
        NetworkManager.getInstance().registerNetwork();
    }

    public void init()
    {

    }

    public void postInit()
    {

    }

}
