package at.aau.pokerfox.partypoker.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import at.aau.pokerfox.partypoker.R;

/**
 * Created by Andreas on 28.05.2018.
 */

public class CardListAdapter extends ArrayAdapter<DrawableCard> {
    private final ArrayList<DrawableCard> list;
    private final Activity contextActivity;

    static class ViewHolder {
        protected TextView cardName;
        protected ImageView image;
    }

    public CardListAdapter(Activity contextActivity, ArrayList <DrawableCard> list) {
        super(contextActivity, R.layout.activity_cardimages_row, list); //is not an inner class
        this.contextActivity = contextActivity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = contextActivity.getLayoutInflater();
            view = inflator.inflate(R.layout.activity_cardimages_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.cardName = (TextView) view.findViewById(R.id.cardName);
            viewHolder.image = (ImageView) view.findViewById(R.id.adapterImage);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.cardName.setText(list.get(position).getName());
        holder.image.setImageDrawable(list.get(position).getImage());
        return view;
    }
}
