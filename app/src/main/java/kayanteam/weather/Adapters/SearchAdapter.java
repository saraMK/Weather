package kayanteam.weather.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kayanteam.weather.Dialogs.CreatDialog;
import kayanteam.weather.R;
import kayanteam.weather.Retrofit.CitisModel;
import kayanteam.weather.Utilies.Operations;


/**
 * Created by mosta on 1/31/2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private Context context;
    private List<CitisModel.Model> arrayList;
    private Dialog dialog;
    private CreatDialog creatDialog;

    private View view;

    public SearchAdapter(Context context, List<CitisModel.Model> arrayList) {
        this.context = context;
        this.arrayList = new ArrayList<>();
        this.arrayList = arrayList;
        creatDialog = new CreatDialog();
        dialog = creatDialog.create_dialog(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.weather_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        // set up any onClickListener you need on the view here
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.cityName.setText(arrayList.get(position).getName());
        holder.temp.setText(Operations.roundTwoDecimals(arrayList.get(position).getMain().getTemp()) + "°C");
        holder.humidity.setText(arrayList.get(position).getMain().getHumidity() + "%");

        holder.wind.setText(arrayList.get(position).getWind().getSpeed() + " m/s , "
                + Operations.roundTwoDecimals(arrayList.get(position).getWind().getDeg()) + "deg");

        String img = Operations.IMAGE_PATH + arrayList.get(position).getWeather().get(0).getIcon() + ".png";
        Picasso.with(context).load(img).placeholder(R.drawable.logo).error(R.drawable.logo).into(holder.icon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatDialog.setDialogData(arrayList.get(position));
                dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cityName, temp, humidity, wind;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            cityName = (TextView) itemView.findViewById(R.id.city_name);
            temp = (TextView) itemView.findViewById(R.id.temp);
            humidity = (TextView) itemView.findViewById(R.id.humdity);
            wind = (TextView) itemView.findViewById(R.id.wind);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }

    public void filter(String input) {

        List<CitisModel.Model> temp = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            CitisModel.Model model = arrayList.get(i);
            Log.d("MessageModel_model", model.getName() + "\n" + input);
            if (model.getName().toLowerCase().contains(input.toLowerCase())) {
                temp.add(model);
            }
        }


        arrayList = (temp);
        notifyDataSetChanged();

    }

    public void setArrayList(List<CitisModel.Model> arrayList) {

        this.arrayList = (arrayList);
        notifyDataSetChanged();
    }
}
