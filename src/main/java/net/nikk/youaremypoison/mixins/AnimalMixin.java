package net.nikk.youaremypoison.mixins;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Animal.class)
public class AnimalMixin {
    @Inject(method = "usePlayerItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
    private void injected(Player player, InteractionHand hand, ItemStack stack, CallbackInfo ci) {
        if(stack.getOrCreateTag().getBoolean("effects")) {
            List<MobEffectInstance> t = PotionUtils.getMobEffects(stack);
            for (MobEffectInstance statusEffectInstance : t) {
                ((Animal) (Object) this).addEffect(statusEffectInstance);
            }
        }
    }
}
