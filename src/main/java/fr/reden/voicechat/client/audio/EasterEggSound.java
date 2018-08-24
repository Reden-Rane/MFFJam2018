package fr.reden.voicechat.client.audio;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;

public class EasterEggSound
{
    private RepeatableSound sound;
    private final SoundEvent soundEvent;
    private final int frequency;
    private final float volume;

    public EasterEggSound(int frequency, SoundEvent soundEvent, float volume)
    {
        this.soundEvent = soundEvent;
        this.frequency = frequency;
        this.volume = volume;
    }

    public void update()
    {
        EntityPlayer player = Minecraft.getMinecraft().player;

        if (TransmissionUtil.canPlayerReceiveOnFrequency(player, this.frequency))
        {
            if (!isPlaying())
            {
                startPlaying();
            }
        }
        else
        {
            if (isPlaying())
            {
                stopPlaying();
            }
        }
    }

    public void startPlaying()
    {
        this.sound = new RepeatableSound(this.soundEvent, this.volume);

        Minecraft.getMinecraft().getSoundHandler().playSound(this.sound);
    }

    public void stopPlaying()
    {
        if (sound != null)
        {
            sound.stop();
        }
    }

    public boolean isPlaying()
    {
        return Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(sound);
    }
}