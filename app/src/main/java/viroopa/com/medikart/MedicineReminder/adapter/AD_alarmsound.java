package viroopa.com.medikart.MedicineReminder.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;



import java.util.List;

import viroopa.com.medikart.MedicineReminder.MRA_MonitorSetting;
import viroopa.com.medikart.MedicineReminder.MRA_SetReminder;
import viroopa.com.medikart.MedicineReminder.Model.M_alarmsoundlist;
import viroopa.com.medikart.R;

/**
 * Created by ABCD on 30/04/2016.
 */
public class AD_alarmsound extends ArrayAdapter<M_alarmsoundlist> {

    private LayoutInflater inflater;
   // private MRA_SetReminder.SoundClickListener clickCallback;
    private int selected = -1;
    private int previousSelected = -1;
    private View.OnClickListener listener;
    public MediaPlayer player;

    public int getSelected() {
        return selected;
    }
    public void setSelected(int selected) {
        this.previousSelected = this.selected;
        this.selected = selected;
    }

   /* public AD_alarmsound(Context context, List<M_alarmsoundlist> sounds,
                         MRA_SetReminder.SoundClickListener callback) {
        super(context, -1, sounds);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.clickCallback = callback;
        listener = new SoundClickListener();
        player = new MediaPlayer();

    }
*/
    public AD_alarmsound(Context context, List<M_alarmsoundlist> sounds
                        ) {
        super(context, -1, sounds);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        player = new MediaPlayer();
    }

    @Override
    public View getView(final int position, View convertView,final  ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.alarm_sound, null);
            convertView.setOnClickListener(listener);
            convertView.findViewById(R.id.radio).setOnClickListener(listener);

        }
        convertView.setTag(position);
        M_alarmsoundlist sound = getItem(position);
        TextView title = (TextView)convertView.findViewById(R.id.title);
        title.setText(sound.getTitle());
        RadioButton radio = (RadioButton)convertView.findViewById(R.id.radio);
        radio.setTag(position);
        radio.setChecked(selected == position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = (Integer)v.getTag();
                setSelected(id);

                M_alarmsoundlist sound = getItem(position);
                // Open file dialog if "Custom" option selected.



                // Play music otherwise.
                try {

                    if (player.isPlaying()) {
                        player.stop();
                    }
                    player.reset();
                    player.setDataSource(parent.getContext(), sound.getUri());
                    player.prepare();
                    player.start();
                } catch (Exception e) {
                    // Don't know what should I do here.
                    Log.e(getClass().getName(), "Can't play sound", e);
                }
                notifyDataSetChanged();
                notifyDataSetChanged();
            }

        });

        return convertView;
    }

    public void selectPrevious() {
        this.selected = this.previousSelected;
        notifyDataSetChanged();
    }

    /*private class SoundClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int id = (Integer)view.getTag();
            setSelected(id);
            clickCallback.onClick(selected);
            notifyDataSetChanged();
        }


    }*/
}
