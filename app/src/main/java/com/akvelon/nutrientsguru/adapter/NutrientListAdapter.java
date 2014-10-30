package com.akvelon.nutrientsguru.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.akvelon.nutrientsguru.R;
import com.akvelon.nutrientsguru.model.NutrientGroup;
import com.akvelon.nutrientsguru.model.NutrientListItem;
import com.akvelon.nutrientsguru.model.NutrientValue;

import java.util.List;
import java.util.Map;

public class NutrientListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<NutrientGroup> nutrientGroups;
    private Map<NutrientGroup, List<NutrientListItem>> nutrientsByGroups;


    public NutrientListAdapter(Context context, List<NutrientGroup> nutrientGroups, Map<NutrientGroup, List<NutrientListItem>> nutrientsByGroups) {
        this.context = context;
        this.nutrientGroups = nutrientGroups;
        this.nutrientsByGroups = nutrientsByGroups;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.nutrientsByGroups.get(this.nutrientGroups.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        NutrientListItem nutrientListItem = (NutrientListItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.nutrient_item, null);
        }

        ((TextView) convertView.findViewById(R.id.nutrientName)).setText(nutrientListItem.getNutrient().getName());
        ((TextView) convertView.findViewById(R.id.nutrientUnit)).setText(nutrientListItem.getNutrient().getUnit());
        ((TextView) convertView.findViewById(R.id.nutrientValue1)).setText(nutrientListItem.getValues().get(0).getValue().toString());
        if (nutrientListItem.getValues().size() > 1) {
            ((TextView) convertView.findViewById(R.id.nutrientValue2)).setText(nutrientListItem.getValues().get(1).getValue().toString());
        }
        if (nutrientListItem.getValues().size() > 2) {
            ((TextView) convertView.findViewById(R.id.nutrientValue3)).setText(nutrientListItem.getValues().get(2).getValue().toString());
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.nutrientsByGroups.get(this.nutrientGroups.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.nutrientGroups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.nutrientGroups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = ((NutrientGroup) getGroup(groupPosition)).getName();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.nutrient_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    public void recalculateNutrientValues(double factor) {
        for (NutrientGroup group : nutrientsByGroups.keySet()) {
            for (NutrientListItem nutrientListItem : nutrientsByGroups.get(group)) {
                for (NutrientValue nutrientValue : nutrientListItem.getValues()) {
                    double value = Math.round(nutrientValue.getValueFor100Gram() * factor * 1000) / 1000;
                    nutrientValue.setValue(value);
                }

            }
        }
        notifyDataSetChanged();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
