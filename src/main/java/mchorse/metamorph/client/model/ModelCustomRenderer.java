package mchorse.metamorph.client.model;

import org.lwjgl.opengl.GL11;

import mchorse.metamorph.api.models.Model;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * Custom model renderer class
 *
 * This class extended only for purpose of storing more
 */
public class ModelCustomRenderer extends ModelRenderer
{
    public Model.Limb limb;
    public Model.Transform trasnform;
    public ModelCustomRenderer parent;

    public float scaleX = 1;
    public float scaleY = 1;
    public float scaleZ = 1;

    public ModelCustomRenderer(ModelBase model, int texOffX, int texOffY)
    {
        super(model, texOffX, texOffY);
    }

    /**
     * Initiate with limb and transform instances
     */
    public ModelCustomRenderer(ModelBase model, Model.Limb limb, Model.Transform transform)
    {
        this(model, limb.texture[0], limb.texture[1]);

        this.limb = limb;
        this.trasnform = transform;
    }

    /**
     * Apply transformations on this model renderer
     */
    public void applyTransform(Model.Transform transform)
    {
        this.trasnform = transform;

        float x = transform.translate[0];
        float y = transform.translate[1];
        float z = transform.translate[2];

        this.rotationPointX = x;
        this.rotationPointY = this.limb.parent.isEmpty() ? (-y + 24) : -y;
        this.rotationPointZ = -z;

        this.rotateAngleX = transform.rotate[0] * (float) Math.PI / 180;
        this.rotateAngleY = -transform.rotate[1] * (float) Math.PI / 180;
        this.rotateAngleZ = -transform.rotate[2] * (float) Math.PI / 180;

        this.scaleX = transform.scale[0];
        this.scaleY = transform.scale[1];
        this.scaleZ = transform.scale[2];
    }

    @Override
    public void addChild(ModelRenderer renderer)
    {
        ((ModelCustomRenderer) renderer).parent = this;

        super.addChild(renderer);
    }

    @Override
    public void render(float scale)
    {
        GL11.glPushMatrix();
        GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);

        super.render(scale);

        GL11.glPopMatrix();
    }

    @Override
    public void postRender(float scale)
    {
        if (this.parent != null)
        {
            this.parent.postRender(scale);
        }

        GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);

        super.postRender(scale);
    }
}