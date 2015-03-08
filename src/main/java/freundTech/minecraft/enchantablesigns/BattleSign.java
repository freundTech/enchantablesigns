package freundTech.minecraft.enchantablesigns;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSign;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BattleSign extends ItemSword{
    
    public BattleSign(ToolMaterial material) {
	super(material);
	this.maxStackSize = 16;
	setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
	if (side == EnumFacing.DOWN)
        {
            return false;
        }
        else if (!worldIn.getBlockState(pos).getBlock().getMaterial().isSolid())
        {
            return false;
        }
        else
        {
            pos = pos.offset(side);

            if (!playerIn.canPlayerEdit(pos, side, stack))
            {
                return false;
            }
            else if (!Blocks.standing_sign.canPlaceBlockAt(worldIn, pos))
            {
                return false;
            }
            else if (worldIn.isRemote)
            {
                return true;
            }
            else
            {
                if (side == EnumFacing.UP)
                {
                    int i = MathHelper.floor_double((double)((playerIn.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
                    worldIn.setBlockState(pos, Blocks.standing_sign.getDefaultState().withProperty(BlockStandingSign.ROTATION, Integer.valueOf(i)), 3);
                }
                else
                {
                    worldIn.setBlockState(pos, Blocks.wall_sign.getDefaultState().withProperty(BlockWallSign.FACING, side), 3);
                }

                --stack.stackSize;
                TileEntity tileentity = worldIn.getTileEntity(pos);

                if (tileentity instanceof TileEntitySign && !ItemBlock.setTileEntityNBT(worldIn, pos, stack))
                {
                    playerIn.openEditSign((TileEntitySign)tileentity);
                }

                return true;
            }
        }
    }
    
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.NONE;
    }
    
    @Override
    public boolean isItemTool(ItemStack stack)
    {
	return true;
    }
    
    @Override
    public float getStrVsBlock(ItemStack stack, Block block)
    {
        return 1.0F;
    }
    
    @Override
    public boolean canHarvestBlock(Block blockIn)
    {
        return false;
    }
    
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return false;
    }

}
