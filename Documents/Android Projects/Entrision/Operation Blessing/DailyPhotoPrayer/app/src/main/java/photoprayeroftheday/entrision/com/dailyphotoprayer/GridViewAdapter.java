package photoprayeroftheday.entrision.com.dailyphotoprayer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList data = new ArrayList();

    public GridViewAdapter(Context context, ArrayList data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.select_prayer, null);

            TextView text = (TextView) gridView.findViewById(R.id.text);
            text.setText((String) data.get(position));

            ImageView imageView = (ImageView) gridView.findViewById(R.id.image);

            imageView.setImageResource(R.drawable.hawks);
        } else {
            gridView = convertView;
        }

        return gridView;
    }
}
