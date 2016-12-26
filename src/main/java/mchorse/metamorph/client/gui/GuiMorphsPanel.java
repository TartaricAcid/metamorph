package mchorse.metamorph.client.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.client.gui.Gui;

public class GuiMorphsPanel extends Gui
{
    private Map<String, List<AbstractMorph>> morphs = new HashMap<String, List<AbstractMorph>>();

    private final int margin = 20;

    private int selected = -1;
    private int perRow = 6;

    private float scroll = 0;
    private boolean dragging = false;
}