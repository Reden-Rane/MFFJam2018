package fr.reden.voicechat.client;

import fr.reden.voicechat.client.audio.AudioManager;
import fr.reden.voicechat.client.audio.EasterEggSound;
import fr.reden.voicechat.client.audio.SoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class ClientEventHandler
{
    private static List<EasterEggSound> easterEggsSounds = new ArrayList<>();

    static
    {
        easterEggsSounds.add(new EasterEggSound(42, SoundRegistry.FREQ_42_SOUND, 0.1F));
        easterEggsSounds.add(new EasterEggSound(192, SoundRegistry.FREQ_192_SOUND, 0.1F));
        easterEggsSounds.add(new EasterEggSound(197, SoundRegistry.FREQ_197_SOUND, 0.1F));
        easterEggsSounds.add(new EasterEggSound(404, SoundRegistry.FREQ_404_SOUND, 0.1F));
        easterEggsSounds.add(new EasterEggSound(Integer.MAX_VALUE, SoundRegistry.FREQ_ELO_SOUND, 0.1F));
    }

    @SubscribeEvent
    public void updateEasterEggSounds(TickEvent.PlayerTickEvent event)
    {
        if (event.player.world.isRemote)
        {
            easterEggsSounds.forEach(EasterEggSound::update);
        }
    }

    private static float lastVoiceVolume = 1.0F;

    @SubscribeEvent
    public void updateSourcesVolume(TickEvent.ClientTickEvent event)
    {
        float volume = Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.VOICE);

        if (lastVoiceVolume != volume)
        {
            AudioManager.getInstance().setSourcesVolume(volume);
            lastVoiceVolume = volume;
        }
    }

}
