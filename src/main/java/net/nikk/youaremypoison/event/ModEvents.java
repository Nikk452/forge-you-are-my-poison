package net.nikk.youaremypoison.event;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.nikk.youaremypoison.YouAreMyPoisonMod;

import java.util.List;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = YouAreMyPoisonMod.MODID)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void onItemUse(PlayerInteractEvent.RightClickItem event){
            ItemStack stack = event.getItemStack();
            Player user = event.getEntity();
            if(stack.isEdible()){
                ItemStack poisoned;
                if(stack.getOrCreateTag().getBoolean("effects")) {
                    poisoned = stack.copy();
                    poisoned.setCount(1);
                } else poisoned = stack.getItem().getDefaultInstance();
                ItemStack itemStack = user.getOffhandItem();
                if(itemStack.is(Items.POTION) || itemStack.is(Items.SPLASH_POTION)){
                    List<MobEffectInstance> list = PotionUtils.getMobEffects(itemStack);
                    if(!list.isEmpty()) {
                        PotionUtils.getCustomEffects(poisoned.getTag(),list);
                        PotionUtils.setCustomEffects(poisoned,list);
                        poisoned.getOrCreateTag().putBoolean("effects",true);
                        if(!user.getAbilities().instabuild) {
                            boolean is_potion = itemStack.is(Items.POTION);
                            itemStack.shrink(1);
                            if(is_potion) {
                                if (itemStack.isEmpty()) user.getInventory().offhand.set(0,new ItemStack(Items.GLASS_BOTTLE));
                                else user.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                            }
                        }
                        event.getLevel().playSound((Player)null, user.getX(), user.getY(), user.getZ(), itemStack.is(Items.SPLASH_POTION)? SoundEvents.SPLASH_POTION_BREAK:SoundEvents.WANDERING_TRADER_DRINK_POTION, SoundSource.PLAYERS, 0.5F, 0.4F / (event.getLevel().getRandom().nextFloat() * 0.4F + 0.8F));
                        stack.shrink(1);
                        if (stack.isEmpty()) user.getInventory().add(user.getInventory().selected, poisoned);
                        else user.getInventory().add(poisoned);
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        event.setCanceled(true);
                    }
                } else if(itemStack.is(Items.LINGERING_POTION)){
                    List<MobEffectInstance> list = PotionUtils.getMobEffects(itemStack);
                    if(!list.isEmpty()) {
                        event.getLevel().playSound((Player)null, user.getX(), user.getY(), user.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 0.5F, 0.4F / (event.getLevel().getRandom().nextFloat() * 0.4F + 0.8F));
                        PotionUtils.getCustomEffects(stack.getTag(),list);
                        PotionUtils.setCustomEffects(stack,list);
                        stack.getOrCreateTag().putBoolean("effects",true);
                        if(!user.getAbilities().instabuild) itemStack.shrink(1);
                        event.setCancellationResult(InteractionResult.SUCCESS);
                        event.setCanceled(true);
                    }
                }
            }
            if(!event.isCanceled()) event.setCancellationResult(InteractionResult.PASS);
        }

        @SubscribeEvent
        public static void onLivingUseItem(LivingEntityUseItemEvent.Finish event){
            ItemStack stack = event.getItem();
            if(stack.isEdible()){
                if(stack.getOrCreateTag().getBoolean("effects")) {
                    List<MobEffectInstance> t = PotionUtils.getMobEffects(stack);
                    for (MobEffectInstance statusEffectInstance : t) {
                        event.getEntity().addEffect(statusEffectInstance);
                    }
                }
            }
        }
    }
}
