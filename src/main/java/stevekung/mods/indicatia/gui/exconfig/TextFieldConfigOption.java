package stevekung.mods.indicatia.gui.exconfig;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.exconfig.screen.widget.ExtendedTextFieldWidget;

@OnlyIn(Dist.CLIENT)
public class TextFieldConfigOption extends ExtendedConfigOption
{
    private final Function<ExtendedConfig, String> getter;
    private final BiConsumer<ExtendedConfig, String> setter;

    public TextFieldConfigOption(String key, Function<ExtendedConfig, String> getter, BiConsumer<ExtendedConfig, String> setter)
    {
        super(key);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public Widget createOptionButton(int x, int y, int width)
    {
        ExtendedTextFieldWidget textField = new ExtendedTextFieldWidget(x, y, width, this);
        this.set(textField.getText());
        textField.setText(this.get());
        textField.setDisplayName(this.getDisplayName());
        textField.setDisplayPrefix(this.getDisplayPrefix());
        return textField;
    }

    public void set(String value)
    {
        this.setter.accept(ExtendedConfig.INSTANCE, value);
    }

    public String get()
    {
        return this.getter.apply(ExtendedConfig.INSTANCE);
    }
}