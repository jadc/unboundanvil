package red.jad.unboundanvil;

import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilScreenHandler.class)
public class AnvilCheapRenameMixin {

    @Redirect(method = "updateResult", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/screen/Property;get()I",
            ordinal = 0))
    private int alwaysCapRenameCost(Property instance){
        return Integer.MAX_VALUE;
    }

    @ModifyConstant(method = "updateResult", constant = @Constant(intValue = 39))
    private int capRenameCost(int constant) {
        return 1;
    }
}
