package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedClass;
import org.nakedobjects.object.NakedClassManager;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.control.Permission;
import org.nakedobjects.object.reflect.AssociateCommand;
import org.nakedobjects.object.reflect.OneToOneAssociation;
import org.nakedobjects.security.Session;
import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.Color;
import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.ContentDrag;
import org.nakedobjects.viewer.skylark.Location;
import org.nakedobjects.viewer.skylark.MenuOptionSet;
import org.nakedobjects.viewer.skylark.ObjectContent;
import org.nakedobjects.viewer.skylark.OneToOneContent;
import org.nakedobjects.viewer.skylark.Size;
import org.nakedobjects.viewer.skylark.Style;
import org.nakedobjects.viewer.skylark.Text;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.ViewAxis;
import org.nakedobjects.viewer.skylark.ViewSpecification;
import org.nakedobjects.viewer.skylark.core.AbstractView;

import org.apache.log4j.Logger;

public class EmptyField extends AbstractView {
	private static final Logger LOG = Logger.getLogger(EmptyField.class);
	private static Text style = Style.NORMAL;
    
	public EmptyField(Content content, ViewSpecification specification, ViewAxis axis) {
		super(content, specification, axis);
		if(!(content instanceof OneToOneContent)) {
			throw new IllegalArgumentException("Content for EmptyField must be AssociationContent: " + content);
		}
		NakedObject object = ((OneToOneContent) getContent()).getObject();
		if(object != null) {
		    throw new IllegalArgumentException("Content for EmptyField must be null: " + object);
		}
	}
    
    private boolean canDrop(ContentDrag drag) {
        NakedObject source = ((ObjectContent) drag.getSourceContent()).getObject();
        
        if (source instanceof NakedClass) {
            return true;
        } else {
            NakedObject target = ((ObjectContent) getParent().getContent()).getObject();
            Permission perm = getEmptyField().getAbout(Session.getSession().getSecurityContext(), target, source).canUse();

            if (perm.getReason() != null) {
                getViewManager().setStatus(perm.getReason());
            }

            //            about  = perm.isAllowed() &&
            //			field.getType().isAssignableFrom(source.getClass());
            return perm.isAllowed();
        }
    }

    private OneToOneAssociation getEmptyField() {
    	return (OneToOneAssociation) ((OneToOneContent) getContent()).getField();
	}
    
    protected String iconName() {
        String clsName = getEmptyField().getType().getName();
        return clsName.substring(clsName.lastIndexOf('.') + 1);
	}

    public String toString() {
		return "EmptyField" + getId();
	}
    
    public void dragObjectIn(ContentDrag drag) {
        if (canDrop(drag)) {
            getState().setCanDrop();
        } else {
            getState().setCantDrop();
        }

        markDamaged();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);

        Color color;

        if (getState().canDrop()) {
            color = Style.VALID;
        } else if (getState().cantDrop()) {
            color = Style.INVALID;
        } else if (getState().isViewIdentified()) {
            color = Style.PRIMARY1;
        } else {
            color = Style.SECONDARY1;
        }

        int iconHeight = (style.getAscent() * 125) / 100;
        int iconWidth = (iconHeight * 80) / 100;
        int containerHeight = getSize().getHeight();
        int iconCentre = containerHeight / 2;

        int xt = iconWidth + (HPADDING * 2);
        int yt = getBaseline();

        int xi = getPadding().getLeft() + HPADDING;
        int yi = iconCentre - (iconHeight / 2);

        canvas.drawSolidOval(xi, yi, iconWidth, iconHeight, color);
        canvas.drawText(name(), xt, yt, color, style);

        if (AbstractView.DEBUG) {
            Size size = getSize();
            canvas.drawRectangle(0, 0, size.getWidth() - 1,
                size.getHeight() - 1, Color.DEBUG3);
            canvas.drawLine(0, size.getHeight() / 2, size.getWidth() - 1,
                size.getHeight() / 2, Color.DEBUG3);
        }
    }

    public void drop(ContentDrag drag) {
        if (canDrop(drag)) {
	        NakedObject target = ((ObjectContent) getParent().getContent()).getObject();
	        View dragSource = drag.getSourceView();
            NakedObject source = ((ObjectContent) dragSource.getContent()).getObject();
            
            NakedObject associatedObject;
            OneToOneAssociation field = getEmptyField();      
            LOG.debug("drop " + source + " on " + field + "/" + target);
            if (source instanceof NakedClass) {
                associatedObject = ((NakedClass) source).createInstance();
            } else {
                associatedObject = source;
            }
            
            getViewManager().getUndoStack().add(new AssociateCommand(target, associatedObject, field));
            // field.setAssociation(target, associatedObject);
                        
            if(! target.isPersistent()) {
                getParent().invalidateContent();
            }
        }
    }

    public NakedClass forNakedClass() {
        OneToOneAssociation field = getEmptyField();
        String className = field.getType().getName();
        return NakedClassManager.getInstance().getNakedClass(className);
    }

    /**
     * @see View#getBaseline()
     */
    public int getBaseline() {
        int containerHeight = getSize().getHeight();
        int iconCentre = containerHeight / 2;
        int yt = iconCentre + (style.getAscent() / 2);

        return yt;
    }

    public Size getRequiredSize() {
    	Size size = new Size(140, 23);
        return size;
    }

    public boolean indicatesForView(Location mouseLocation) {
        return true;
    }

    protected void init(NakedObject object) {
        if (object != null) {
            throw new IllegalArgumentException(
                "An EmptyField view must be created with a null object");
        }
    }

    /**
     * An empty field is an icon.
     */
    public boolean isOpen() {
        return false;
    }

    /**
     * An empty field should not be replaced by another view.
     */
    public boolean isReplaceable() {
        return false;
    }
    
    protected boolean shaded() {
		return true;
	}

    private String name() {
        OneToOneAssociation field = getEmptyField();

        if (field == null) {
            return "";
        } else {
            return forNakedClass().getSingularName();
        }
    }

    /**
     * Objects returned by menus are used to set this field before passing the
     * call on to the parent.
     */
    public void objectActionResult(Naked result, Location at) {
        NakedObject target = ((ObjectContent) getParent().getContent()).getObject();
        OneToOneAssociation field = getEmptyField();
        if(field.getType().isAssignableFrom(result.getClass())) {
        	field.setAssociation(target, (NakedObject) result);
        }
        super.objectActionResult(result, at);
    }

    protected String  title() {
		return  name();
	}

    public void menuOptions(MenuOptionSet options) {
        ClassOption.menuOptions(forNakedClass(), options);
    }
    
    public static class Specification implements ViewSpecification {
    	public boolean canDisplay(Naked object) {
    		return object == null;
    	}

    	public View createView(Content content, ViewAxis axis) {
    		return new EmptyField(content, this, axis);
    	}

    	public boolean isOpen() {
    		return false;
    	}

    	public boolean isReplaceable() {
    		return true;
    	}

    	public String getName() {
    		return "empty field";
    	}

    	public boolean isSubView() {
    		return true;
    	}
    }
}


/*
    Naked Objects - a framework that exposes behaviourally complete
    business objects directly to the user.
    Copyright (C) 2000 - 2003  Naked Objects Group Ltd

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    The authors can be contacted via www.nakedobjects.org (the
    registered address of Naked Objects Group is Kingsway House, 123 Goldworth
    Road, Woking GU21 1NR, UK).
*/
