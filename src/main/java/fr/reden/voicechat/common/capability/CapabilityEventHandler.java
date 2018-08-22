package fr.reden.voicechat.common.capability;

import fr.reden.voicechat.common.CommonProxy;
import fr.reden.voicechat.common.VoiceChatMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityEventHandler
{

    @SubscribeEvent
    public void onPlayerLoggedIn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event)
    {
        EntityPlayer player = event.player;
        CustomFrequenciesCapability cap = player.getCapability(CommonProxy.SAVED_FREQUENCIES_CAPABILITY, null);

        if (!event.player.world.isRemote)
        {
            cap.sync();
        }
    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event)
    {
        if (event.isWasDeath())
        {
            if (event.getOriginal().hasCapability(CommonProxy.SAVED_FREQUENCIES_CAPABILITY, null))
            {
                CustomFrequenciesCapability cap = event.getOriginal().getCapability(CommonProxy.SAVED_FREQUENCIES_CAPABILITY, null);
                CustomFrequenciesCapability newCap = event.getEntityPlayer().getCapability(CommonProxy.SAVED_FREQUENCIES_CAPABILITY, null);
                newCap.setCustomFrequencyList(cap.getCustomFrequencyList());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event)
    {
        if (!event.player.world.isRemote)
        {
            event.player.getCapability(CommonProxy.SAVED_FREQUENCIES_CAPABILITY, null).sync();
        }
    }

    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
        {
            event.addCapability(new ResourceLocation(VoiceChatMod.MOD_ID + ":SAVED_FREQUENCIES_CAPABILITY"), new CustomFrequenciesCapability((EntityPlayer) event.getObject()));
        }
    }

}
