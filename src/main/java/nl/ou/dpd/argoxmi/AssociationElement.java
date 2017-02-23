/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd.argoxmi;

import nl.ou.dpd.fourtuples.FT_constants;
import nl.ou.dpd.fourtuples.FourTuple;
import org.xml.sax.Attributes;

/**
 *
 * @author E.M.van Doorn
 */

public class AssociationElement implements Constants, VerwerkSAXTags {
    // Association is denoted  from left to right
    // Aggregation and composite sign is denoted at right side.

    private VerwerkSAXTags handler;
    private boolean isAggregate, isComposite, isNavigable, isFirstPart;
    private String leftElement, rightElement;

    AssociationElement() {
        handler = null;
        isFirstPart = true;
        leftElement = null;
        rightElement = null;
    }

    public void startElement(String qName, Attributes attributes) {
        switch (qName)
        {
            case ASSOCIATION_END_TAG:
            {
                if (isFirstPart)
                {
                    isNavigable = attributes.getValue(IS_NAVIGABLE).equals("true");
                    isAggregate = attributes.getValue(AGGREGATION).equals(AGGREGATE);
                    isComposite = attributes.getValue(AGGREGATION).equals(COMPOSITE);

                } else
                {
                    isNavigable = isNavigable && attributes.getValue(IS_NAVIGABLE).equals("true");
                    isAggregate = isAggregate || attributes.getValue(AGGREGATION).equals(AGGREGATE);
                    isComposite = isComposite || attributes.getValue(AGGREGATION).equals(COMPOSITE);
                }

                break;
            }

            case INTERFACE_TAG:
            case CLASS_TAG:
            {
                if (isFirstPart)
                {
                    if (isAggregate || isComposite || isNavigable)
                    {
                        rightElement = attributes.getValue(ID_REF);
                    } else
                    {
                        leftElement = attributes.getValue(ID_REF);
                    }

                    isFirstPart = false;
                } else
                {
                    if (leftElement == null)
                    {
                        leftElement = attributes.getValue(ID_REF);
                    } else
                    {
                        rightElement = attributes.getValue(ID_REF);
                    }

                    ArgoXMI.associationElements.add(this);
                }

                break;
            }

            default:
                break;
        }

    }

    public VerwerkSAXTags endElement(String qName) {

        if (handler != null)
        {
            handler = handler.endElement(qName);

            return this;
        }

        return qName.equals(ASSOCIATION_TAG) ? null : this;
    }

    public FourTuple getFourtuple() {
        int type;

        if (isAggregate)
        {
            type = FT_constants.AGGREGATE;
        } else if (isComposite)
        {
            type = FT_constants.COMPOSITE;
        } else if (isNavigable)
        {
            type = FT_constants.ASSOCIATION;
        } else
        {
            type = FT_constants.ASSOCIATION_DIRECTED;
        }

        return new FourTuple(ArgoXMI.classElements.get(leftElement).getName(),
                ArgoXMI.classElements.get(rightElement).getName(), type);
    }
}
