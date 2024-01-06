package red.jad.unboundanvil;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class RomanFixMixin {

    @Unique private static int lvl = 1;

    @Unique
    private static Text getRomanNumeral(int arabic){
        return Text.of(
                "I".repeat(arabic)
                .replace("IIIII", "V")
                .replace("IIII", "IV")
                .replace("VV", "X")
                .replace("VIV", "IX")
                .replace("XXXXX", "L")
                .replace("XXXX", "XL")
                .replace("LL", "C")
                .replace("LXL", "XC")
                .replace("CCCCC", "D")
                .replace("CCCC", "CD")
                .replace("DD", "M")
                .replace("DCD", "CM")
        );
    }

    @ModifyArg(method = "getName", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/text/MutableText;append(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;"))
    private Text fixNumeral(Text text) {
        return getRomanNumeral(lvl);
    }

    // In order to retrieve parameter of injected method.
    @Inject(method = "getName", at = @At("HEAD"))
    public void getLevel(int level, CallbackInfoReturnable<Text> cir) {
        lvl = level;
    }
}
