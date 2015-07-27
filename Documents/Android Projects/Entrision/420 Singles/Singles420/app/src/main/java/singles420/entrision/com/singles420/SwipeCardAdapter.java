package singles420.entrision.com.singles420;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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
            holder.image = (ImageView)row.findViewById(R.id.main_image);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Person person = data.get(position);

        holder.name.setText(person.firstName);
        holder.location.setText(person.location);

        if (person.images.length > 0) {
            new URLImage(holder.image).execute(person.images[0]);
        } else {
            holder.image.setImageResource(R.drawable.no_photo);

        }

        return row;
    }

    static class ViewHolder
    {
        TextView name;
        TextView location;
        ImageView image;
    }
}
