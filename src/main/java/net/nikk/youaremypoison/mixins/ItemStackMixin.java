package net.nikk.youaremypoison.mixins;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Redirect(
            method = "getTooltipLines(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/world/item/ItemStack.hasTag ()Z"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "net/minecraft/network/chat/MutableComponent.withStyle (Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;"),
                    to = @At(value = "INVOKE", target = "net/minecraft/nbt/CompoundTag.getAllKeys ()Ljava/util/Set;")
            )
    )
    private boolean injected(ItemStack itemStack, @Nullable Player player, TooltipFlag context) {
        return !itemStack.isEdible() && itemStack.hasTag();
    }
}
