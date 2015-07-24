package singles420.entrision.com.singles420;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by travis on 7/24/15.
 */
public class SwipeCardAdapter extends ArrayAdapter<Person> {
    private final Context context;
    private final ArrayList<Person> data;
    private final int layoutResourceId;

    public SwipeCardAdapter(Context context, int layoutResourceId, ArrayList<Person> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView)row.findViewById(R.id.name);
            holder.location = (TextView)row.findViewById(R.id.location);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Person person = data.get(position);

        holder.name.setText(person.firstName);
        holder.location.setText(person.location);

        return row;
    }

    static class ViewHolder
    {
        TextView name;
        TextView location;
    }
}
