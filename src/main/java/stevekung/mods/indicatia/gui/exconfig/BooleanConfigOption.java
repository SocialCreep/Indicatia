package stevekung.mods.indicatia.gui.exconfig;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;

@OnlyIn(Dist.CLIENT)
public class BooleanConfigOption extends ExtendedConfigOption
{
    private final Predicate<ExtendedConfig> getter;
    private final BiConsumer<ExtendedConfig, Boolean> setter;

    public BooleanConfigOption(String key, Predicate<ExtendedConfig> getter, BiConsumer<ExtendedConfig, Boolean> setter)
    {
        super(key);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public Widget createOptionButton(int x, int y, int width)
    {
        return new Button(x, y, width, 20, this.getDisplayString(), button ->
        {
            this.set();
            button.setMessage(this.getDisplayString());
        });
    }

    public void set(String value)
    {
        this.set("true".equals(value));
    }

    public void set()
    {
        this.set(!this.get());
        ExtendedConfig.INSTANCE.save();
    }

    private void set(boolean value)
    {
        this.setter.accept(ExtendedConfig.INSTANCE, value);
    }

    public boolean get()
    {
        return this.getter.test(ExtendedConfig.INSTANCE);
    }

    public String getDisplayString()
    {
        return this.getDisplayPrefix() + (this.get() ? TextFormatting.GREEN + "true" : TextFormatting.RED + "false");
    }
}