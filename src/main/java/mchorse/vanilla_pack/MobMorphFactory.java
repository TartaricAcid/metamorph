package mchorse.vanilla_pack;

import mchorse.metamorph.api.EntityUtils;
import mchorse.metamorph.api.IMorphFactory;
import mchorse.metamorph.api.MorphList;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.api.morphs.EntityMorph;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Mob morph factory
 * 
 * This is underlying morph factory. It's responsible for generating 
 * {@link EntityMorph} out of 
 */
public class MobMorphFactory implements IMorphFactory
{
    /**
     * Nothing to register here, since all of the morphs are generated on 
     * runtime 
     */
    @Override
    public void register(MorphManager manager)
    {}

    /**
     * What should I write here?
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerClient(MorphManager manager)
    {}

    /**
     * Get all available variation of vanilla mobs and default types of custom 
     * mobs
     */
    @Override
    public void getMorphs(MorphList morphs)
    {
        for (ResourceLocation location : EntityList.getEntityNameList())
        {
            String name = location.toString();

            if (this.hasMorph(name) && !morphs.hasMorph(name))
            {
                this.addMorph(morphs, name, null);
            }
        }

        /* Adding baby animal variants */
        this.addMorph(morphs, "minecraft:pig", "{Age:-1}");
        this.addMorph(morphs, "minecraft:chicken", "{Age:-1}");
        this.addMorph(morphs, "minecraft:cow", "{Age:-1}");
        this.addMorph(morphs, "minecraft:mooshroom", "{Age:-1}");
        this.addMorph(morphs, "minecraft:polar_bear", "{Age:-1}");

        /* Sheep variants */
        this.addMorph(morphs, "minecraft:sheep", "{Sheared:1b}");
        this.addMorph(morphs, "minecraft:sheep", "{Age:-1}");
        this.addMorph(morphs, "minecraft:sheep", "{Age:-1,Sheared:1b}");

        for (int i = 1; i < 16; i++)
        {
            this.addMorph(morphs, "minecraft:sheep", "{Color:" + i + "}");
        }

        this.addMorph(morphs, "minecraft:sheep", "{CustomName:\"jeb_\"}");

        /* Slime and magma cube variants */
        this.addMorph(morphs, "minecraft:slime", "{Size:1}");
        this.addMorph(morphs, "minecraft:slime", "{Size:2}");

        this.addMorph(morphs, "minecraft:magma_cube", "{Size:1}");
        this.addMorph(morphs, "minecraft:magma_cube", "{Size:2}");

        /* Adding cat variants */
        this.addMorph(morphs, "minecraft:ocelot", "{Age:-1}");

        for (int i = 1; i < 4; i++)
        {
            this.addMorph(morphs, "minecraft:ocelot", "{CatType:" + i + "}");
            this.addMorph(morphs, "minecraft:ocelot", "{CatType:" + i + ",Age:-1}");
        }

        /* Adding villager variants */
        this.addMorph(morphs, "minecraft:villager", "{ProfessionName:\"minecraft:librarian\"}");
        this.addMorph(morphs, "minecraft:villager", "{ProfessionName:\"minecraft:priest\"}");
        this.addMorph(morphs, "minecraft:villager", "{ProfessionName:\"minecraft:smith\"}");
        this.addMorph(morphs, "minecraft:villager", "{ProfessionName:\"minecraft:butcher\"}");

        /* Adding normal bat */
        this.addMorph(morphs, "minecraft:bat", "{BatFlags:2}");

        /* Adding Zombie variants */
        this.addMorph(morphs, "minecraft:zombie", "{IsBaby:1b}");

        for (int i = 1; i < 7; i++)
        {
            this.addMorph(morphs, "minecraft:zombie", "{ZombieType:" + i + "}");
        }
    }

    /**
     * Add an entity morph to the morph list
     */
    private void addMorph(MorphList morphs, String name, String json)
    {
        World world = Minecraft.getMinecraft().world;
        EntityMorph morph = new EntityMorph();
        EntityLivingBase entity = (EntityLivingBase) EntityList.createEntityByIDFromName(new ResourceLocation(name), world);

        if (entity == null)
        {
            System.out.println("Couldn't add morph '" + name + "'");
            return;
        }

        NBTTagCompound data = entity.serializeNBT();

        morph.name = name;

        if (json != null)
        {
            try
            {
                data.merge(JsonToNBT.getTagFromJson(json));
            }
            catch (NBTException e)
            {
                System.out.println("Failed to merge provided JSON data");
                e.printStackTrace();
            }
        }

        EntityUtils.stripEntityNBT(data);
        morph.setEntityData(data);
        morphs.addMorphVariant(name, morph);
    }

    /**
     * Checks if the {@link EntityList} has an entity with given name does 
     * exist and the entity is a living base.
     */
    @Override
    public boolean hasMorph(String name)
    {
        /* Nope! */
        if (name.equals("metamorph.Morph"))
        {
            return false;
        }

        Class<? extends Entity> clazz = null;

        for (EntityEntry entity : ForgeRegistries.ENTITIES)
        {
            if (entity.getName().equals(name))
            {
                clazz = entity.getEntityClass();
            }
        }

        if (clazz != null)
        {
            return EntityLivingBase.class.isAssignableFrom(clazz);
        }

        return false;
    }

    /**
     * Create an {@link EntityMorph} from NBT
     */
    @Override
    public AbstractMorph getMorphFromNBT(NBTTagCompound tag)
    {
        String name = tag.getString("Name");

        if (this.hasMorph(name))
        {
            EntityMorph morph = new EntityMorph();

            morph.fromNBT(tag);

            return morph;
        }

        return null;
    }
}