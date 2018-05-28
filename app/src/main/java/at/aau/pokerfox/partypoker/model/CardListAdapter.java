package at.aau.pokerfox.partypoker.model;

import android.app.Activity;
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

    public CardListAdapter(Activity context, ArrayList <DrawableCard> list) {
        super(context, R.layout.activity_countrycode_row, list);
        contextActivity = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.activity_countrycode_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.flag = (ImageView) view.findViewById(R.id.flag);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
        holder.flag.setImageDrawable(list.get(position).getFlag());
        return view;
    }
}
