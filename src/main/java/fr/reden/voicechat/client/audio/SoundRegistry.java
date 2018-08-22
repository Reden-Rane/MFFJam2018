package fr.reden.voicechat.client.audio;

import fr.reden.voicechat.common.VoiceChatMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = VoiceChatMod.MOD_ID)
public class SoundRegistry
{
    private static final List<SoundEvent> soundEventList = new ArrayList<>();

    public static final SoundEvent TALKIE_SWITCH_SOUND;
    public static final SoundEvent TALKIE_TRANSMISSION_NOTIFY_SOUND;

    static
    {
        soundEventList.add(TALKIE_SWITCH_SOUND = new SoundEvent(new ResourceLocation(VoiceChatMod.MOD_ID, "talkie_switch")));
        soundEventList.add(TALKIE_TRANSMISSION_NOTIFY_SOUND = new SoundEvent(new ResourceLocation(VoiceChatMod.MOD_ID, "talkie_transmission_notify")));
    }

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event)
    {
        SoundEvent[] soundEvents = new SoundEvent[soundEventList.size()];
        soundEventList.toArray(soundEvents);
        event.getRegistry().registerAll(soundEvents);
    }

}
