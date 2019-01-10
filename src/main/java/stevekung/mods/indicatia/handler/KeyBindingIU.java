package stevekung.mods.indicatia.handler;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyModifier;

public class KeyBindingIU extends KeyBinding
{
    public KeyBindingIU(String description, KeyModifier keyModifier, InputMappings.Input keyCode)
    {
        super(description, new KeyConflictContextHandler(), keyModifier, keyCode, "key.indicatia.category");
    }

    public KeyBindingIU(String description, int keyCode)
    {
        super(description, keyCode, "key.indicatia.category");
    }
}