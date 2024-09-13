package codes.domino.bouncingminecarts.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(AbstractMinecartEntity.class)
public abstract class MinecartMixin extends Entity {

    @Unique
    private static final Set<Block> whitelistedBlocks = Set.of(Blocks.HOPPER);
    @Unique
    private static final Set<TagKey<Block>> whitelistedTags = Set.of(BlockTags.TRAPDOORS);

    public MinecartMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "willHitBlockAt", at = @At("HEAD"), cancellable = true)
    private void injected(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {

        AbstractMinecartEntity thisEntity = (AbstractMinecartEntity) (Object) this;
        BlockState blockState = thisEntity.getWorld().getBlockState(pos);

        boolean returnValue = blockState.isSolidBlock(thisEntity.getWorld(), pos)
                || whitelistedBlocks.stream().anyMatch(blockState::isOf)
                || whitelistedTags.stream().anyMatch(blockState::isIn);

        cir.setReturnValue(returnValue);
    }
}
