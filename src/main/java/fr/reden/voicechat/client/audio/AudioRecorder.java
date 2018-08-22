package fr.reden.voicechat.client.audio;

import fr.reden.voicechat.common.network.NetworkManager;
import fr.reden.voicechat.common.network.PacketAudioSample;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCdevice;
import org.lwjgl.openal.OpenALException;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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

    private final ByteBuffer dataBuffer;

    private final IntBuffer availableSamples = BufferUtils.createIntBuffer(1);

    /**
     * Le microphone qu'on utilisera pour l'acquisition
     */
    private final ALCdevice microphoneDevice;

    private boolean isRecording = false;

    //TODO - SUPPRIMER, SEULEMENT POUR LES TESTS
    private final Source playbackSource = new Source();

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

        this.dataBuffer = BufferUtils.createByteBuffer(this.sampleBufferSize * SAMPLE_BYTES_SIZE);
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
                    ALC11.alcCaptureSamples(this.microphoneDevice, this.dataBuffer, this.sampleBufferSize);

//                    System.out.println("Recorded data");

                    if (FMLCommonHandler.instance().getMinecraftServerInstance() != null && Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null)
                    {
                        Minecraft.getMinecraft().addScheduledTask(() -> NetworkManager.getInstance().getNetworkWrapper().sendToServer(new PacketAudioSample(sampleBufferSize * SAMPLE_BYTES_SIZE, dataBuffer)));
                    }


/*                    //Principe à utiliser dans les packets
                    ByteBuf pktBuf = Unpooled.copiedBuffer(this.dataBuffer);
                    ByteBuffer newBuf = BufferUtils.createByteBuffer(this.sampleBufferSize * SAMPLE_BYTES_SIZE);
                    pktBuf.readBytes(newBuf);
                    newBuf.rewind();*/

//                    buf.writeBytes(this.dataBuffer, this.dataBuffer.remaining());

/*                    //TODO - SUPPRIMER, SEULEMENT POUR LES TESTS
                    int oaBuffer = AL10.alGenBuffers();
                    AL10.alBufferData(oaBuffer, SAMPLE_FORMAT, newBuf, SAMPLE_FREQUENCY);
                    playbackSource.pushBufferToQueue(oaBuffer);
                    playbackSource.popBufferFromQueue();*/
                }
            }
        }
    }
}
