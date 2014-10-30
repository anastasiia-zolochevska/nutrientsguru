package com.akvelon.nutrientsguru.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.TextView;

import com.akvelon.nutrientsguru.R;
import com.akvelon.nutrientsguru.persistence.DataBaseHelper;

import java.util.HashSet;
import java.util.Set;

import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_PRODUCT_GROUP;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_PRODUCT_ID;
import static com.akvelon.nutrientsguru.persistence.DataBaseHelper.C_PRODUCT_NAME;


public class ProductListAdapter extends CursorAdapter {

    private Set<String> selectedProductIds = new HashSet<String>();


    private LayoutInflater layerInflater;

    public ProductListAdapter(final Context context, final Cursor cursor, int flags) {
        super(context, cursor, flags);
        layerInflater = LayoutInflater.from(context);

        setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                String query = constraint.toString().replaceAll(" ", "%");
                return DataBaseHelper.getInstance(context).filterProducts(query);
            }
        });
    }

    static class ViewHolder {
        TextView header;
        TextView ndb;
        TextView group;
        CheckBox selected;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.header = (TextView) view.findViewById(R.id.list_item_header_text);
            holder.ndb = (TextView) view.findViewById(R.id.list_item_ndb_text);
            holder.group = (TextView) view.findViewById(R.id.list_item_group_text);
            holder.selected = (CheckBox) view.findViewById(R.id.select_product_checkbox);
            view.setTag(holder);
        }
        final String id = cursor.getString(cursor.getColumnIndex(C_PRODUCT_ID));
        holder.ndb.setText(id);
        holder.header.setText(cursor.getString(cursor.getColumnIndex(C_PRODUCT_NAME)));
        holder.group.setText(cursor.getString(cursor.getColumnIndex(C_PRODUCT_GROUP)));

        holder.selected.setChecked(selectedProductIds.contains(id));
        holder.selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v.findViewById(R.id.select_product_checkbox);

                if (cb.isChecked()) {
                    selectedProductIds.add(id);
                } else if (!cb.isChecked()) {
                    selectedProductIds.remove(id);
                }
                if(selectedProductIds.size()>3 || selectedProductIds.size()==0){

                }
            }
        });

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return layerInflater.inflate(R.layout.product_list_item, parent, false);
    }

    public Set<String> getSelectedProductIds() {
        return selectedProductIds;
    }
}
