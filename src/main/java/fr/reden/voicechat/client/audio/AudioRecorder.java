package fr.reden.voicechat.client.audio;

import fr.reden.voicechat.client.ClientProxy;
import fr.reden.voicechat.common.item.ItemRegistry;
import fr.reden.voicechat.common.item.ItemTalkieWalkie;
import fr.reden.voicechat.common.network.audio.AudioPacketUtil;
import fr.reden.voicechat.common.network.audio.client.IAudioNetworkClientBound;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCdevice;
import org.lwjgl.openal.OpenALException;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Set;
import java.util.TreeSet;

import static fr.reden.voicechat.common.VoiceChatMod.getProxy;
import static org.lwjgl.openal.AL10.*;

public class AudioRecorder extends Thread
{
    /**
     * Le format de nos samples (on utilisera pas le stéréo ici car il est plus lourd et inutile dans ce cas)
     */
    public static final int SAMPLE_FORMAT = AL_FORMAT_MONO16;
    /**
     * La fréquence d'acquisition des samples
     */
    public static final int SAMPLE_FREQUENCY = 44100;
    /**
     * La taille d'un sample (ici 16 car on utilise du MONO16) qu'on divise par 8 pour passer des bits en bytes
     */
    public static final int SAMPLE_BYTES_SIZE = 16 / 8;

    private final int captureInterval;

    private final int sampleBufferSize;

    private final IntBuffer availableSamples = BufferUtils.createIntBuffer(1);

    /**
     * Le microphone qu'on utilisera pour l'acquisition
     */
    private final ALCdevice microphoneDevice;

    private boolean isRecording = false;

    /**
     * @param microphoneDeviceName Le microphone à utiliser
     * @param captureInterval      L'intervalle en millisecondes correspondant à la durée de chaque petit morceau d'audio envoyé
     */
    public AudioRecorder(String microphoneDeviceName, int captureInterval) throws OpenALException
    {
        this.captureInterval = captureInterval;
        this.sampleBufferSize = (int) (SAMPLE_FREQUENCY * captureInterval / 1000.0);
        this.microphoneDevice = ALC11.alcCaptureOpenDevice(microphoneDeviceName, AudioRecorder.SAMPLE_FREQUENCY, AudioRecorder.SAMPLE_FORMAT, this.sampleBufferSize);

        if (alGetError() != AL_NO_ERROR)
        {
            throw new OpenALException("Error occured while instanciating AudioRecorder");
        }

        super.start();
    }

    public void startRecording()
    {
        if (!isRecording)
        {
            ALC11.alcCaptureStart(this.microphoneDevice);
            isRecording = true;
        }
    }

    public void stopRecording()
    {
        if (isRecording)
        {
            ALC11.alcCaptureStop(this.microphoneDevice);
            isRecording = false;
        }
    }

    @Override
    public void run()
    {
        while (true)
        {
            if (this.isRecording)
            {
                ALC10.alcGetInteger(this.microphoneDevice, ALC11.ALC_CAPTURE_SAMPLES, this.availableSamples);

                if (this.availableSamples.get(0) >= this.sampleBufferSize)
                {
                    ByteBuffer audioDataBuffer = BufferUtils.createByteBuffer(this.sampleBufferSize * SAMPLE_BYTES_SIZE);

                    ALC11.alcCaptureSamples(this.microphoneDevice, audioDataBuffer, this.sampleBufferSize);

                    if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null)
                    {
                        Set<Integer> frequencySet = new TreeSet<>();

                        EntityPlayer player = Minecraft.getMinecraft().player;

                        for (ItemStack stack : player.inventory.mainInventory)
                        {
                            if (!stack.isEmpty() && stack.getItem() == ItemRegistry.TALKIE_WALKIE)
                            {
                                boolean activated = ((ItemTalkieWalkie) ItemRegistry.TALKIE_WALKIE).isActivated(stack);
                                int frequency = ((ItemTalkieWalkie) ItemRegistry.TALKIE_WALKIE).getFrequency(stack);

                                if (activated)
                                {
                                    frequencySet.add(frequency);
                                }
                            }
                        }

                        if (frequencySet.size() > 0)
                        {
                            IAudioNetworkClientBound clientBound = ((ClientProxy) getProxy()).getAudioNetworkClientBound();

                            if (clientBound.getSocket() != null)
                            {
                                byte[] audioData = new byte[audioDataBuffer.remaining()];
                                audioDataBuffer.get(audioData);
                                clientBound.getSocket().sendPacketToServer(AudioPacketUtil.createAudioPacket(player.getUniqueID(), frequencySet, audioData));
                            }
                        }
                    }
                }
            }
        }
    }
}
