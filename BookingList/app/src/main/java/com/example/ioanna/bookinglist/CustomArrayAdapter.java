package com.example.ioanna.bookinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ioanna on 09/07/2017.
 */

public class CustomArrayAdapter extends ArrayAdapter<Book> {

    public CustomArrayAdapter(Context context, ArrayList<Book> items) {
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
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
        }
        final Book currentItem = getItem(position);
        // Set Title
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.txt_title);
        String title = currentItem.getTitle();
        if(title.isEmpty())
            titleTextView.setVisibility(View.GONE);
        else
            titleTextView.setText(title);
        // Set subtitle
        TextView subTitleTextView = (TextView) listItemView.findViewById(R.id.txt_subtitle);
        String subttitle = currentItem.getSubtitle();
        if(subttitle.isEmpty())
            subTitleTextView.setVisibility(View.GONE);
        else
        subTitleTextView.setText(subttitle);
        // Set Author
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.txt_author);
        String authors = currentItem.getAuthor();
        if(authors.isEmpty())
            authorTextView.setVisibility(View.GONE);
        else
            authorTextView.setText(authors);
        // Set Image
        ImageView iconView = (ImageView) listItemView.findViewById(R.id.item_img);
        String imageURL = currentItem.getImageURL();
        if (!imageURL.isEmpty())
            new ImageLoadTask(imageURL,iconView).execute();;
        // Return the whole list item layout so that it can be shown in the ListView
        return listItemView;
    }
}