package red.jad.unboundanvil;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.screen.AnvilScreenHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class AnvilUnrestrictedMixin {

    @Mixin(AnvilScreenHandler.class)
    static class AnvilScreenHandlerMixin {
        @Redirect(method = "updateResult", at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I"
        ))
        private int uncapCombinedLevel(Enchantment instance) {
            return instance.getMaxLevel() > 1 ? Integer.MAX_VALUE : 1;
        }

        @Redirect(method = "updateResult", at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/enchantment/Enchantment;canCombine(Lnet/minecraft/enchantment/Enchantment;)Z"
        ))
        private boolean allowEnchantConflicts(Enchantment self, Enchantment other) {
            if (self == Enchantments.SILK_TOUCH) return other != Enchantments.FORTUNE;
            if (self == Enchantments.FORTUNE) return other != Enchantments.SILK_TOUCH;
            return true;
        }

        @Redirect(method = "updateResult", at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z",
                opcode = Opcodes.GETFIELD,
                ordinal = 1
        ))
        private boolean neverExpensive(PlayerAbilities instance) {
            return true;
        }

        @Inject(method = "getNextCost", at = @At(value = "RETURN"), cancellable = true)
        private static void alternateCostGrowth(int cost, CallbackInfoReturnable<Integer> cir){
            if( cost < 30 ) cir.setReturnValue(cir.getReturnValue());
            else cir.setReturnValue(cost + 1);
        }
    }

    @Mixin(AnvilScreen.class)
    static class AnvilScreenMixin {
        @Redirect(method = "drawForeground", at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z",
                opcode = Opcodes.GETFIELD
        ))
        private boolean neverExpensiveOnClient(PlayerAbilities instance) {
            return true;
        }
    }
}
