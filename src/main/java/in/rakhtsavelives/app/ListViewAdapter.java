package in.rakhtsavelives.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater li;
    private ArrayList<ListViewItem> items;

    public ListViewAdapter(Context context, ArrayList<ListViewItem> items) {
        li = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ListViewItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = li.inflate(R.layout.list_view_row, null);
            holder.name = (TextView) convertView.findViewById(R.id.tvName);
            holder.bg = (TextView) convertView.findViewById(R.id.tvBG);
            holder.city = (TextView) convertView.findViewById(R.id.tvCity);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        holder.name.setText(items.get(position).getName());
        holder.bg.setText(items.get(position).getBg());
        holder.city.setText(items.get(position).getCity());
        return convertView;
    }

    public class ViewHolder {
        TextView name, bg, city;
    }
}
