package com.example.ioanna.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ioanna on 15/07/2017.
 */

public class CustomArrayAdapter extends ArrayAdapter<NewFeed> {

    //Reference the child views for later actions
    ViewHolder holder;

    public CustomArrayAdapter(Context context, ArrayList<NewFeed> items) {
        super(context, 0, items);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.content_item, parent, false);
            //Cache view fields into the holder
            holder = new ViewHolder(listItemView);
            //Associate the holder with the view for later lookup
            listItemView.setTag(holder);
        } else {
            // View already exists, get the holder instance from the view
            holder = (ViewHolder) listItemView.getTag();
        }
        NewFeed currentItem = getItem(position);
        // Set Title
        String title = currentItem.getTitle();
        if (title.isEmpty())
            holder.titleTextView.setVisibility(View.GONE);
        else
            holder.titleTextView.setText(title);
        // Set Section
        String section = currentItem.getSection();
        if (section.isEmpty())
            holder.sectionTextView.setVisibility(View.GONE);
        else
            holder.sectionTextView.setText(section);
        // Set Publication Date
        String date = currentItem.getDate();
        if (date.isEmpty())
            holder.dateTextView.setVisibility(View.GONE);
        else
            holder.dateTextView.setText(date);
        // Return the whole list item layout so that it can be shown in the ListView
        return listItemView;
    }

    static class ViewHolder {
        @BindView(R.id.txt_title)
        TextView titleTextView;
        @BindView(R.id.txt_section)
        TextView sectionTextView;
        @BindView(R.id.txt_publicationDate)
        TextView dateTextView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
