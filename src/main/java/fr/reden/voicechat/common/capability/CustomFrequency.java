package fr.reden.voicechat.common.capability;

//TODO Préférer un fichier côté serveur qui sera synchro plutôt qu'une capability
public class CustomFrequency
{
    public final String name;
    public final int frequency;

    public CustomFrequency(String name, int frequency)
    {
        this.name = name;
        this.frequency = frequency;
    }
}
