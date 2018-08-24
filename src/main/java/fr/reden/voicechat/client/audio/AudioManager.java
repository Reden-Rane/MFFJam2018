package fr.reden.voicechat.client.audio;

import fr.reden.voicechat.common.VoiceChatMod;
import org.lwjgl.openal.*;
import org.lwjgl.util.vector.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AudioManager
{
    private static AudioManager instance;

    private Map<UUID, AudioSource> playerAudioSourceMap = new HashMap<>();

    private AudioManager() {}

    public void init() throws OpenALException
    {
        VoiceChatMod.logger.info("Initializing audio manager...");
        setupListener();

        if (!ALC10.alcIsExtensionPresent(AL.getDevice(), "ALC_EXT_CAPTURE"))
        {
            throw new OpenALException("ALC_EXT_CAPTURE extension not available");
        }

        VoiceChatMod.logger.info("Initializing recorder...");
        VoiceChatMod.logger.info("Default capture Device: " + getDefaultCaptureDevice());
        AudioRecorder recorder = new AudioRecorder(getDefaultCaptureDevice(), 100);
        recorder.startRecording();
        VoiceChatMod.logger.info("Initialized recorder");
        VoiceChatMod.logger.info("Initialized audio manager.");
    }

    public void setSourcesVolume(float volume)
    {
        playerAudioSourceMap.values().forEach(audioSource -> audioSource.setGain(volume));
    }

    public AudioSource getPlayerAudioSource(UUID senderUUID)
    {
        if (!playerAudioSourceMap.containsKey(senderUUID))
        {
            AudioSource playerAudioSource = new AudioSource();
            playerAudioSource.setPosition(new Vector3f(0, 0, 0), true);
            playerAudioSourceMap.put(senderUUID, playerAudioSource);
        }
        return playerAudioSourceMap.get(senderUUID);
    }

    /**
     * Initialise les valeurs par défaut du listener, ici on met 0 0 0 pour le moment car le listener n'a pas de position donné
     */
    /*
    TODO Changer la position du listener pour celle du joueur pour, par la suite, implémenter des sources fixes dans le monde et non plus une source "ambiante"
            Quelques idées:
                - entendre les talkies des autres joueurs même si on ne l'a pas en main
                - ajouter des postes radios
     */
    public void setupListener()
    {
        AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
        AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
    }

    public String[] getAvailableCaptureDevices()
    {
        return ALC10.alcGetString(null, ALC11.ALC_CAPTURE_DEVICE_SPECIFIER).split("\0");
    }

    public String getDefaultCaptureDevice()
    {
        return ALC10.alcGetString(AL.getDevice(), ALC11.ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER);
    }

    public void destroy()
    {
        for (AudioSource audioSource : playerAudioSourceMap.values())
        {
            audioSource.destroy();
        }
    }

    public static AudioManager getInstance()
    {
        if (instance == null)
        {
            instance = new AudioManager();
        }
        return instance;
    }

}
