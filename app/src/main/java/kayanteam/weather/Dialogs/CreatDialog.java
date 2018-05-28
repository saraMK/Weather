package kayanteam.weather.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kayanteam.weather.R;
import kayanteam.weather.Retrofit.CitisModel;
import kayanteam.weather.Utilies.Operations;

/**
 * Created by mosta on 3/29/2018.
 */

public class CreatDialog {
    private TextView cityName,humidity,pressure,wind,weather,temp,bigTemp,smallTemp;
    private ImageView imageView;
    private Context context;

    public  Dialog create_dialog(Context context) {
        this.context=context;
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.height=WindowManager.LayoutParams.MATCH_PARENT;
        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity= Gravity.CENTER;
        lp.copyFrom(dialog.getWindow().getAttributes());
        dialog.getWindow().setAttributes(lp);
        init(dialog);

        return dialog;
    }

    private  void init(Dialog dialog) {
        cityName=(TextView)dialog.findViewById(R.id.city_name);
        humidity=(TextView)dialog.findViewById(R.id.humdity);
        pressure=(TextView)dialog.findViewById(R.id.pressure);
        wind=(TextView)dialog.findViewById(R.id.wind);
        weather=(TextView)dialog.findViewById(R.id.weather);
        temp=(TextView)dialog.findViewById(R.id.temp);
        bigTemp=(TextView)dialog.findViewById(R.id.bigTemp);
        smallTemp=(TextView)dialog.findViewById(R.id.smallTemp);
        imageView=(ImageView)dialog.findViewById(R.id.icon);


    }
    public void setDialogData(CitisModel.Model model){
        cityName.setText(model.getName());
        humidity.setText(model.getMain().getHumidity()+"%");
        pressure.setText(model.getMain().getPressure()+" hpa");
        wind.setText(model.getWind().getSpeed()+" m/s ,"+Operations.roundTwoDecimals(model.getWind().getDeg())+"deg");
        weather.setText(model.getWeather().get(0).getDescription());
        temp.setText(Operations.roundTwoDecimals(model.getMain().getTemp())+"°C");
        bigTemp.setText(Operations.roundTwoDecimals(model.getMain().getTemp_max())+"°C");
        smallTemp.setText(Operations.roundTwoDecimals(model.getMain().getTemp_min())+"°C");
        String img=Operations.IMAGE_PATH+model.getWeather().get(0).getIcon()+".png";

        Picasso.with(context).load(img).placeholder(R.drawable.logo).error(R.drawable.logo).into(imageView);

    }
    
}
